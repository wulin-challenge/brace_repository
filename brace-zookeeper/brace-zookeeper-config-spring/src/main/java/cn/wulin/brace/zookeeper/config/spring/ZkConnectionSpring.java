package cn.wulin.brace.zookeeper.config.spring;

import cn.wulin.brace.zookeeper.ZkConnection;
import cn.wulin.ioc.URL;
import cn.wulin.ioc.extension.ExtensionFactory;
import cn.wulin.ioc.extension.InterfaceExtensionLoader;

public class ZkConnectionSpring implements ZkConnection{

	@Override
	public String conn(URL url) {
		ExtensionFactory extension = InterfaceExtensionLoader.getExtensionLoader(ExtensionFactory.class).getExtension(ZkConnection.SPRING_EXTENSION_FACTORY);
		ZkConnection zkConn = extension.getExtension(ZkConnection.class, url.getParameter(ZkConnection.ZK_CONN_SPRING_BEAN));
		return zkConn.conn(url);
	}

}
