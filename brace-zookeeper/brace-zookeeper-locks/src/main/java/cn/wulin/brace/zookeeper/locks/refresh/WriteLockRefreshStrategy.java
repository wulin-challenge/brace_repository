package cn.wulin.brace.zookeeper.locks.refresh;

/**
 * 写锁写数据后的刷新数据的策略
 * @author wubo
 *
 */
public interface WriteLockRefreshStrategy {
	
	/**
	 * 刷新
	 */
	void refresh(RefrechHook refrechHook);
	
}
