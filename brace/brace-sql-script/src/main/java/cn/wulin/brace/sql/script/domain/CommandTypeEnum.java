package cn.wulin.brace.sql.script.domain;

/**
 * 命令类型枚举
 * @author wulin
 *
 */
public enum CommandTypeEnum {
	
	INSERT("insert","插入语句"),
	UPDATE("update","更新语句"),
	DELETE("delete","删除语句"),
	SELECT("select","查询语句"),
	SCRIPT("script","脚本语句"),
	FUNCTION("function","函数"),
	INDEX("index","索引");
	
	private String name;
	private String description;
	
	public static CommandTypeEnum findByName(String name) {
		CommandTypeEnum[]  enums = values();
		for (CommandTypeEnum elementElement : enums) {
			if(elementElement.getName().equals(name.trim())) {
				return elementElement;
			}
		}
		return null;
	}
	
	private CommandTypeEnum(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
