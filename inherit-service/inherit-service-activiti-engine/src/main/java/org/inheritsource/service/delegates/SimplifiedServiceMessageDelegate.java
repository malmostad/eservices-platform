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

package org.inheritsource.service.delegates;

import java.util.Date;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.delegate.DelegateExecution;
import org.inheritsource.service.common.util.ConfigUtil;
import org.inheritsource.taskform.engine.TaskFormService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SimplifiedServiceMessageDelegate implements JavaDelegate,
		ApplicationContextAware {

	public static final Logger log = LoggerFactory
			.getLogger(SimplifiedServiceMessageDelegate.class);

	public static String PROC_VAR_RECIPIENT_USER_ID = "recipientUserId";
	public static String PROC_VAR_SERVICE_DOC_URI = "serviceDocUri";

	public static String ACT_VAR_MESSAGE_TEXT = "emailMessageText";
	public static String ACT_VAR_SUBJECT = "emailSubject";

	public void execute(DelegateExecution execution) throws Exception {
		TaskFormService service = (TaskFormService) context.getBean("engine");

		log.info("SimplifiedServiceMessageDelegate called from "
				+ execution.getCurrentActivityName() + " in process "
				+ execution.getProcessInstanceId() + " at " + new Date());

		Boolean isPublic = true; // NOTE

		String recipientUserId = (String) execution.getEngineServices()
				.getRuntimeService()
				.getVariable(execution.getId(), PROC_VAR_RECIPIENT_USER_ID);
		if (recipientUserId == null || recipientUserId.trim().length() == 0) {
			log.error("Invalid use of SimplifiedServiceMessageDelegate, the task local variable "
					+ PROC_VAR_RECIPIENT_USER_ID
					+ "is expected to have a value");
		}

		Properties props = ConfigUtil.getConfigProperties();

		String messageText = (String) props.get("mail.text.messageText");
		String siteUri = (String) props.get("site.base.uri");
		String SMTPSERVER = (String) props.get("mail.smtp.host");
		String SMTPportString = (String) props.get("mail.smtp.port");

		int smtpPort = Integer.valueOf(SMTPportString); 
		String	username = "";  String	password = "" ; 
		
			// SSL port 465 
		if (smtpPort == 465) { 
		username = (String) props.get("mail.smtp.username");
		password = (String) props.get("mail.smtp.password");
		}
	
		
		String from = (String) props.get("mail.text.from");
		String to = "none@nowhere.com";
		String inbox = "";

		if (messageText == null || messageText.trim().length() == 0) {
			messageText = "Du har ett beslut i din inkorg ";
		}

		if ((siteUri != null) && (inbox != null)) {
			messageText = messageText + " " + siteUri + "/" + inbox;
		}

		String messageSubject = (String) props.get("mail.text.messageSubject");
		if (messageSubject == null || messageSubject.trim().length() == 0) {
			messageSubject = "Delgivning";
		}

		log.info("Email to: {}", recipientUserId);
		if (isPublic) {
			inbox = (String) props.get("site.base.public");
			// read from configuration
			log.info("siteUri:{}", siteUri);
			log.info("inbox:{}", inbox);

			if (service == null) {
				log.error("failed to get service, unable to determine emailadress ");
				return;
			} else {
				try {
					to = service.getMyProfile(recipientUserId).getEmail();
					sendEmail(to, from, messageSubject, messageText,
							SMTPSERVER, smtpPort, 	username , 	password );
				} catch (Exception e) {
					log.error(e.toString());

				}
			}

		} else {
			// inbox = (String) props.get("site.base.intranet");
			// read from ldap

			log.error("ldap connection not implemented, unable to determine emailadress ");

			// send email in separate method

		}

		return;
	}

	private static void sendEmail(String to, String from,
			String messageSubject, String messageText, String SMTPSERVER,
			int smtpPort,final String username , final String password ) {
		log.info("to: {}", to);
		// check email address
		// might like to replace this with EmailValidator from apache.commons
		if (!rfc2822.matcher(to).matches()) {
			log.error("Invalid address");
			return;
		}

		log.info("SMTPSERVER:{}", SMTPSERVER);

		log.info("Email subject: {}", messageSubject);
		log.info("Email text: {}", messageText);

		// Setup mail server
		Properties mailprops = new Properties();
		mailprops.setProperty("mail.smtp.host", SMTPSERVER);
		mailprops.setProperty("mail.smtp.port", Integer.toString(smtpPort));
		// mailprops.setProperty( , value) ;
		try {
			Session session = null;
			if (smtpPort == 465) {
				// SSL 
				mailprops.setProperty("mail.smtp.socketFactory.port", "465");
				mailprops.setProperty("mail.smtp.socketFactory.class",
						"javax.net.ssl.SSLSocketFactory");
				mailprops.setProperty("mail.smtp.auth", "true");
	
				session = Session.getInstance(mailprops	,		new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username,password);
					}
				});
			} else {
				session = Session.getInstance(mailprops);
			}

			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from.replaceAll("\\+", " ")));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));

			message.setSubject(messageSubject.replaceAll("\\+", " "));
			message.setText(messageText.replaceAll("\\+", " "));

			// Send message
			log.info("EmailToInitiator: Sending message to " + to
					+ " via smtpserver: " + SMTPSERVER);
			log.info((String) message.getContent());
			Transport.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;

	}

	// work around app context

	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		context = arg0;

	}

	private static ApplicationContext context;

	private static final Pattern rfc2822 = Pattern
			.compile("^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$");

	public static void main(String[] args) {
		System.out.println("Hello World!");
		// some tests to see the impact of Swedish letters
		// in string in java from the properties






		Properties props = ConfigUtil.getConfigProperties();

//  SSL  : uncomment and configure
//		final String username = "username@gmail.com";
//		final String password = "password";		
//		String SMTPSERVER = "smtp.gmail.com";
//		int smtpPort = 465;
	
	// local debug server	
		final String username = "";
		final String password = "";		
		String SMTPSERVER = "localhost";
		int smtpPort = 1025;
		
		
		
		String from = (String) props.get("mail.text.from");

		String to = "none@nowhere.com";

		String messageTaskSubject = (String) props
				.get("mail.text.messageTaskSubject");
		String messageSubject = (String) props.get("mail.text.messageSubject");
		String messageText = (String) props.get("mail.text.messageText");
		String messageTaskText = (String) props
				.get("mail.text.messageTaskText");
		String siteUri = (String) props.get("site.base.uri");
		String inbox = (String) props.get("site.base.public");
		String inbox2 = (String) props.get("site.base.intranet");

		if ((siteUri != null) && (inbox != null)) {
			messageText = messageText + " " + siteUri + "/" + inbox;
		}

		if ((siteUri != null) && (inbox != null)) {
			messageTaskText = messageTaskText + " " + siteUri + "/" + inbox2;
		}

		sendEmail(to, from, messageSubject + " / " + messageTaskSubject,
				messageText + " \n" + messageTaskText, SMTPSERVER, smtpPort, username, 	password);

	}

}
