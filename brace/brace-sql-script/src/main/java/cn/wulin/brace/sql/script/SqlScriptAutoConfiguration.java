package cn.wulin.brace.sql.script;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import cn.wulin.brace.sql.script.base.impl.MysqlCreateTableImpl;
import cn.wulin.brace.sql.script.dao.impl.SqlScriptDaoImpl;
import cn.wulin.brace.sql.script.properties.SqlScriptProperties;

/**
 * sql 脚本自动配置
 * @author wulin
 *
 */
@Configuration
@EnableConfigurationProperties({SqlScriptProperties.class})
public class SqlScriptAutoConfiguration {
	
	@Bean("mysqlCreateTableImpl")
	public MysqlCreateTableImpl mysqlCreateTableImpl() {
		return new MysqlCreateTableImpl();
	}
	
	@Bean
	@DependsOn(value= {"mysqlCreateTableImpl"})
	public ResourceScript ResourceScriptConfig() {
		return new ResourceScript();
	}
	
	@Bean
	public SqlScriptDaoImpl sqlScriptDaoImpl() {
		return new SqlScriptDaoImpl();
	}
	
	@Bean
	public InitializingScriptBeanPostProcessor initializingScriptBeanPostProcessor() {
		return new InitializingScriptBeanPostProcessor();
	}

}
