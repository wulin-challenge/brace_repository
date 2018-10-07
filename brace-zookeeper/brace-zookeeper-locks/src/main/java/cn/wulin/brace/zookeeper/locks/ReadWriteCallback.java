package cn.wulin.brace.zookeeper.locks;

import java.util.concurrent.TimeUnit;
/**
 * 读写回调
 * @author wubo
 *
 */
public abstract class ReadWriteCallback<T> {
	
	//锁的超时时间
	private long time;
	//锁的超时时间的单位
	private TimeUnit unit;
	//放弃校验锁的时间
	private long giveUpCheckTime;
	//放弃校验锁的时间单位
	private TimeUnit giveUpCheckTimeUnit;
	//是否每次请求都校验锁,true:每次请求都校验锁,false:第一次校验后,设置放弃校验锁超时时间,在放弃锁超时时间内不再校验锁
	private Boolean everyTimeCheckLock;
	
	public ReadWriteCallback(long time, TimeUnit unit) {
		super();
		this.time = time;
		this.unit = unit;
		giveUpCheckTime = time;
		giveUpCheckTimeUnit = unit;
		everyTimeCheckLock = false;
	}
	public ReadWriteCallback(long time, TimeUnit unit, Boolean everyTimeCheckLock) {
		super();
		this.time = time;
		this.unit = unit;
		this.everyTimeCheckLock = everyTimeCheckLock;
		giveUpCheckTime = time;
		giveUpCheckTimeUnit = unit;
	}
	
	public ReadWriteCallback(long time, TimeUnit unit, long giveUpCheckTime, TimeUnit giveUpCheckTimeUnit,
			Boolean everyTimeCheckLock) {
		super();
		this.time = time;
		this.unit = unit;
		this.giveUpCheckTime = giveUpCheckTime;
		this.giveUpCheckTimeUnit = giveUpCheckTimeUnit;
		this.everyTimeCheckLock = everyTimeCheckLock;
	}
	/**
	 * 业务逻辑回调
	 */
	public abstract T callback();

	/**
	 * 读失败回调
	 */
	public void readFail(){}
	
	public long getTime() {
		return time;
	}

	public TimeUnit getUnit() {
		return unit;
	}
	
	public long getGiveUpCheckTime() {
		return giveUpCheckTime;
	}
	
	public TimeUnit getGiveUpCheckTimeUnit() {
		return giveUpCheckTimeUnit;
	}
	
	public Boolean getEveryTimeCheckLock() {
		return everyTimeCheckLock;
	}
}
