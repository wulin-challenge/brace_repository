package cn.wulin.brace.zookeeper.locks;

import java.util.concurrent.TimeUnit;
/**
 * 读写回调
 * @author wubo
 *
 */
public abstract class ReadWriteCallback {
	
	private long time;
	private TimeUnit unit;
	public ReadWriteCallback(long time, TimeUnit unit) {
		super();
		this.time = time;
		this.unit = unit;
	}
	
	/**
	 * 业务逻辑回调
	 */
	public abstract void callback();

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
}
