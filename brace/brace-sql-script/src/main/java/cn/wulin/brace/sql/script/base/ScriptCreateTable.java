package cn.wulin.brace.sql.script.base;

/**
 * realtime 框架内部创建表接口
 * @author wulin
 *
 */
public interface ScriptCreateTable {
	
	/**
	 * 数据源配置
	 * @return
	 */
	String dataSourceTable();
	
	/**
	 * 判断是否支持该数据库类型
	 * @param database
	 * @return
	 */
	boolean support(String database);
}
