package cn.wulin.brace.zookeeper.test.listener.lock2;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ListenerClientStarter {
	
	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring-temp1-1.xml");
		ctx.start();
		
		ListenerClient1 client1 = new ListenerClient1();
		ListenerClient2 client2 = new ListenerClient2();
		
		client1.multiThread();
		client2.multiThread();
	}

}
