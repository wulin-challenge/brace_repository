package cn.wulin.brace.sql.script.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义列
 * @author wulin
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface Column {

	/**
	 * 列的名称
	 * @return
	 */
	String name() default "";
	
	/**
	 * 数据具体类型(利用这个是为避开不同数据库表达相同数据类型之间的差异)
	 */
	String dataType() default "";
	
	/**
	 * 列的长度
	 * @return
	 */
	int length() default 255;
	
	/**
	 * 创建索引的语句,例如: create index idx_ft_id on base_transfer_mapping(fileTransferId)
	 * @return
	 */
	String indexCommand() default "";
}
