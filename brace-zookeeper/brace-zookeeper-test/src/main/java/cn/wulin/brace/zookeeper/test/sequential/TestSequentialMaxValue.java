package cn.wulin.brace.zookeeper.test.sequential;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.zookeeper.CreateMode;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.wulin.brace.zookeeper.ZookeeperConfig;

public class TestSequentialMaxValue {
	
	private static final String root = "/max-number";
	
	private static final ExecutorService es = Executors.newFixedThreadPool(2);
	
	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring-temp1-1.xml");
		ctx.start();
		ZookeeperConfig instance = ZookeeperConfig.getInstance();
		instance.removeNode(root);
		instance.createNode(root, CreateMode.PERSISTENT);//创建根节点
		
		final long max = 99999999999l;
		for (long i = 0; i < max; i++) {
			final long j = i;
			es.submit(new Runnable(){
				@Override
				public void run() {
					String path = root+"/"+j+"_###";
					path = instance.createNode(path, CreateMode.PERSISTENT_SEQUENTIAL);//创建根节点
					if(j != max-1){
						instance.removeNode(path);
					}
					System.out.println(j);
				}
			});
		}
	}

}
