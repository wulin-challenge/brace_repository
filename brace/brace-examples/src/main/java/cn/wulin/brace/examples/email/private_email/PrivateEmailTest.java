package cn.wulin.brace.examples.email.private_email;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

/**
 * 用于测试私有邮箱
 * @author wulin
 *
 */
public class PrivateEmailTest {
	private static final ExecutorService EXECUTORS = Executors.newFixedThreadPool(8);
	
	public void testSimpleEmail(int i) {
		long start = System.currentTimeMillis();
//		Boolean send = PrivateEmailUtil.send("linlin@haohaoxiaohua.com", "测试邮件", comtext());
		Boolean send = PrivateEmailUtil.send("tolin@haohaoxiaohua.com", "测试邮件", comtext());
//		Boolean send = PrivateEmailUtil.send("1178649872@qq.com", "测试邮件", comtext());
		
		long end = System.currentTimeMillis();
		if(send) {
			System.out.println("ok--"+i+"--"+(end-start));
		}else {
			System.out.println("fail--"+i+"-----------"+(end-start));
		}
		
	}
	
	@Test
	public void testMultiEmail() {
		
		long start = System.currentTimeMillis();
		int total = 10000;
		for (int i = 0; i < total; i++) {
			final int j = i;
			EXECUTORS.submit(()->{
				try {
					
					testSimpleEmail(j);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if(j>(total-20)) {
					long end = System.currentTimeMillis();
					System.out.println("总时间--"+j+"----------------------"+(end-start));
				}
			});
			
		}
		
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String comtext() {
		String cnt = "<table align=\"center\" width=\"600\" cellpadding=\"15\" cellspacing=\"0\" border=\"0\" bgcolor=\"#f8f9fa\">\r\n" + 
				"        <tr>\r\n" + 
				"            <td bgcolor=\"white\" align=\"center\">\r\n" + 
				"                <h3>【新手必读】完整ChatGPT注册指南 - 轻松创建您的账户，开启智能助手新篇章！</h3>\r\n" + 
				"            </td>\r\n" + 
				"        </tr>\r\n" + 
				"        <tr>\r\n" + 
				"            <td bgcolor=\"white\" align=\"justify\">\r\n" + 
				"                <p>您是否曾想过拥有一位智能助手，随时为您提供高质量的写作、编辑、答疑解惑和实用建议服务？我们很高兴地告诉您，通过ChatGPT，这一切都可以实现！</p>\r\n" + 
				"                <p>ChatGPT是OpenAI开发的一款强大的人工智能语言模型，具备出色的文本生成和理解能力。为了让您轻松加入我们的用户社群，我们精心为您准备了一份详尽的ChatGPT注册指南！</p>\r\n" + 
				"            </td>\r\n" + 
				"        </tr>\r\n" + 
				"        <tr>\r\n" + 
				"            <td bgcolor=\"white\" align=\"center\">\r\n" + 
				"                <a href=\"http://www.notescloud.top/cloudSearch/detail/6622\" style=\"display: inline-block; padding: 10px 20px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px;width:100%;\" rel=\"noopener noreferrer\" target=\"_blank\">立刻查看学习</a>\r\n" + 
				"            </td>\r\n" + 
				"        </tr>\r\n" + 
				"        <tr>\r\n" + 
				"            <td bgcolor=\"white\" align=\"center\">\r\n" + 
				"                <p style=\"font-size: 12px;\"><a href=\"https://www.haohaoxiaohua.com/sl/eact/${param}/npu/S\" style=\"color: #888; text-decoration: none;\" rel=\"noopener noreferrer\" target=\"_blank\">如果您不想再接收此类通知邮件，请点击此处取消订阅</a></p>\r\n" + 
				"            </td>\r\n" + 
				"        </tr>\r\n" + 
				"    </table>";
		
		return cnt;
	}

}
