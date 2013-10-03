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
import org.inheritsource.service.common.util.ParameterEncoder;
//import org.inheritsource.taskform.engine.TaskFormService;
import org.restlet.resource.ServerResource;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

public class EmailTo extends ServerResource {
	//TODO Place all configurable strings in resource config file
//	private static final String SENDERADDRESS="No Reply Malmö <noreply@malmo.se>";
//	private static final String SMTPSERVER="relay.malmo.se";
	private static final String SMTPSERVER="smtp.bredband.net";
	private static final String ERRORRECIPIENT="malmo@inherit.se";
	private static final String ERRORMESG = "Mail till användare, fel: processInstanceUuid tom eller null";

	@Post
	public  void emailTo() {
		// the recipient address is the emailAddress of the process initiator
		//uri-template: /emailTo/{mailTo}/{mailSubject}/{mailBody}
		String mailTo = ParameterEncoder.decode((String)getRequestAttributes().get("mailTo"));
		String mailFrom = ParameterEncoder.decode((String)getRequestAttributes().get("mailFrom"));
		String mailSubject = ParameterEncoder.decode((String)getRequestAttributes().get("mailSubject"));
		String mailBody = ParameterEncoder.decode((String)getRequestAttributes().get("mailBody"));

		//System.out.println("mailSubject: " + mailSubject);
		//System.out.println("mailBody: " + mailBody);

		Properties properties = System.getProperties();

		// Setup mail server
		properties.setProperty("mail.smtp.host",SMTPSERVER);

		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);

		if (mailTo  != null && ( mailTo.trim().length() > 0 )){
		} else {
			mailTo=ERRORRECIPIENT;
			mailBody = ERRORMESG; 
		}
		try{
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(mailFrom));
			message.addRecipient(Message.RecipientType.TO,
					new InternetAddress(mailTo));
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
