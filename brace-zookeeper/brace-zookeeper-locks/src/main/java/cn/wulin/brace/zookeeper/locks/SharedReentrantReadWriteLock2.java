package cn.wulin.brace.zookeeper.locks;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
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
public class SharedReentrantReadWriteLock2 {

	private Logger logger = LoggerFactory.getLogger(SharedReentrantReadWriteLock2.class);
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
	
	//放弃检测的开始时间
	private Long giveUpCheckStartTime;
	
	public SharedReentrantReadWriteLock2(String lockRootNode) {
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
	public <T> T writeLock(ReadWriteCallback<T> readWriteCallback) {
		try {
			lock.writeLock().acquire(readWriteCallback.getTime(), readWriteCallback.getUnit());
			T result = readWriteCallback.callback();
			readWriteCallback.readFail();
			updateLockDataNodeData();
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
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
	public <T> T readLock(ReadWriteCallback<T> readWriteCallback) {
		
		if(!readWriteCallback.getEveryTimeCheckLock()){
			//若成立,直接调用回调
			//为性能,放弃了%100的成功率
			if(judgeGiveUpCheckLock(readWriteCallback)){
				return readWriteCallback.callback();//建议此处的回调业务在毫秒内完成
			}else{
				giveUpCheckStartTime = getCurrentTime();
			}
		}
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
			return readWriteCallback.callback();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		} finally {
			try {
				lock.readLock().release();
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
	}

	/**
	 * 判断放弃检测锁是否成立,此处为原则操作
	 * @param readWriteCallback
	 * @return
	 */
	private <T> boolean judgeGiveUpCheckLock(ReadWriteCallback<T> readWriteCallback) {
		//放弃检测的开始时间,主要是处理第一次请求为null的情况
		try {
			localLock.lock();
			long currentTime = getCurrentTime();
			giveUpCheckStartTime = giveUpCheckStartTime == null?currentTime:giveUpCheckStartTime;
			//将TimeUnit单位的放弃检测时间转为毫秒数
			long checkTime = readWriteCallback.getGiveUpCheckTime();
			TimeUnit checkUnit = readWriteCallback.getGiveUpCheckTimeUnit();
			final Long millisToWait = (checkUnit != null) ? checkUnit.toMillis(checkTime) : null;
			return giveUpCheckJudge(currentTime, checkUnit, millisToWait);
		} finally {
			if(localLock.isLocked()){
				localLock.unlock();
			}
		}
	}

	/**
	 * 获取当前时间
	 * @return
	 */
	private long getCurrentTime() {
		return System.currentTimeMillis();
	}

	/**
	 * 放弃检测判断
	 * @param currentTime
	 * @param checkUnit
	 * @param millisToWait
	 * @return
	 */
	private boolean giveUpCheckJudge(long currentTime, TimeUnit checkUnit, final Long millisToWait) {
		return checkUnit != null && ((currentTime-giveUpCheckStartTime)<=millisToWait) && (currentTime != giveUpCheckStartTime);
	}
	
	
}
