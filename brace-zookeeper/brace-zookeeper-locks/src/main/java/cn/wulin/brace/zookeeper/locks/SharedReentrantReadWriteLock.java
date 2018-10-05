package cn.wulin.brace.zookeeper.locks;

import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;

import cn.wulin.brace.zookeeper.ZookeeperConfig;
import cn.wulin.ioc.logging.Logger;
import cn.wulin.ioc.logging.LoggerFactory;

/**
 * 分布式共享读写重入锁
 * 
 * @author wubo
 */
public class SharedReentrantReadWriteLock {

	private Logger logger = LoggerFactory.getLogger(SharedReentrantReadWriteLock.class);
	//分布式锁
	private InterProcessReadWriteLock lock;
	private ZookeeperConfig instance;
	//分布式锁在zookeeper中的根节点
	private String lockRootPath;
	
	/**
	 * 本地锁
	 */
	private ReentrantLock localLock = new ReentrantLock();
	/**
	 * 本地锁版本号
	 */
	private AtomicLong localLockVersion = new AtomicLong(0);
	private static final String lock_data_node = "dataNode";
	
	public SharedReentrantReadWriteLock(String lockRootNode) {
		instance = ZookeeperConfig.getInstance();
		this.lockRootPath = lockRootNode;
		lock = new InterProcessReadWriteLock(instance.getCuratorFramework(), lockRootNode);
		// 创建节点并添加监听
		createNodeAndAddListener();
	}

	/**
	 * 得到锁数据节点数据路径
	 * 
	 * @return
	 */
	public String getFullLockDataNodePath() {
		return lockRootPath + "/" + lock_data_node;
	}

	/**
	 * 创建节点并添加监听
	 */
	private void createNodeAndAddListener() {
		ZookeeperConfig.getInstance().createRootNode(getFullLockDataNodePath());
		setLockDataNodeData(localLockVersion.get());
	}
	
	/**
	 * 得到锁节点数据
	 * @return
	 */
	private Long getLockDataNodeData(){
		byte[] nodeData = ZookeeperConfig.getInstance().getNodeData(getFullLockDataNodePath());
		Long data = Long.parseLong(new String(nodeData,Charset.defaultCharset()));
		return data;
	}
	
	/**
	 * 设置锁节点数据
	 * @param data
	 */
	private void setLockDataNodeData(Long data){
		ZookeeperConfig.getInstance().setNodeData(getFullLockDataNodePath(), Long.toString(data).getBytes(Charset.defaultCharset()));
		localLockVersion.set(data);
	}
	
	/**
	 * 更新锁节点数据
	 */
	private void updateLockDataNodeData(){
		Long lockDataNodeData = getLockDataNodeData();
		lockDataNodeData++;
		setLockDataNodeData(lockDataNodeData);
		localLockVersion.set(lockDataNodeData);
	}

	/**
	 * 写锁
	 * @param readWriteCallback
	 */
	public void writeLock(ReadWriteCallback readWriteCallback) {
		try {
			lock.writeLock().acquire(readWriteCallback.getTime(), readWriteCallback.getUnit());
			readWriteCallback.callback();
			updateLockDataNodeData();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			try {
				lock.writeLock().release();
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
	}
	
	/**
	 * 读锁
	 * @param readWriteCallback
	 */
	public void readLock(ReadWriteCallback readWriteCallback) {
		try {
			lock.readLock().acquire(readWriteCallback.getTime(), readWriteCallback.getUnit());
			
			if(getLockDataNodeData() != localLockVersion.get()){
				try {
					localLock.lock();
					Long lockDataNodeData = getLockDataNodeData();
					if(lockDataNodeData != localLockVersion.get()){
						readWriteCallback.readFail();
						localLockVersion.set(lockDataNodeData);
					}
				} finally {
					if(localLock.isLocked()){
						localLock.unlock();
					}
				}
			}
			readWriteCallback.callback();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			try {
				lock.readLock().release();
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
	}
}
