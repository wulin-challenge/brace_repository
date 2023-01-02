package cn.wulin.brace.sql.script.base.impl;

import cn.wulin.brace.sql.script.base.ScriptCreateTable;

public class MysqlCreateTableImpl implements ScriptCreateTable{

	@Override
	public boolean support(String database) {
		return "MYSQL".equalsIgnoreCase(database);
	}
	
	@Override
	public String dataSourceTable() {
		String sql = "create table sql_script("
				+ " `id` varchar(48) primary key not null,"
				+ " `inited` int(2) UNSIGNED NOT NULL COMMENT '是否被初始化,1:是,0:否',"
				+ " `type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '执行sql的类型',"
				+ " `return_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'sql的返回类型',"
				+ " `database_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '数据库的类型',"
				+ " `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT 'sql内容',"
				+ " `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'sql脚本的名称',"
				+ " `table_name` varchar(65) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'sql脚本对应的表名',"
				+ " `description` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述，最多200字',"
				+ " `create_date` bigint(20) UNSIGNED NOT NULL COMMENT '创建日期',"
				+ " `modify_date` bigint(20) UNSIGNED NOT NULL COMMENT '修改日期'"
				+ ")";
		return sql;
	}

}
