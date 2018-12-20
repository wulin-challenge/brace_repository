package cn.wulin.brace.zookeeper.locks;

import cn.wulin.brace.zookeeper.event.ZookeeperCuratorEvent;
import cn.wulin.ioc.logging.Logger;
import cn.wulin.ioc.logging.LoggerFactory;

/**
 * 分布式共享读写重入锁
 * 
 * @author wubo
 */
public class SharedReentrantReadWriteLock extends AbstractDistributedReadWriteLock{
	private Logger logger = LoggerFactory.getLogger(SharedReentrantReadWriteLock.class);
	
	private RefreshNotify refreshNotify;
	
	public SharedReentrantReadWriteLock(String lockRootNode) {
		super(lockRootNode);
	}
	
	public SharedReentrantReadWriteLock(String lockRootNode,RefreshNotify refreshNotify) {
		this(lockRootNode, refreshNotify, false);
	}
	
	public SharedReentrantReadWriteLock(String lockRootNode,RefreshNotify refreshNotify,boolean nativeNotify) {
		super(lockRootNode,new WriteLockListener(null,refreshNotify),nativeNotify);
		WriteLockListener listener = (WriteLockListener) event;
		listener.setDistributedReadWriteLock(this);
		this.refreshNotify = refreshNotify;
	}

	@Override
	protected ZookeeperCuratorEvent getZookeeperCuratorEvent() {
		return new WriteLockListener(this,refreshNotify);
	}

}
