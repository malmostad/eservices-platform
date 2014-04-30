/* 
 *  Process Aware Web Application Platform 
 * 
 *  Copyright (C) 2011-2013 Inherit S AB 
 * 
 *  This program is free software: you can redistribute it and/or modify 
 *  it under the terms of the GNU Affero General Public License as published by 
 *  the Free Software Foundation, either version 3 of the License, or 
 *  (at your option) any later version. 
 * 
 *  This program is distributed in the hope that it will be useful, 
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 *  GNU Affero General Public License for more details. 
 * 
 *  You should have received a copy of the GNU Affero General Public License 
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>. 
 * 
 *  e-mail: info _at_ inherit.se 
 *  mail: Inherit S AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN 
 *  phone: +46 8 641 64 14 
 */ 
 
package org.inheritsource.portal.components.mycases;


import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.inheritsource.service.common.domain.ActivityInstanceItem;
import org.inheritsource.service.common.domain.DocBoxFormData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignForm extends Form  {
 
	public static final Logger log = LoggerFactory.getLogger(SignForm.class);

	public static final String START = "START";
	@Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
		super.doBeforeRender(request, response);
		
		ActivityInstanceItem activity = (ActivityInstanceItem)request.getAttribute("activity");

		DocBoxFormData docBoxFormData = engine.getDocBoxFormDataToSign(activity, request.getLocale());

		String pdfUrl = docBoxFormData.getDocUri();
		
		String docNo = docBoxFormData.getDocNo();
		String pdfChecksum = docBoxFormData.getCheckSum();
		String docboxRef = docBoxFormData.getDocboxRef();
		String signText = docBoxFormData.getSignText();
		
		StringBuffer responseUrl = request.getRequestURL();
		responseUrl.append("/confirm?docboxRef=");
		responseUrl.append(docboxRef);  // reference to the document to sign
		responseUrl.append("&docNo=");
		responseUrl.append(docNo);      // document no of the document to sign
		responseUrl.append("&instance_id=");
		responseUrl.append(activity.getInstanceId());  // motrice form instance id FormEngine.FORM_INSTANCEID to the sign activity
		 // the instance id will be used by SignFormConfirm to find the signing activity 
		
		request.setAttribute("pdfUrl", pdfUrl);
		request.setAttribute("docNo", docNo);
		request.setAttribute("pdfChecksum", pdfChecksum);
		request.setAttribute("signText", signText);
		request.setAttribute("responseUrl", responseUrl.toString());
	}
	
}
