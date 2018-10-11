package cn.wulin.brace.zookeeper.locks.refresh;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import cn.wulin.brace.zookeeper.locks.util.LockUtil;

/**
 * 写锁写数据后的刷新数据的策略共同基类
 * @author wubo
 *
 */
public abstract class AbstractWriteLockRefreshStrategy implements WriteLockRefreshStrategy{
	
	/**
	 * 延迟执行时间
	 */
	protected long delayTime = 3000;
	
	/**
	 * 异步执行直接的间隔时间
	 */
	protected long intervalTime = 3000;

	/**
	 * 本地刷新锁
	 */
	protected ReentrantLock refreshLock = new ReentrantLock();
	
	/**
	 * 第一次请求锁
	 */
	protected ReentrantLock firstRequestLock = new ReentrantLock();
	
	/**
	 * 刷新状态:是否正在刷新,true:表示正在刷新,反之为false
	 */
	protected AtomicBoolean refreshing = new AtomicBoolean(false);

	/**
	 * 延迟执行标记:true:表示延迟执行,false表示立刻执行
	 */
	protected AtomicBoolean delayMark = new AtomicBoolean(false);
	
	/**
	 * 第一个请求的开始时间
	 */
	protected Long firstRequestStartTime;
	@Override
	public void refresh(RefrechHook refrechHook) {
		firstRequestLock.lock();
		if(refreshing.get()){
			try{
				refreshLock.lock();
				firstRequestUnlock();//减锁第一个请求
				delayMark.set(true);
				if(!refreshing.get()){
					asyncDelayRefresh(refrechHook, intervalTime+delayTime);
				}
			}finally{
				if(refreshLock.isLocked()){
					refreshLock.unlock();
				}
			}
			return;
		}else{
			long currentTime;
			try{
				refreshLock.lock();
				refreshing.set(true);
				
				currentTime = LockUtil.getCurrentTime();
				firstRequestStartTime = firstRequestStartTime == null?currentTime:firstRequestStartTime;
				firstRequestUnlock();//减锁第一个请求
			}finally{
				if(refreshLock.isLocked()){
					refreshLock.unlock();
				}
			}
			//保证连续两次请求之间的间隔差
			if(judgeFirstRequest(currentTime)){
				asyncDelayRefresh(refrechHook, intervalTime+delayTime);
			}else{
				doRefresh(refrechHook);
			}
		}
	}
	
	/**
	 * 检测第一次请求
	 * @param currentTime
	 * @return
	 */
	private boolean judgeFirstRequest(long currentTime) {
		return ((currentTime-firstRequestStartTime)<=intervalTime) && (currentTime != firstRequestStartTime);
	}
	
	/**
	 * 异步延迟刷新
	 * @param refrechHook 刷新钩子
	 * @param delayTime 延迟执行时间
	 */
	protected void asyncDelayRefresh(RefrechHook refrechHook,long delayTime){
		try{
			refreshLock.lock();
			delayMark.set(false);
			refreshing.set(true);
		}finally{
			if(refreshLock.isLocked()){
				refreshLock.unlock();
			}
		}
		
		//延迟执行
		Timer timer = new Timer();// 实例化Timer类  
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				doRefresh(refrechHook);
				this.cancel();
			}
		},delayTime);
	};
	
	/**
	 * 减锁第一个请求
	 */
	private void firstRequestUnlock(){
		if(firstRequestLock.isLocked()){
			firstRequestLock.unlock();
		}
	}
	
	/**
	 * 检测下次是否刷新
	 * @param refrechHook
	 */
	protected void checkNextWhetherRefrech(RefrechHook refrechHook){
		try{
			refreshLock.lock();
			if(delayMark.get()){
				asyncDelayRefresh(refrechHook, intervalTime+delayTime);
			}else{
				refreshing.set(false);
			}
		}finally{
			if(refreshLock.isLocked()){
				refreshLock.unlock();
			}
		}
	}
	
	protected abstract void doRefresh(RefrechHook refrechHook);
}
