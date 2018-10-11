package cn.wulin.brace.zookeeper.locks.refresh;

/**
 * 刷新加载的回调钩子类
 * @author wubo
 *
 */
public abstract class RefrechHook {

	/**
	 * 刷新加载的回调钩子方法
	 */
	public abstract void doRefresh();
}
