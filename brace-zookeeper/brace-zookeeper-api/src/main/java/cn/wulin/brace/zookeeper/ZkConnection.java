package cn.wulin.brace.zookeeper;

import cn.wulin.ioc.URL;
import cn.wulin.ioc.extension.Adaptive;
import cn.wulin.ioc.extension.SPI;

@SPI
public interface ZkConnection {
	
	/**
	 * zk连接的url参数
	 */
	public static final String ZK_CONN = "zk_conn";
	
	/**
	 * spring的bean名称的参数
	 */
	public static final String ZK_CONN_SPRING_BEAN = "zk_conn_spring_bean";
	
	/**
	 * spring的扩展工厂
	 */
	public static final String SPRING_EXTENSION_FACTORY = "spring";
	/**
	 * 获取zk连接
	 * @return
	 */
	@Adaptive(ZK_CONN)
	String conn(URL url);

}
