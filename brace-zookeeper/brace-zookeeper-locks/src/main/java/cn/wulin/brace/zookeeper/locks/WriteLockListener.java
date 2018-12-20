package cn.wulin.brace.zookeeper.locks;

import java.nio.charset.Charset;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;

import cn.wulin.brace.zookeeper.event.ZookeeperCuratorEvent;

/**
 * 写锁监听器
 * @author wubo
 *
 */
public class WriteLockListener implements ZookeeperCuratorEvent{
	
	private AbstractDistributedReadWriteLock distributedReadWriteLock;
	
	private RefreshNotify refreshNotify;
	
	public WriteLockListener(AbstractDistributedReadWriteLock distributedReadWriteLock,RefreshNotify refreshNotify) {
		super();
		this.distributedReadWriteLock = distributedReadWriteLock;
		this.refreshNotify = refreshNotify;
	}
	

	@Override
	public void nodeAddedEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) {
		notify(treeCacheEvent);
	}

	@Override
	public void nodeUpdatedEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) {
		notify(treeCacheEvent);
	}

	/**
	 * 得到锁节点数据
	 * @param treeCacheEvent
	 * @return
	 */
	private long getLockDataNodeData(TreeCacheEvent treeCacheEvent){
		if(treeCacheEvent != null && treeCacheEvent.getData() != null){
			byte[] nodeData = treeCacheEvent.getData().getData();
			if(nodeData != null){
				Long data = Long.parseLong(new String(nodeData,Charset.defaultCharset()));
				return data;
			}
		}
		return -1;
	}

	@Override
	public void nodeRemovedEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) {
		notify(treeCacheEvent);
	}

	@Override
	public void connectionReconnectedEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) {
		notify(treeCacheEvent);
	}

	public AbstractDistributedReadWriteLock getDistributedReadWriteLock() {
		return distributedReadWriteLock;
	}

	public void setDistributedReadWriteLock(AbstractDistributedReadWriteLock distributedReadWriteLock) {
		this.distributedReadWriteLock = distributedReadWriteLock;
	}

	public RefreshNotify getRefreshNotify() {
		return refreshNotify;
	}

	public void setRefreshNotify(RefreshNotify refreshNotify) {
		this.refreshNotify = refreshNotify;
	}
	
	private void notify(TreeCacheEvent treeCacheEvent) {
		boolean nativeNotify = distributedReadWriteLock.getNativeNotify();
		long localVersion = distributedReadWriteLock.getLocalLockVersion().get();
		long lockDataNodeData = getLockDataNodeData(treeCacheEvent);
		if(refreshNotify != null){
			//若本地通知为true,则不管什么情况都进行刷新
			if(nativeNotify){
				refreshNotify.doRefreshNotify();
			}else{
				//若本地通知为false,则只有当本地锁版本与服务器端的锁的版本号不一致时才进行刷新
				if(localVersion != lockDataNodeData){
					refreshNotify.doRefreshNotify();
				}
			}
		}
	}
}
