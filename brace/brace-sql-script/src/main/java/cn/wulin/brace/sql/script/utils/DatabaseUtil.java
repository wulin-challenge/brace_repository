package cn.wulin.brace.sql.script.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 数据库工具类
 * @author wulin
 *
 */
public class DatabaseUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseUtil.class);
	
	/**
	 * 得到数据库类型
	 * @param syncTemplate
	 * @return
	 */
	public static String getDataBaseType(JdbcTemplate syncTemplate) {
		
		DruidDataSource dataSource = (DruidDataSource) syncTemplate.getDataSource();
		String connectUrl = dataSource.getUrl();
		return getDataBaseType(connectUrl);
	}
	
	public static String getDataBaseType(String url) {
		String dbType = JdbcUtils.getDbType(url);
		if(JdbcConstants.MYSQL.equals(dbType)) {
			return "mysql";
		}else if(JdbcConstants.ORACLE.equals(dbType)) {
			return "oracle";
		}else if(JdbcConstants.DM.equals(dbType)) {
			return "dm";
		}else if(JdbcConstants.SQL_SERVER.equals(dbType)) {
			return "SQlServer";
		}
		return dbType;
	}

}
