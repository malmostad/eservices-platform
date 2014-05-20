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
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.mail.Message;

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

	public static final Logger log = Logger
			.getLogger(SimplifiedServiceMessageDelegate.class.getName());


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
			log.severe("Invalid use of SimplifiedServiceMessageDelegate, the task local variable "
					+ PROC_VAR_RECIPIENT_USER_ID
					+ "is expected to have a value");
		}

		Properties props = ConfigUtil.getConfigProperties();

		String messageText = (String) props.get("mail.text.messageText");
		String siteUri = (String) props.get("site.base.uri");
		String SMTPSERVER = (String) props.get("mail.smtp.host");
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

		log.info("Email to: " + recipientUserId);
		if (isPublic) {
			inbox = (String) props.get("site.base.public");
			// read from configuration
			if (service == null) {
				log.severe("failed to get service, unable to determine emailadress ");
				return;
			} else {
				try {
					to = service.getMyProfile(recipientUserId).getEmail();
					sendEmail(to, from, messageSubject, messageText, siteUri,
							inbox, SMTPSERVER);
				} catch (Exception e) {
					log.severe(e.toString());

				}
			}

		} else {
			inbox = (String) props.get("site.base.intranet");
			// read from ldap

			log.severe("ldap connection not implemented, unable to determine emailadress ");

			// send email in separate method

		}

		return;
	}

	private void sendEmail(String to, String from, String messageSubject,
			String messageText, String siteUri, String inbox, String SMTPSERVER) {
		log.info("to: " + to);
		// check email address
		// might like to replace this with EmailValidator from apache.commons
		if (!rfc2822.matcher(to).matches()) {
			log.severe("Invalid address");
			return;
		}

		log.info("siteUri:" + siteUri);
		log.info("inbox:" + inbox);
		log.info("SMTPSERVER:" + SMTPSERVER);

		log.info("Email subject: " + messageSubject);
		log.info("Email text: " + messageText);

		// Setup mail server
		Properties mailprops = new Properties();
		mailprops.setProperty("mail.smtp.host", SMTPSERVER);

		try {

			Session session = Session.getInstance(mailprops);
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

}
