package cn.wulin.brace.sql.script.domain;

/**
 * <command>元素
 * @author wubo
 *
 */
public class Command {
	
	private String type;//执行的sql命令类型
	private String name;//执行的sql命令名称
	private String table;//表
	private String text;//命令的内容
	private String returnType = CommandReturnEnum.VOID.getName(); //返回类型
	private String database; //数据库
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getReturnType() {
		return returnType;
	}
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
}
