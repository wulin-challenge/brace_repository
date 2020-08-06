package cn.wulin.brace.demo.email;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class TestQQEmail {
	/* 此处使用的是使用的qq邮箱  */
	 public static void main(String[] args) throws Exception {
	        //1.
	        Message msg = null;//Message是创建和邮件的核心api，表示一封电子邮件
	        Properties prop = new Properties();
	        prop.setProperty("mail.debug","true");//调试，打印邮件发送的信息
	        prop.setProperty("mail.smtp.auth","true");
	        prop.setProperty("mail.transport.protocol","smtp");
	        Session session = Session.getInstance(prop);//session用于定义发送邮件之前所需要的环境信息，
	        //2.消息
	        msg = new MimeMessage(session);
	        msg.setSubject("Hello World wulin");//主题
	        msg.setText("Hello World，This is my first E-mail. 谢谢");//邮件正文
	        // 你的邮件地址(如：1178649872@qq.com)
	        msg.setFrom(new InternetAddress("1178649872@qq.com"));
	        //3.发送
	        Transport transport = session.getTransport();
//	        transport.connect("smtp.qq.com",465,"1178649872","cuqbrbsyrwpnifij");//使用别的邮箱服务器，需使用正确的smtp地址
	        transport.connect("smtp.qq.com",587,"1178649872","cuqbrbsyrwpnifij");//使用别的邮箱服务器，需使用正确的smtp地址
	        // 收件人的邮件地址(如：lisi@qq.com)
	        transport.sendMessage(msg, new InternetAddress[]{new InternetAddress("1178649872@qq.com")});
	        //可拼写多个收件人
	        transport.close();
	    }
}

