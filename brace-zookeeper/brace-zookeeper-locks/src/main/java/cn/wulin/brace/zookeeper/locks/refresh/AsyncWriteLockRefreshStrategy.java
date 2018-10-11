package cn.wulin.brace.zookeeper.locks.refresh;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 异步写锁写数据后的刷新数据的策略
 * @author wubo
 *
 */
public class AsyncWriteLockRefreshStrategy extends AbstractWriteLockRefreshStrategy{
	
	public AsyncWriteLockRefreshStrategy() {}
	
	public AsyncWriteLockRefreshStrategy(long intervalTime,long delayTime) {
		this.intervalTime = intervalTime;
		this.delayTime = delayTime;
	}

	@Override
	protected void doRefresh(RefrechHook refrechHook) {
		asyncDelayRefreshDealWith(refrechHook, delayTime);
	}

	/**
	 * 异步延迟刷新处理
	 * @param refrechHook 刷新钩子
	 * @param delayTime 延迟执行时间
	 */
	private void asyncDelayRefreshDealWith(RefrechHook refrechHook,long delayTime){
		//延迟执行
		Timer timer = new Timer();// 实例化Timer类  
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				//避免第一次刷新执行两次
				try{
					refreshLock.lock();
					delayMark.set(false);
				}finally{
					if(refreshLock.isLocked()){
						refreshLock.unlock();
					}
				}
				refrechHook.doRefresh();
				checkNextWhetherRefrech(refrechHook);
				this.cancel();
			}
		},delayTime);
	};
}
