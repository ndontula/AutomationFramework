package com.automation.utilities;

import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import com.automation.actions.ActionEngine;

/**
 * This class provides Utility Helper functions for the Logging Module
 *
 * @author Narasimha Dontula
 * @since 20/06/2020.
 */
public class ReadMail extends ActionEngine {
	Store store=null;
	Folder inbox=null;
	
	public void gmailLogin() throws Throwable {
		String gmailId = ConfigManager.getProperty("gmailId").trim();
		String gmailPassword = ConfigManager.getProperty("gmailPassword").trim();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		Properties props = new Properties();
		try {
			props.put("mail.imap.ssl.enable", "true");
			props.put("mail.imap.sasl.enable", "true");
			props.put("mail.imap.sasl.mechanisms", "XOAUTH2");
			props.put("mail.imap.auth.login.disable", "true");
			props.put("mail.imap.auth.plain.disable", "true");
			Session session = Session.getInstance(props);
			store = session.getStore("imaps");
			store.connect("smtp.gmail.com", gmailId, gmailPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Message[] sortMessagesFromRecentToOldest() throws Throwable {
		Message[] messages = null;
		try {
			inbox = store.getFolder("inbox");
			inbox.open(Folder.READ_WRITE);
			// Fetch unseen messages from inbox folder
			messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
			// Sort messages from recent to oldest
			Arrays.sort(messages, (m1, m2) -> {
				try {
					return m2.getSentDate().compareTo(m1.getSentDate());
				} catch (MessagingException e) {
					throw new RuntimeException(e);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return messages;
	}
	
	public String getTextFromMessage(Message message) throws Throwable  {
		String result = "";
		if (message.isMimeType("text/plain")) {
			result = message.getContent().toString();
		} else if (message.isMimeType("multipart/*")) {
			MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
			result = getTextFromMimeMultipart(mimeMultipart);
		}
		return result;
	}

	public String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws Throwable  {

		int count = mimeMultipart.getCount();
		if (count == 0)
			throw new MessagingException("Multipart with no body parts not supported.");
		boolean multipartAlt = new ContentType(mimeMultipart.getContentType()).match("multipart/alternative");
		// alternatives appear in an order of increasing
		// faithfulness to the original content. Customize as req'd.
		if (multipartAlt){
			return getTextFromBodyPart(mimeMultipart.getBodyPart(count - 1));
		}
		String result = "";
		for (int i = 0; i < count; i++) {
			BodyPart bodyPart = mimeMultipart.getBodyPart(i);
			result += getTextFromBodyPart(bodyPart);
		}
		return result;
	}

	public String getTextFromBodyPart(BodyPart bodyPart) throws Throwable  {
		String result = "";
		if (bodyPart.isMimeType("text/plain")) {
			result = (String) bodyPart.getContent();
		} else if (bodyPart.isMimeType("text/html")) {
			String html = (String) bodyPart.getContent();
			result = org.jsoup.Jsoup.parse(html).text();
		} else if (bodyPart.getContent() instanceof MimeMultipart) {
			result = getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
		}
		return result;
	}
	
	public String readInvitationMail(String subject) throws Throwable {
		gmailLogin();
		return getMostRecentMail(subject);
	}

	public String getMostRecentMail(String subject) throws Throwable {
		String mailMessage = "";
		int counter = 0;
		try {
			Message[] messages = sortMessagesFromRecentToOldest();
			while (messages.length > 0 && subject.equals(messages[0].getSubject()) && counter < 5) {
				messages = sortMessagesFromRecentToOldest();
				Thread.sleep(1000L);
				counter++;
			}
			if (messages.length > 0) {
				mailMessage = getTextFromMessage(messages[0]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mailMessage;
	}

	public String getURLFromGmail(String subject, String urlContainsText) throws Throwable {
		String mailMessage = "", passwordResetULR = "";
		try {
			gmailLogin();
			mailMessage = getMostRecentMail(subject);
			if (!"".equals(mailMessage)) {
				passwordResetULR = getURL(mailMessage, urlContainsText);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			inbox.close(true);
			store.close();
		}
		return passwordResetULR;
	}

	// This method accept mail body message as a string and partial URL as arguments and returns the user portal URL from the mail body message. 
	private String getURL(String mailBodyMessage, String urlContainsText) throws Throwable {
		String url = "";
		Scanner scanner = null;
		try{
			scanner = new Scanner(mailBodyMessage);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				// process the line
				if (line != null && !"".equals(line.trim())) {
					if (line.contains(urlContainsText)) {
						url = line;
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			scanner.close();
		}
		return url;
	}
}