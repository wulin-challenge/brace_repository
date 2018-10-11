package cn.wulin.brace.zookeeper.locks.refresh;

/**
 * 同步写锁写数据后的刷新数据的策略
 * @author wubo
 *
 */
public class SyncWriteLockRefreshStrategy extends AbstractWriteLockRefreshStrategy{
	
	

	public SyncWriteLockRefreshStrategy() {}
	
	public SyncWriteLockRefreshStrategy(long intervalTime,long delayTime) {
		this.intervalTime = intervalTime;
		this.delayTime = delayTime;
	}

	@Override
	protected void doRefresh(RefrechHook refrechHook) {
		refrechHook.doRefresh();
		checkNextWhetherRefrech(refrechHook);
	}

}
