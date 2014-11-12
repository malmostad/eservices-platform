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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

		int smtpPort = 25;
		try {
			smtpPort = Integer.parseInt(SMTPportString);
		} catch (NumberFormatException e) {
			log.info("Illegal value of SMTPportString: {}", SMTPportString);
			e.printStackTrace();
		}

		String authMecanisms = (String) props.get("mail.smtp.authMecanisms");
		if ((authMecanisms != null) && (!(authMecanisms.equals("DIGEST-MD5")))) {
			authMecanisms = null;
		}

		String username = "";
		String password = "";
		// SSL port 465 or TLS port 587
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
					SendMail.send(to, from, messageSubject, messageText,
							SMTPSERVER, smtpPort, authMecanisms, username,
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
								userDirectoryEntries.addAll(userDirectoryEntry);
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
											+ userDirectoryEntries.toString());
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
						SendMail.send(to, from, messageSubject, messageText,
								SMTPSERVER, smtpPort, authMecanisms, username,
								password);

					}

				}
			}

		}

		return;
	}

	public static void main(String[] args) {
		System.out.println("Hello World!");
		// some tests to see the impact of Swedish letters
		// in string in java from the properties

		// Setup mail server

		Properties props = ConfigUtil.getConfigProperties();

		String from = (String) props.get("mail.text.from");
		String SMTPportString = (String) props.get("mail.smtp.port");
		System.out.println("SMTPportString = " + SMTPportString);

		// SSL : uncomment and configure
		// final String username = "username" ;
		// final String password = "password;
		// String SMTPSERVER = "smtpserver";
		// int smtpPort = 587;
		// local debug server
		final String username = "";
		final String password = "";
		String SMTPSERVER = "localhost";
		int smtpPort = 1025;
		String to = "none@nowhereorsomewhere.com";

		// int smtpPort = Integer.valueOf(SMTPportString);
		System.out.println(" smtpPort= " + smtpPort);

		// String username = "";
		// String password = "";
		String authMecanisms = "DIGEST-MD5";
		String messageSubject = "main subject";
		String messageText = "Message Text from main TaskMessageListener";

		SendMail.send(to, from, messageSubject, messageText, SMTPSERVER,
				smtpPort, authMecanisms, username, password);

	}
}
