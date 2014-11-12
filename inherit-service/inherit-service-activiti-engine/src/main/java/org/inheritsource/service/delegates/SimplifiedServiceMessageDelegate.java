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

		int smtpPort = 25;
		try {
			smtpPort = Integer.parseInt(SMTPportString);
		} catch (NumberFormatException e) {
			log.info("Illegal value of SMTPportString: {}", SMTPportString);
			e.printStackTrace();
		}

		String username = "";
		String password = "";

		// SSL port 465 or TLS port 587
		username = (String) props.get("mail.smtp.username");
		password = (String) props.get("mail.smtp.password");
		// String authMecanisms ="DIGEST-MD5" ;
		String from = (String) props.get("mail.text.from");
		String to = "none@nowhere.com";
		String inbox = "";
		String authMecanisms = (String) props.get("mail.smtp.authMecanisms");
		if ((authMecanisms != null) && (!(authMecanisms.equals("DIGEST-MD5")))) {
			authMecanisms = null;
		}

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
				SendMail.send(to, from, messageSubject, messageText,
						SMTPSERVER, smtpPort, authMecanisms, username, password);
			} catch (Exception e) {
				log.error(e.toString());

			}
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

	public static void main(String[] args) {
		System.out.println("Hello World!");
		// some tests to see the impact of Swedish letters
		// in string in java from the properties

		Properties props = ConfigUtil.getConfigProperties();

		// SSL : uncomment and configure
		// final String username = "username@gmail.com";
		// final String password = "password";
		// String SMTPSERVER = "smtp.gmail.com";
		// int smtpPort = 465;

		// local debug server
		final String username = "";
		final String password = "";
		String SMTPSERVER = "localhost";
		int smtpPort = 1025;

		String from = (String) props.get("mail.text.from");

		String to = "none@nowhereorsomewhere.com";

		String messageTaskSubject = (String) props
				.get("mail.text.messageTaskSubject");
		String messageSubject = (String) props.get("mail.text.messageSubject");
		String messageText = (String) props.get("mail.text.messageText");
		String messageTaskText = (String) props
				.get("mail.text.messageTaskText");
		String siteUri = (String) props.get("site.base.uri");
		String inbox = (String) props.get("site.base.public");
		String inbox2 = (String) props.get("site.base.intranet");
		String authMecanisms = "DIGEST-MD5";
		if ((siteUri != null) && (inbox != null)) {
			messageText = messageText + " " + siteUri + "/" + inbox;
		}

		if ((siteUri != null) && (inbox != null)) {
			messageTaskText = messageTaskText + " " + siteUri + "/" + inbox2;
		}

		SendMail.send(to, from, messageSubject + " / " + messageTaskSubject,
				messageText + " \n" + messageTaskText, SMTPSERVER, smtpPort,
				authMecanisms, username, password);

	}

}
