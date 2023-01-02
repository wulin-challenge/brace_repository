package cn.wulin.brace.sql.script.domain;

import cn.wulin.brace.sql.script.annotation.Column;
import cn.wulin.brace.sql.script.annotation.EntityTable;
import cn.wulin.brace.sql.script.annotation.Id;

/**
 * 数据源配置实体
 * @author wulin
 *
 */
@EntityTable(name="sql_script")
public class SqlScriptEntity {
	
	/**
	 * 被初始化
	 */
	public static final int INITED = 1;
	
	/**
	 * 没有被初始化
	 */
	public static final int NO_INITED = 0;
	
	
	@Id
	private String id;
	
	/**
	 * 是否可用,1:可用,0:不可用
	 */
	private int inited = NO_INITED;
	
	/**
	 * 脚本名称
	 */
	private String name;
	/**
	 * 脚本类型
	 */
	private String type;
	
	/**
	 * 命令返回类型
	 */
	@Column(name="return_type")
	private String returnType;
	/**
	 * 脚本类型
	 */
	@Column(name="database_type")
	private String databaseType;
	/**
	 * 脚本执行的主表,一般不直接使用,因为一个脚本中可以能会有多个表的sql语句
	 */
	@Column(name="table_name")
	private String tableName;
	
	/**
	 * 脚本类容
	 */
	private String content;
	/**
	 * 描述
	 */
	private String description;
	
	@Column(name="create_date")
	private Long createDate;
	
	@Column(name="modify_date")
	private Long modifyDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getInited() {
		return inited;
	}

	public void setInited(int inited) {
		this.inited = inited;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Long createDate) {
		this.createDate = createDate;
	}

	public Long getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Long modifyDate) {
		this.modifyDate = modifyDate;
	}
	
	public String getDatabaseType() {
		return databaseType;
	}

	public void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}
	
	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	@Override
	public String toString() {
		return "[id=" + id + ", inited=" + inited + ", name=" + name + ", type=" + type
				+ ", returnType=" + returnType + ", databaseType=" + databaseType + ", tableName=" + tableName
				+ ", content=" + content + ", description=" + description + ", createDate=" + createDate
				+ ", modifyDate=" + modifyDate + "]";
	}

}
