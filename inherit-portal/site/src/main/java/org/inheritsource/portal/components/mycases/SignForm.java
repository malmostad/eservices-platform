/* == Motrice Copyright Notice == 
 * 
 * Motrice Service Platform 
 * 
 * Copyright (C) 2011-2014 Motrice AB 
 * 
 * This program is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU Affero General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. 
 * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU Affero General Public License for more details. 
 * 
 * You should have received a copy of the GNU Affero General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>. 
 * 
 * e-mail: info _at_ motrice.se 
 * mail: Motrice AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN 
 * phone: +46 8 641 64 14 
 
 */ 
 
 
package org.inheritsource.portal.components.mycases;


import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.inheritsource.service.common.domain.ActivityInstanceItem;
import org.inheritsource.service.common.domain.ActivityInstanceLogItem;
import org.inheritsource.service.common.domain.DocBoxFormData;
import org.inheritsource.service.common.domain.UserInfo;
import org.inheritsource.service.form.SignStartFormTaskHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignForm extends Form  {
 
	public static final Logger log = LoggerFactory.getLogger(SignForm.class);

	public static final String START = "START";
	@Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
		super.doBeforeRender(request, response);
		
		/*
		 * There is three possibly states of sign form
		 * 1. No signing request initiated. (viewtype=signform, error="")
		 * 2. Perform a sign request. There is a text parameter with the text to sign
		 *     a) invalid text (somone else has probably modified/signed the document => retry)
		 *     	  (viewtype=signform, error="textconflict")
		 *     b) equal text => perform sign request
		 *     	  (sucessfully performed sign request => viewtype=pendingsignreq
		 *         failed to perform signing request => viewtype=signform, error="failedsignreq")
		 * 3. Signing request is already initiated. There is a motriceFormSignTransactionId 
		 *    local task variable and attribute in the form instance. Waiting for signature.
		 *    (viewtype=pendingsignreq)
		 * 4. The sign task is completed (viewtype=signeddocument, error="")
		 * 
		 * If signing fails the motriceFormSignTransactionId task variable _must_ be removed 
		 */
		
		String text = getPublicRequestParameter(request, "text"); 

		ActivityInstanceItem activity = (ActivityInstanceItem)request.getAttribute("activity");
		UserInfo user = (UserInfo)request.getAttribute("user");
		
		String viewtype = "signform";
		String error = "";
		
		if (activity instanceof ActivityInstanceLogItem) {
			// activity already performed => state 4
			viewtype="signeddocument";
		}
		else {
			Object transactionId = activity.getAttributes().get(SignStartFormTaskHandler.FORM_SIGN_TRANSACTION_ID);
			if (transactionId != null) {
				// there is a pending signature request => state 3
				viewtype = "pendingsignreq";
			}
			else {
				
				DocBoxFormData docBoxFormData = engine.getDocBoxFormDataToSign(activity, request.getLocale());

				if (docBoxFormData != null) {
					String pdfUrl = docBoxFormData.getDocUri();
					
					String docNo = docBoxFormData.getDocNo();
					String pdfChecksum = docBoxFormData.getCheckSum();
					String docboxRef = docBoxFormData.getDocboxRef();
					String signText = docBoxFormData.getSignText();
				
					if (text!=null && text.trim().length()>0) {
						if (text.equals(signText)) {
							// state 2b
							if (this.getEngine().getActivitiEngineService().createSignRequestOfForm(activity.getInstanceId(), user.getUuid(), docBoxFormData)) {
								viewtype = "pendingsignreq";
							}
							else {
								error = "failedsignreq";
							}
							
						}
						else {
							// state 2a
							error = "textconflict";
						}
					} 
				
					request.setAttribute("pdfUrl", pdfUrl);
					request.setAttribute("docNo", docNo);
					request.setAttribute("pdfChecksum", pdfChecksum);
					request.setAttribute("signText", signText);     
				}
			}
		}
				
		request.setAttribute("viewtype", viewtype);
		request.setAttribute("errDescription", error);
	}
	
}
