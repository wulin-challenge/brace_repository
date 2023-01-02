package cn.wulin.brace.sql.script.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import cn.wulin.brace.sql.script.FreemarkerEngine;
import cn.wulin.brace.sql.script.ResourceScript;

/**
 * 引擎参数
 * @author wulin
 *
 */
@SuppressWarnings("unchecked")
public class EngineParam {
	private final static Logger LOGGER = LoggerFactory.getLogger(ResourceScript.class);
	private static final FreemarkerEngine ENGINE = new FreemarkerEngine();
	private String name;

	private Map<String,Object> params = new HashMap<>();
	
	/**
	 * 当前脚本的所有参数
	 */
	private Map<String,Command> currentScripts = new HashMap<String,Command>();
	
	/**
	 * 在执行脚本前替换,后面的数字自增1
	 * @return
	 */
	public Long replaceNumberSequence(String name) {
		String key = this.name+":"+name+":replaceNumber:sequence";
		Long number = (Long) params.get(key);
		if(params.containsKey(key) && number != null) {
			number++;
			params.put(key, number);
			return number;
		}
		
		number = replaceNumber(name);
		params.put(key, number);
		return number;
		
	}
	
	/**
	 * 将查询处理的值缓存起来,第二次直接使用缓存
	 * @param name
	 * @return
	 */
	public Long replaceNumberCache(String name) {
		String key = this.name+":"+name+":replaceNumber:cache";
		Long number = (Long) params.get(key);
		if(params.containsKey(key) && number != null) {
			return number;
		}
		number = replaceNumber(name);
		params.put(key, number);
		return number;
	}
	
	/**
	 * 查询出数字新变量的值,然后返回
	 * @return
	 */
	public Long replaceNumber(String name) {
		Command command = getCommand(name);
		if(!command.getType().trim().equalsIgnoreCase(CommandTypeEnum.SELECT.getName())) {
			throw new RuntimeException("replaceNumber的命令类型必须为select");
		}
		
		EngineParam engineParam = new EngineParam();
		engineParam.setName(name);
		engineParam.setCurrentScripts(currentScripts);
		String selectSql = ENGINE.parseScript(engineParam);
		
		JdbcTemplate jdbcTemplate = ResourceScript.getBeanFactory2().getBean(JdbcTemplate.class);
		Long number = 0L;
		try {
			number = jdbcTemplate.queryForObject(selectSql, Long.class);
		} catch (Exception e) {
			LOGGER.error("replaceNumber 执行数据库失败!",e);
		}
		return number;
	}
	
	/**
	 * 将查询处理的值缓存起来,第二次直接使用缓存
	 * @param name
	 * @return
	 */
	public Map<String,Object> replaceMapCache(String name) {
		String key = this.name+":"+name+":replaceMap:cache";
		Map<String,Object> map = (Map<String,Object>) params.get(key);
		if(params.containsKey(key) && map != null) {
			return map;
		}
		map = replaceMap(name);
		params.put(key, map);
		return map;
	}
	
	public Map<String,Object> replaceMap(String name){
		Command command = getCommand(name);
		if(!command.getType().trim().equalsIgnoreCase(CommandTypeEnum.SELECT.getName())) {
			throw new RuntimeException("replaceMap的命令类型必须为select");
		}
		
		EngineParam engineParam = new EngineParam();
		engineParam.setName(name);
		engineParam.setCurrentScripts(currentScripts);
		String selectSql = ENGINE.parseScript(engineParam);
		
		JdbcTemplate jdbcTemplate = ResourceScript.getBeanFactory2().getBean(JdbcTemplate.class);
		try {
			Map<String, Object> map = jdbcTemplate.queryForMap(selectSql);
			return map;
		} catch (Exception e) {
			LOGGER.error("replaceMap 执行数据库失败!",e);
		}
		return null;
	}
	
	
	/**
	 * 将查询处理的值缓存起来,第二次直接使用缓存
	 * @param name
	 * @return
	 */
	public List<Map<String,Object>> replaceListCache(String name) {
		String key = this.name+":"+name+":replaceList:cache";
		List<Map<String,Object>> list = (List<Map<String,Object>>) params.get(key);
		if(params.containsKey(key) && list != null) {
			return list;
		}
		list = replaceList(name);
		params.put(key, list);
		return list;
	}
	
	public List<Map<String,Object>> replaceList(String name){
		Command command = getCommand(name);
		if(!command.getType().trim().equalsIgnoreCase(CommandTypeEnum.SELECT.getName())) {
			throw new RuntimeException("replaceList的命令类型必须为select");
		}
		
		EngineParam engineParam = new EngineParam();
		engineParam.setName(name);
		engineParam.setCurrentScripts(currentScripts);
		String selectSql = ENGINE.parseScript(engineParam);
		
		JdbcTemplate jdbcTemplate = ResourceScript.getBeanFactory2().getBean(JdbcTemplate.class);
		try {
			List<Map<String, Object>> list = jdbcTemplate.queryForList(selectSql);
			return list;
		} catch (Exception e) {
			LOGGER.error("replaceMap 执行数据库失败!",e);
		}
		return null;
	}
	
	/**
	 * 得到当前命令
	 * @return
	 */
	public Command getCurrentCommand() {
		return getCommand(this.name);
	}
	
	/**
	 * 得到指定名称的命令
	 * @param name
	 * @return
	 */
	public Command getCommand(String name) {
		return currentScripts.get(name);
	}

	/**
	 * 设置当前脚本
	 * @param sql
	 */
	public void setCurrentScripts(Sql sql) {
		List<Command> commandList = sql.getCommandList();
		for (Command command : commandList) {
			currentScripts.put(command.getName(), command);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public Map<String, Command> getCurrentScripts() {
		return currentScripts;
	}

	public void setCurrentScripts(Map<String, Command> currentScripts) {
		this.currentScripts = currentScripts;
	}
}
