package org.inheritsource.service.delegates;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.regex.Pattern;
import java.util.Properties;
import javax.mail.Session;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {

	public static final Logger log = LoggerFactory.getLogger(SendMail.class);

	private static final Pattern rfc2822 = Pattern
			.compile("^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$");

	protected static void send(String to, String from, String messageSubject,
			String messageText, String SMTPSERVER, int smtpPort,
			String authMecanisms, final String username, final String password) {
		log.info("to: " + to);
		// check email address
		// might like to replace this with EmailValidator from apache.commons
		if (!rfc2822.matcher(to).matches()) {
			log.error("Invalid address");
			return;
		}

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
				if (smtpPort == 587) { // TLS
					mailprops.setProperty("mail.smtp.auth", "true");
					mailprops.setProperty("mail.smtp.starttls.enable", "true");
					// mailprops.setProperty("mail.smtp.starttls.enable",
					// "false");
					mailprops.setProperty("mail.smtp.port", "587");
					if (authMecanisms != null) {
						mailprops.setProperty("mail.smtp.auth.mechanisms",
								authMecanisms);
						// mailprops.setProperty("mail.smtp.auth.mechanisms",
						// "DIGEST-MD5") ;
					}
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

}
