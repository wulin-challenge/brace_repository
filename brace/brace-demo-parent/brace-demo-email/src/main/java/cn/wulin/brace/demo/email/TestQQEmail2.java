//package cn.wulin.brace.demo.email;
//
//public class TestQQEmail2 {
//	import java.util.Properties;
//	
//	public static void SendEmailByQQ(){
//	        String userName = "平时QQ邮箱登录的账号";
//	        String password = "获取到第三方登录的密码";
//	        String from = "发送邮件的账号";
//	        String to = "接收邮件的账号";
//	        
//	        String ssl = "javax.net.ssl.SSLSocketFactory";
//	        //创建一个发送者对象
//	        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//	        mailSender.setHost("smtp.qq.com");
//	        mailSender.setUsername(userName);
//	        mailSender.setPassword(password);
//	
//	//        //加认证机制
//	        Properties properties = new Properties();
//	        properties.setProperty("mail.transport.protocol", "smtp");
//	        properties.setProperty("mail.smtp.auth", "true");//开启认证
//	        properties.setProperty("mail.debug", "true");//启用调试
//	
//	//## 加了以下的参数后就会报错 不知道上面原因，注释掉就好了。有知道的大佬麻烦评论告知以下 谢谢
//	
//	//        properties.setProperty("mail.smtp.timeout", "1000");//设置链接超时
//	//        properties.setProperty("mail.smtp.port", "465");//设置端口
//	//        properties.setProperty("mail.smtp.socketFactory.port", "465");//设置ssl端口
//	//        properties.setProperty("mail.smtp.socketFactory.fallback", "false");
//	//        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//	        mailSender.setJavaMailProperties(properties);
//	
//	        //组织邮件参数并执行发送
//	        SimpleMailMessage message = new SimpleMailMessage();
//	        message.setFrom(from);
//	        message.setTo(to);
//	        message.setSubject("邮件标题");
//	        message.setText("邮件内容");
//	        mailSender.send(message);
//	    }
//}
//
