package cn.wulin.brace.zookeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import cn.wulin.brace.zookeeper.event.ZookeeperCuratorEvent;
import cn.wulin.brace.zookeeper.event.ZookeeperCuratorEventAdapter;
import cn.wulin.brace.zookeeper.event.ZookeeperCuratorListener;
import cn.wulin.ioc.URL;
import cn.wulin.ioc.extension.InterfaceExtensionLoader;
import cn.wulin.ioc.logging.Logger;
import cn.wulin.ioc.logging.LoggerFactory;

/**
 * zookeeper的核心配置类
 * @author wubo
 *
 */
public class ZookeeperConfig {
	private Logger logger = LoggerFactory.getLogger(ZookeeperConfig.class);
	private static ZookeeperConfig zookeeperConfig;
	private static CuratorFramework curatorFramework;
	
	/**
	 * 得到连接配置信息
	 */
	private ZkConnection zkConn = InterfaceExtensionLoader.getExtensionLoader(ZkConnection.class).getAdaptiveExtension();
	
	private ZookeeperConfig(){
		
	}
	
	/**
	 * 连接zookeeper,默认采用spring的方式进行加载
	 */
	public void connectZk(){
		Map<String,String> params = new HashMap<String,String>();
		params.put(ZkConnection.ZK_CONN, "spring");
		params.put(ZkConnection.ZK_CONN_SPRING_BEAN, "zkConnSpringBean");
		URL url = new URL("zk","127.0.0.1",2181,params);
		connectZk(url);
	}
	
	/**
	 * 连接zookeeper
	 */
	public void connectZk(URL url,String rootNode){
		try {
			connectZookeeperCurator(url);
			createRootNode(rootNode);
		} catch (IOException e){
			logger.error("zookeeper连接失败!请检测zookeeper地址",e);
		}
	}
	
	/**
	 * 连接zookeeper
	 */
	public void connectZk(URL url){
		try {
			connectZookeeperCurator(url);
		} catch (IOException e){
			logger.error("zookeeper连接失败!请检测zookeeper地址",e);
		}
	}
	
	public void createRootNode(String rootNode){
		createNode(rootNode, CreateMode.PERSISTENT);//创建根节点
		setNodeListener(rootNode);
	}
	
	
	
	/**
	 * 判断节点是否存在,存在就返回true,否则返回false
	 * @param path
	 * @return
	 */
	public Boolean existsNode(String path){
		try {
			if(curatorFramework.checkExists().forPath(path) == null){
				return false;
			}
		} catch (Exception e) {
			logger.error(path+" 节点创建失败!", e);
		}
		return true;
	}
	
	/**
	 * 创建节点:这里的节点数据为空
	 * @param node 节点
	 * @param createMode 节点模式
	 */
	public String createNode(String path,CreateMode createMode){
		try {
			if(curatorFramework.checkExists().forPath(path) == null){
				return curatorFramework.create().creatingParentsIfNeeded().withMode(createMode).forPath(path);
			}
		} catch (Exception e) {
			logger.error(path+" 节点创建失败!", e);
		}
		return null;
	}
	
	/**
	 * 设置节点数据
	 * @param path
	 * @param data
	 */
	public void setNodeData(String path,byte[] data){
		try {
			if(curatorFramework.checkExists().forPath(path) != null){
				curatorFramework.setData().forPath(path, data);
			}
		} catch (Exception e) {
			logger.error(path+" 设置节点数据失败!", e);
		}
	}
	
	/**
	 * 设置节点监听
	 * @param path
	 */
	@SuppressWarnings("resource")
	public void setNodeListener(String path){
		TreeCache treeCache = new TreeCache(curatorFramework, path);
		
		ZookeeperCuratorEvent zookeeperCuratorEvent = new ZookeeperCuratorEventAdapter();
		TreeCacheListener treeCacheListener = new ZookeeperCuratorListener(zookeeperCuratorEvent);
		treeCache.getListenable().addListener(treeCacheListener);
		try {
			treeCache.start();
		} catch (Exception e) {
			logger.error(path+" 节点监听启动失败!", e);
		}
	}
	
	/**
	 * 获取当前节点下面的孩子节点
	 * @param path 
	 * @return
	 */
	public List<String> getChildrens(String path){
		try {
			if(curatorFramework.checkExists().forPath(path) != null){
				return curatorFramework.getChildren().forPath(path);
			}
		} catch (Exception e) {
			logger.error(path+" 获取孩子节点失败!", e);
		}
		return null;
	}
	
	/**
	 * 得到某个节点下孩子的全路径
	 * @param path
	 * @return
	 */
	public List<String> getChildrenFullPathList(String path){
		try {
			if(curatorFramework.checkExists().forPath(path) != null){
				List<String> fullPathList = new ArrayList<>();
				List<String> forPath = curatorFramework.getChildren().forPath(path);
				for (String childrenPath : forPath) {
					fullPathList.add(path+"/"+childrenPath);
				}
				return fullPathList;
			}
		} catch (Exception e) {
			logger.error(path+" 获取孩子节点失败!", e);
		}
		return null;
	}
	
	/**
	 * 获取节点数据
	 * @param path
	 * @return
	 */
	public byte[] getNodeData(String path){
		try {
			if(curatorFramework.checkExists().forPath(path) != null){
				byte[] nodeData = curatorFramework.getData().forPath(path);
				return nodeData;
			}
		} catch (Exception e) {
			logger.error(path+" 获取节点数据失败!", e);
		}
		return null;
	}
	
	/**
	 * 删除节点
	 * @param path
	 */
	public void removeNode(String path){
		try {
			if(curatorFramework.checkExists().forPath(path) != null){
				curatorFramework.delete().guaranteed().deletingChildrenIfNeeded().forPath(path);
			}
		} catch (Exception e) {
			logger.error(path+" 删除节点失败!", e);
		}
	}
	
	/**
	 * 连接zookeeper
	 * @throws IOException
	 */
	private void connectZookeeperCurator(URL url) throws IOException{
		String zkAddress = zkConn.conn(url);
		// 重试策略
        // 初始休眠时间为 1000ms, 最大重试次数为 3
		RetryPolicy retry = new ExponentialBackoffRetry(1000, 3);
        // 创建一个客户端, 60000(ms)为 session 超时时间, 15000(ms)为链接超时时间
		curatorFramework = CuratorFrameworkFactory.newClient(zkAddress, 60000, 15000, retry);
		curatorFramework.start();
	}
	
	public CuratorFramework getCuratorFramework(){
		return curatorFramework;
	}

	public static ZookeeperConfig getInstance(){
		if(zookeeperConfig == null){
			zookeeperConfig = new ZookeeperConfig();
		}
		return zookeeperConfig;
	}
	
}
