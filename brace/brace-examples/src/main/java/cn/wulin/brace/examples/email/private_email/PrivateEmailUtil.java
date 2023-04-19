package cn.wulin.brace.examples.email.private_email;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrivateEmailUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(PrivateEmailUtil.class);
	
	private static final ConcurrentMap<String, Session> SESSIONS = new ConcurrentHashMap<>();
	
	public static Boolean send(String receiverAddress, String title, String content) {

		String senderAddress = "linlin@haohaoxiaohua.com";
		String password = "linlin";
		String sendHost = "smtp.haohaoxiaohua.com";
		int port = 465;

		try {
			Session session = createSession(senderAddress, password, sendHost, port);
			// 创建邮件信息
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(senderAddress));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverAddress));
			message.setSubject(title);
			
			message.setContent(content, "text/html;charset=utf-8");
			//message.setText(newContent);

			// 发送邮件
			Transport.send(message);
		} catch (Exception e) {
			LOGGER.error("邮件发送失败", e);
			return false;
		}
		return true;
	}
	
	/**
	 * 创建session
	 * @param sender
	 * @param receiver
	 * @return
	 */
	private static Session createSession(String senderAddress,String password,String sendHost,int port) {
		Session session = SESSIONS.get(senderAddress);
		if(session != null) {
			return session;
		}

		Properties props = new Properties();
		props.put("mail.smtp.host", sendHost);
		props.put("mail.smtp.auth", "true");

		if (port == 465) {
			props.put("mail.smtps.port", "465");
		} else if (port == 587) {
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.port", "587");
		}

		// 创建一个邮件会话
		session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(senderAddress, password);
			}
		});
		
		SESSIONS.put(senderAddress, session);
		return session;
	}

}
