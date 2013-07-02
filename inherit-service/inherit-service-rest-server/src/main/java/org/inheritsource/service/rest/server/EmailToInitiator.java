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
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

import org.inheritsource.service.common.domain.ActivityInstanceItem;
import org.inheritsource.service.common.domain.ProcessInstanceDetails;
import org.inheritsource.service.common.util.ParameterEncoder;
import org.inheritsource.service.orbeon.MyProfile;
import org.inheritsource.taskform.engine.TaskFormService;
import org.restlet.resource.ServerResource;
import org.restlet.resource.Get;
import org.restlet.resource.Post;


public class EmailToInitiator extends ServerResource {
	//TODO Place all configurable strings in resource config file
	private static final String SENDERADDRESS="No Reply Malmö <noreply@malmo.se>";
//	private static final String SENDERADDRESS="info@inherit.se";
	private static final String SMTPSERVER="relay.malmo.se";
//	private static final String SMTPSERVER="smtp.bredband.net";
	private static final String ERRORRECIPIENT="malmo@inherit.se";
	private static final String ERRORMESG1 = "Mail till processInitiator, fel: processInstanceDetails == null";
	private static final String ERRORMESG2 = "Mail till processInitiator, fel: processInstanceUuid tom eller null";
	TaskFormService engine = new TaskFormService();
	MyProfile myProfile = new MyProfile();
	
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
			ProcessInstanceDetails processInstanceDetails = engine.getProcessInstanceDetails(processInstanceUuid);
			if (processInstanceDetails != null) {
				to = myProfile.getEmail(processInstanceDetails.getStartedBy());
			} else {
				to=ERRORRECIPIENT;
				mailBody = ERRORMESG1; 
			}
		} else {
			to=ERRORRECIPIENT;
			mailBody = ERRORMESG2; 
		}
		try{
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO,
					new InternetAddress(to));
			message.setSubject(mailSubject.replaceAll("\\+"," "));
			message.setText(mailBody.replaceAll("\\+"," "));
			
			// Send message
			Transport.send(message);
			System.out.println("Message Sent...");
		}catch (MessagingException e) {
			e.printStackTrace();
		}
		//		return result;
		return;
	}
}
