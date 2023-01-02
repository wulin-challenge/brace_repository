package cn.wulin.brace.sql.script.domain;

/**
 * 执行命令的返回类型
 * @author wulin
 *
 */
public enum CommandReturnEnum {
	
	NUMBER("number","数字类型"),
	MAP("map","map类型"),
	LIST("list","列表类型"),
	VOID("void","没有返回值");
	
	private String name;
	private String description;
	
	public static CommandReturnEnum findByName(String name) {
		CommandReturnEnum[]  enums = values();
		for (CommandReturnEnum elementElement : enums) {
			if(elementElement.getName().equals(name.trim())) {
				return elementElement;
			}
		}
		return null;
	}
	
	private CommandReturnEnum(String name, String description) {
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
