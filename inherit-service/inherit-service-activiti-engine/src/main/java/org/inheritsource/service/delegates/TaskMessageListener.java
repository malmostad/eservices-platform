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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.task.IdentityLink;
import org.inheritsource.service.common.domain.UserDirectoryEntry;
import org.inheritsource.service.common.util.ConfigUtil;
import org.inheritsource.service.identity.UserDirectoryService;

public class TaskMessageListener implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1141071604517769171L;

	/**
	 * 
	 */

	public static final Logger log = LoggerFactory
			.getLogger(TaskMessageListener.class);

	public static String PROC_VAR_RECIPIENT_USER_ID = "recipientUserId";
	public static String PROC_VAR_SERVICE_DOC_URI = "serviceDocUri";

	public static String ACT_VAR_MESSAGE_TEXT = "emailMessageText";
	public static String ACT_VAR_SUBJECT = "emailSubject";

	public void notify(DelegateTask execution) {

		log.info("SimplifiedServiceMessageDelegate called from "
				+ execution.getProcessInstanceId() + " at " + new Date());

		Boolean isPublic = false; // NOTE

		Properties props = ConfigUtil.getConfigProperties();

		String mText = (String) props.get("mail.text.messageTaskText");
		if (mText == null || mText.trim().length() == 0) {
			mText = "Du har ett ärende i din inkorg ";
		}

		String messageText = "";
		messageText = mText;

		String siteUri = (String) props.get("site.base.uri");
		String SMTPSERVER = (String) props.get("mail.smtp.host");
		String SMTPportString = (String) props.get("mail.smtp.port");
		int smtpPort = Integer.valueOf(SMTPportString);

		String username = "";
		String password = "";
		// SSL port 465 or  TLS port 587 
		username = (String) props.get("mail.smtp.username");
		password = (String) props.get("mail.smtp.password");

		String from = (String) props.get("mail.text.from");
		String to = "none@nowhere.com";
		String inbox = (String) props.get("site.base.intranet");

		String taskName = execution.getName();
		String processDefinitionId = execution.getProcessDefinitionId();

		messageText = messageText + "( " + taskName + " , "
				+ processDefinitionId + ")\n";
		if ((siteUri != null) && (inbox != null)) {
			messageText = messageText + " " + siteUri + "/" + inbox;
		}

		String messageSubject = (String) props
				.get("mail.text.messageTaskSubject");
		if (messageSubject == null || messageSubject.trim().length() == 0) {
			messageSubject = "Nytt ärende";
		}

		// log.info("Email to: " + recipientUserId);
		if (isPublic) {
			//
			log.error("public not implemented ");
		} else {
			inbox = (String) props.get("site.base.intranet");
			// read info for task

			UserDirectoryService userDirectoryService = new UserDirectoryService();
			String assignee = execution.getAssignee();
			ArrayList<UserDirectoryEntry> userDirectoryEntries = new ArrayList<UserDirectoryEntry>();

			if (assignee != null) {
				// send mail to Assignee
				log.info("sending email to assignee");
				String[] userIds = { assignee };
				userDirectoryEntries.addAll(userDirectoryService
						.lookupUserEntries(userIds));
				for (UserDirectoryEntry user : userDirectoryEntries) {
					to = user.getMail();
					if (to != null) {
						sendEmail(to, from, messageSubject, messageText,
								siteUri, inbox, SMTPSERVER, smtpPort, username,
								password);

					}
				}
			} else {

				// go through list and send mail to all candidates
				Set<IdentityLink> identityLinks = execution.getCandidates();
				if (identityLinks == null) {
					log.error("no candidates found");
					return;
				} else {

					for (IdentityLink identityLink : identityLinks) {
						String groupId = identityLink.getGroupId();
						if (groupId != null) {
							log.info("looking up group " + groupId);
							try {
								ArrayList<UserDirectoryEntry> userDirectoryEntry = userDirectoryService
										.lookupUserEntriesByGroup(groupId);
								if (userDirectoryEntry != null) {
									userDirectoryEntries
											.addAll(userDirectoryEntry);
									log.info("add1 :"
											+ userDirectoryEntries.toString());
								}
							}

							catch (Exception e) {
								log.error(e.toString());
							}

						} else {
							String userId = identityLink.getUserId();
							String[] userIds = { userId };

							if (userId != null) {
								log.info("looking up userId " + userId);
								try {
									ArrayList<UserDirectoryEntry> userDirectoryEntry = userDirectoryService
											.lookupUserEntries(userIds);
									if (userDirectoryEntry != null) {
										userDirectoryEntries
												.addAll(userDirectoryEntry);
										log.info("add2 :"
												+ userDirectoryEntries
														.toString());
									}

								}

								catch (Exception e) {
									log.error(e.toString());
								}
							}
						}
					}
				}
				// make unique
				if (userDirectoryEntries.isEmpty()) {
					log.error("No candidates found ");
					return;
				} else {
					HashSet<UserDirectoryEntry> users = new HashSet<UserDirectoryEntry>();
					users.addAll(userDirectoryEntries);
					log.info("add3 :" + users.toString());
					for (UserDirectoryEntry user : users) {
						to = user.getMail();
						if (to != null) {
							sendEmail(to, from, messageSubject, messageText,
									siteUri, inbox, SMTPSERVER, smtpPort,
									username, password);

						}

					}
				}

			}

		}

		return;
	}

	private static void sendEmail(String to, String from,
			String messageSubject, String messageText, String siteUri,
			String inbox, String SMTPSERVER, int smtpPort,
			final String username, final String password) {
		log.info("to: " + to);
		// check email address
		// might like to replace this with EmailValidator from apache.commons
		if (!rfc2822.matcher(to).matches()) {
			log.error("Invalid address");
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
		mailprops.setProperty("mail.smtp.port", Integer.toString(smtpPort));

		try {
			Session session = null;
			if (smtpPort == 465) {
				// SSL
				mailprops.setProperty("mail.smtp.socketFactory.port", "465");
				mailprops.setProperty("mail.smtp.socketFactory.class",
						"javax.net.ssl.SSLSocketFactory");
				mailprops.setProperty("mail.smtp.auth", "true");

				session = Session.getInstance(mailprops,
						new javax.mail.Authenticator() {
							protected PasswordAuthentication getPasswordAuthentication() {
								return new PasswordAuthentication(username,
										password);
							}
						});
			} else {
				if (smtpPort == 587) {  // TLS 
					mailprops.setProperty("mail.smtp.auth", "true");
					mailprops.setProperty("mail.smtp.starttls.enable", "true");
					mailprops.setProperty("mail.smtp.port", "587");
					session = Session.getInstance(mailprops,
							new javax.mail.Authenticator() {
								protected PasswordAuthentication getPasswordAuthentication() {
									return new PasswordAuthentication(username,
											password);
								}
							});
				} else {

					session = Session.getInstance(mailprops);
				}
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

	private static final Pattern rfc2822 = Pattern
			.compile("^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$");

	public static void main(String[] args) {
		System.out.println("Hello World!");
		// some tests to see the impact of Swedish letters
		// in string in java from the properties

		// Setup mail server
		Properties mailprops = new Properties();

		Properties props = ConfigUtil.getConfigProperties();

		String from = (String) props.get("mail.text.from");
		String SMTPportString = (String) props.get("mail.smtp.port");
		System.out.println("SMTPportString = " + SMTPportString);

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
		String to = "none@nowhere.com";

		// int smtpPort = Integer.valueOf(SMTPportString);
		System.out.println(" smtpPort= " + smtpPort);

		// String username = "";
		// String password = "";

		String messageSubject = "main subject";
		String messageText = "Message Text from main TaskMessageListener";

		sendEmail(to, from, messageSubject, messageText, "", "", SMTPSERVER,
				smtpPort, username, password);

	}
}
