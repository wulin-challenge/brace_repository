package cn.wulin.brace.zookeeper.event;

import java.nio.charset.Charset;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;

import cn.wulin.ioc.URL;
import cn.wulin.ioc.util.UrlUtils;

public class ZookeeperCuratorEventAdapter implements ZookeeperCuratorEvent{
	@Override
	public void nodeAddedEvent(CuratorFramework curatorFramework,TreeCacheEvent treeCacheEvent) {
//		URL url = parseURL(treeCacheEvent);
//		if(url != null && NewsConstants.REGISTER_PROTOCOL_KEY.equals(url.getProtocol())){
//			executeNotifyListener(url, NewsConstants.ZOOKEEPER_NODE_ADDED_EVENT);
//		}
	}

	@Override
	public void nodeUpdatedEvent(CuratorFramework curatorFramework,TreeCacheEvent treeCacheEvent) {
//		URL url = parseURL(treeCacheEvent);
//		if(url != null && NewsConstants.REGISTER_PROTOCOL_KEY.equals(url.getProtocol())){
//			executeNotifyListener(url, NewsConstants.ZOOKEEPER_NODE_UPDATED_EVENT);
//		}
	}

	@Override
	public void nodeRemovedEvent(CuratorFramework curatorFramework,TreeCacheEvent treeCacheEvent) {
//		URL url = parseURL(treeCacheEvent);
//		if(url != null && NewsConstants.REGISTER_PROTOCOL_KEY.equals(url.getProtocol())){
//			executeNotifyListener(url, NewsConstants.ZOOKEEPER_NODE_REMOVED_EVENT);
//		}
		
//		//重新发布服务
//		RegistryZkService.getInstance().republishService();
	}

	@Override
	public void connectionReconnectedEvent(CuratorFramework curatorFramework,TreeCacheEvent treeCacheEvent) {
//		URL url = parseURL(treeCacheEvent);
//		if(url != null && NewsConstants.REGISTER_PROTOCOL_KEY.equals(url.getProtocol())){
//			executeNotifyListener(url, NewsConstants.ZOOKEEPER_RECONNECTED_EVENT);
//		}
//		//重新发布服务
//		RegistryZkService.getInstance().republishService();
	}
	
	/**
	 * 解析出url
	 * @param treeCacheEvent
	 * @return
	 */
	private URL parseURL(TreeCacheEvent treeCacheEvent) {
		String urlStr = new String(treeCacheEvent.getData().getData(),Charset.defaultCharset());
		URL cancelRegistryURL = UrlUtils.parseURL(urlStr, null);
		return cancelRegistryURL;
	}
	
	/**
	 * 执行通知监听
	 * @param url
	 * @param category 执行类别
	 */
	private void executeNotifyListener(URL url,String category){
//		url = url.addParameter(NewsConstants.CATEGORY_KEY, category);
//		Set<String> notifyListenerKeys = InterfaceExtensionLoader.getExtensionLoader(NotifyListener.class).getSupportedExtensions();
//		for (String notifyListenerKey : notifyListenerKeys) {
//			NotifyListener notifyListener = InterfaceExtensionLoader.getExtensionLoader(NotifyListener.class).getExtension(notifyListenerKey);
//			notifyListener.notify(url);
//		}
	}
}
