package cn.wulin.brace.zookeeper.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.wulin.brace.zookeeper.ZookeeperConfig;

public class TestZkConfigSpring {
	
	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring-temp1-1.xml");
		ctx.start();
		ZookeeperConfig.getInstance().connectZk();
	}

}
