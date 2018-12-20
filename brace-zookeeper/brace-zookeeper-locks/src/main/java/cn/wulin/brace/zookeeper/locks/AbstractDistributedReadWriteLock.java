package cn.wulin.brace.zookeeper.locks;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;

import cn.wulin.brace.zookeeper.ZookeeperConfig;
import cn.wulin.brace.zookeeper.event.ZookeeperCuratorEvent;
import cn.wulin.brace.zookeeper.event.ZookeeperCuratorEventAdapter;
import cn.wulin.brace.zookeeper.locks.util.LockUtil;
import cn.wulin.ioc.logging.Logger;
import cn.wulin.ioc.logging.LoggerFactory;

/**
 * 分布式对写锁的基类
 * @author wubo
 *
 */
public abstract class AbstractDistributedReadWriteLock implements DistributedReadWriteLock{
	
	private Logger logger = LoggerFactory.getLogger(AbstractDistributedReadWriteLock.class);
	/**
	 * 锁的数据节点
	 */
	public static final String lock_data_node = "dataNode";
	
	//分布式锁
	protected InterProcessReadWriteLock lock;
	protected ZookeeperConfig instance;
	//分布式锁在zookeeper中的根节点
	protected String lockRootPath;
	
	/**
	 * 本地锁
	 */
	protected ReentrantLock localLock = new ReentrantLock();
	/**
	 * 本地锁版本号
	 */
	protected AtomicLong localLockVersion = new AtomicLong(0);
	
	
	//放弃检测的开始时间
	protected Long giveUpCheckStartTime;
	
	/**
	 * zookeeper事件
	 */
	protected ZookeeperCuratorEvent event;
	
	/**
	 * 本地修改数据后是否进行本地通知
	 */
	protected boolean nativeNotify;
	
	public AbstractDistributedReadWriteLock(String lockRootNode) {
		this.instance = ZookeeperConfig.getInstance();
		this.lockRootPath = lockRootNode;
		this.lock = new InterProcessReadWriteLock(instance.getCuratorFramework(), lockRootNode);
		// 创建节点并添加监听
		this.event = getZookeeperCuratorEvent();
		createNodeAndAddListener(this.event);
	}
	
	public AbstractDistributedReadWriteLock(String lockRootNode,ZookeeperCuratorEvent event,boolean nativeNotify) {
		this.nativeNotify = nativeNotify;
		instance = ZookeeperConfig.getInstance();
		this.lockRootPath = lockRootNode;
		lock = new InterProcessReadWriteLock(instance.getCuratorFramework(), lockRootNode);
		this.event = event;
		// 创建节点并添加监听
		createNodeAndAddListener(event);
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
				giveUpCheckStartTime = LockUtil.getCurrentTime();
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
	protected void createNodeAndAddListener(ZookeeperCuratorEvent zookeeperCuratorEvent) {
		ZookeeperConfig.getInstance().createRootNode(getFullLockDataNodePath(),zookeeperCuratorEvent);
		setLockDataNodeData(localLockVersion.get());
	}
	
	/**
	 * 得到锁节点数据
	 * @return
	 */
	protected Long getLockDataNodeData(){
		byte[] nodeData = ZookeeperConfig.getInstance().getNodeData(getFullLockDataNodePath());
		Long data = Long.parseLong(new String(nodeData,Charset.defaultCharset()));
		return data;
	}
	
	/**
	 * 设置锁节点数据
	 * @param data
	 */
	protected void setLockDataNodeData(Long data){
		ZookeeperConfig.getInstance().setNodeData(getFullLockDataNodePath(), Long.toString(data).getBytes(Charset.defaultCharset()));
		localLockVersion.set(data);
	}
	
	/**
	 * 更新锁节点数据
	 */
	protected void updateLockDataNodeData(){
		Long lockDataNodeData = getLockDataNodeData();
		lockDataNodeData++;
		setLockDataNodeData(lockDataNodeData);
		localLockVersion.set(lockDataNodeData);
	}
	
	/**
	 * 判断放弃检测锁是否成立,此处为原则操作
	 * @param readWriteCallback
	 * @return
	 */
	protected <T> boolean judgeGiveUpCheckLock(ReadWriteCallback<T> readWriteCallback) {
		//放弃检测的开始时间,主要是处理第一次请求为null的情况
		try {
			localLock.lock();
			long currentTime = LockUtil.getCurrentTime();
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
	 * 放弃检测判断
	 * @param currentTime
	 * @param checkUnit
	 * @param millisToWait
	 * @return
	 */
	protected boolean giveUpCheckJudge(long currentTime, TimeUnit checkUnit, final Long millisToWait) {
		return checkUnit != null && ((currentTime-giveUpCheckStartTime)<=millisToWait) && (currentTime != giveUpCheckStartTime);
	}
	
	/**
	 * 得到本地锁版本号
	 * @return
	 */
	public AtomicLong getLocalLockVersion() {
		return localLockVersion;
	}
	
	/**
	 * 得到本地是否通知
	 * @return
	 */
	public boolean getNativeNotify() {
		return nativeNotify;
	}

	protected abstract ZookeeperCuratorEvent getZookeeperCuratorEvent();
	
}
