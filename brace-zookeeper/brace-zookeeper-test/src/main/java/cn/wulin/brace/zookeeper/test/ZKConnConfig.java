package cn.wulin.brace.zookeeper.test;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import cn.wulin.brace.zookeeper.ZkConnection;
import cn.wulin.ioc.URL;
import cn.wulin.ioc.spring.extension.SpringExtensionFactory;

public class ZKConnConfig implements ZkConnection,ApplicationContextAware{

	@Override
	public String conn(URL url) {
		return "zxq:2181";
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringExtensionFactory.addApplicationContext(applicationContext);
	}

}
