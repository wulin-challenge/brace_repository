package cn.wulin.brace.sql.script;

import cn.wulin.brace.sql.script.dao.SqlScriptDao;

/**
 * 初始化sql脚本
 * @author wulin
 *
 */
public interface InitializingScript {
	
	/**
	 * 初始化sql脚本
	 * @param resourceScript 执行脚本的对象
	 * @param sqlScriptDao 存储脚本信息dao
	 */
	void initializing(ResourceScript resourceScript,SqlScriptDao sqlScriptDao);

}
