package cn.wulin.brace.zookeeper.test.lock;

import java.util.HashMap;
import java.util.Map;

import cn.wulin.brace.zookeeper.ZkConnection;
import cn.wulin.brace.zookeeper.ZookeeperConfig;
import cn.wulin.ioc.URL;

public class ZKConfiguration {
	
	public void init(){
		Map<String,String> params = new HashMap<String,String>();
		params.put(ZkConnection.ZK_CONN, "spring");
		params.put(ZkConnection.ZK_CONN_SPRING_BEAN, "zkConnSpringBean");
		URL url = new URL("zk","127.0.0.1",2181,params);
		ZookeeperConfig.getInstance().connectZk(url);
	}

}
