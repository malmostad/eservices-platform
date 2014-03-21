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
 
package org.inheritsource.service.rest.server;

import java.util.*;
import java.util.logging.Logger;

import javax.mail.*;
import javax.mail.internet.*;

import org.inheritsource.service.common.domain.ProcessInstanceDetails;
import org.inheritsource.service.common.domain.MyProfile;
import org.inheritsource.service.common.util.ParameterEncoder;
import org.inheritsource.taskform.engine.TaskFormService;
import org.restlet.resource.Post;

import org.springframework.beans.factory.annotation.Autowired;


public class EmailToInitiator extends Emailer {

	@Autowired
	TaskFormService engine;	

	MyProfile myProfile = null;

	public EmailToInitiator() { 
		super();
	}
	
	@Post
	public  void emailToInitiator() {
		// the recipient address is the emailAddress of the process initiator
		//uri-template: /emailToInitiator/{processInstanceUuid}/{activityInstanceUuid}/{mailSubject}/{mailBody}
		//int result = 0;
		String to = null;
		String processInstanceUuid = ParameterEncoder.decode((String)getRequestAttributes().get("processInstanceUuid"));
		String activityInstanceUuid = ParameterEncoder.decode((String)getRequestAttributes().get("activityInstanceUuid"));
		String mailSubject = ParameterEncoder.decode((String)getRequestAttributes().get("mailSubject"));
		String mailBody = ParameterEncoder.decode((String)getRequestAttributes().get("mailBody"));

		log.info("processInstanceUuid: " + processInstanceUuid);
		//System.out.println("processInstanceUuid: " + processInstanceUuid);
		//System.out.println("activityInstanceUuid: " + activityInstanceUuid);
		//System.out.println("mailSubject: " + mailSubject);
		//System.out.println("mailBody: " + mailBody);

		String from = SENDERADDRESS;
		Properties properties = System.getProperties();

		// Setup mail server
		properties.setProperty("mail.smtp.host",SMTPSERVER);

		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);

/*		if (activityInstanceUuid != null && ( activityInstanceUuid.trim().length() > 0 )) {
			ActivityInstanceItem activityInstance = engine.getActivityInstanceItemByUuId(activityInstanceUuid);

			//System.out.println("===========> activityInstance: ");
			//System.out.println(activityInstance.toString());

			to = myProfile.getEmail(activityInstance.getStartedBy());

		} else if (processInstanceUuid != null && ( processInstanceUuid.trim().length() > 0 )){
			ProcessInstanceDetails processInstanceDetails = engine.getProcessInstanceDetails(processInstanceUuid);
			to = myProfile.getEmail(processInstanceDetails.getStartedBy());
		} else {
			to=ERRORRECIPIENT;
			mailBody = "Fel vid skickande av: " + mailBody; 
		}
*/
		if (processInstanceUuid != null && ( processInstanceUuid.trim().length() > 0 )){
			ProcessInstanceDetails processInstanceDetails = engine.getProcessInstanceDetails(processInstanceUuid, null);
			if (processInstanceDetails != null) {
				myProfile = engine.getMyProfile(processInstanceDetails.getStartedBy());
				to = myProfile.getEmail();
			} else {
				to=ERRORRECIPIENT;
				mailBody = ERRORMESSAGE2; 
			}
		} else {
			to=ERRORRECIPIENT;
			mailBody = ERRORMESSAGE3; 
		}
		try{
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from.replaceAll("\\+"," ")));
			message.addRecipient(Message.RecipientType.TO,
					new InternetAddress(to));

			message.setSubject(mailSubject.replaceAll("\\+"," "));
			message.setText(mailBody.replaceAll("\\+"," "));
			
			// Send message
			log.info("EmailToInitiator: Sending message to " + to  + " via smtpserver: " + SMTPSERVER);
			Transport.send(message);
		}catch (MessagingException e) {
			e.printStackTrace();
		}
		//		return result;
		return;
	}
}
