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
import org.inheritsource.service.common.domain.ProcessInstanceDetails;
import org.inheritsource.service.common.domain.StartLogItem;
import org.inheritsource.service.common.domain.TimelineItem;
import org.inheritsource.service.docbox.DocBoxFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaymentForm extends Form  {
 
	public static final Logger log = LoggerFactory.getLogger(PaymentForm.class);

	public static final String START = "START";
	@Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
		super.doBeforeRender(request, response);
		
		String payActivityName = getPublicRequestParameter(request, "payActivityName"); 
		
		// find payment data 
		// There is a processActivityFormInstanceId or taskUuid
		//     a. payActivityName, Payment data from payActivityName activity in the processInstance
		//        identified by activity instance identified by processActivityFormInstanceId or taskUuid
		//     b. no payActivityName or payActivityName cannot match activity or payActivityName=START
		//         => look for payment data in start form 
		//         
		
		ActivityInstanceItem activity = (ActivityInstanceItem)request.getAttribute("activity");
		
		String formDocId = null;
		String startFormDocId = null;
		
		ProcessInstanceDetails piDetails = null;
		
		if (activity != null && activity.getActivityInstanceUuid()!=null) {
			piDetails = engine.getProcessInstanceDetailsByActivityInstance(activity.getActivityInstanceUuid(), request.getLocale());
			
			// pay process instance start form is the default behaviour
			
			for (TimelineItem item : piDetails.getTimeline().getItems()) {
				
				if (item instanceof ActivityInstanceLogItem) {
					ActivityInstanceLogItem logItem = (ActivityInstanceLogItem)item;
					if (logItem.getActivityName() != null && logItem.getActivityName().equals(payActivityName)) {
						// payActivityName match => Alternative a
						formDocId = logItem.getInstanceId();
						
					}
				}
				
				if (item instanceof StartLogItem) {
					// keep startFormDocId in memory in case of Alternative b
					StartLogItem startItem = (StartLogItem)item;
					startFormDocId = startItem.getInstanceId();
				}
			}
		}
		
		if (formDocId == null || formDocId.trim().length() == 0 ) {
			// Alternative 2b
			formDocId = startFormDocId;
		}

		DocBoxFacade docBox = new DocBoxFacade();
		DocBoxFormData docBoxFormData =  docBox.getDocBoxFormData(formDocId);
		
		String portStr = (request.getLocalPort() == 80 || request.getLocalPort() == 443) ? "" : ":" + request.getLocalPort();
		String protocolStr = request.getLocalPort() == 443 ? "https" : ":" + "http";
		String pdfUrl = protocolStr + "://" +  request.getServerName() + portStr +  "/docbox/doc/ref/" + docBoxFormData.getDocboxRef();
		
		String docNo = docBoxFormData.getDocNo();
		String pdfChecksum = docBoxFormData.getCheckSum();
		String docboxRef = docBoxFormData.getDocboxRef();
		
		StringBuffer responseUrl = request.getRequestURL();
		responseUrl.append("/confirm?docboxRef=");
		responseUrl.append(docboxRef);
		responseUrl.append("&docNo=");
		responseUrl.append(docNo);
		responseUrl.append("&formDocId=");
		responseUrl.append(activity.getInstanceId());  // the BPM activity formDocId, important to not, the payment activity 
		
		
		
		String signText = "Härmed undertecknar jag dokumentet " + pdfUrl + " med dokumentnummer [" + docNo + "] och kontrollsumman [" + pdfChecksum + "]";
		
		request.setAttribute("pdfUrl", pdfUrl);
		request.setAttribute("docNo", docNo);
		request.setAttribute("pdfChecksum", pdfChecksum);
		request.setAttribute("signText", signText);
		request.setAttribute("responseUrl", responseUrl.toString());

	}
	
}
