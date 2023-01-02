package cn.wulin.brace.sql.script.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * <sql>元素
 * @author wubo
 *
 */
public class Sql {
	
	private String database; //数据库
	private List<Command> commandList = new ArrayList<Command>(); //command集合
	
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
	public List<Command> getCommandList() {
		return commandList;
	}
	public void setCommandList(List<Command> commandList) {
		this.commandList = commandList;
	}

}
