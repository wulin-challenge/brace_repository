package cn.wulin.brace.zookeeper.locks;

/**
 * 分布式读写锁
 * @author wubo
 *
 */
public interface DistributedReadWriteLock {
	
	/**
	 * 写锁
	 * @param readWriteCallback
	 */
	public <T> T writeLock(ReadWriteCallback<T> readWriteCallback);
	
	/**
	 * 读锁
	 * @param readWriteCallback
	 */
	public <T> T readLock(ReadWriteCallback<T> readWriteCallback);

}
