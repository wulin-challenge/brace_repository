package cn.wulin.brace.sql.script.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import cn.hutool.core.lang.UUID;
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
	 * 转义脚本,并将其进行缓存
	 * @param name 命令名称
	 * @param freemarkerParse 是否使用freemarker解析脚本
	 * @return
	 */
	public String escapeScriptCache(String name,Boolean freemarkerParse) {
		String key = this.name+":"+name+":escapeScript:cache";
		String text = (String) params.get(key);
		
		if(params.containsKey(key) && StringUtils.isNotBlank(text)) {
			params.put(key, text);
			return text;
		}
		
		text = escapeScript(name,freemarkerParse);
		params.put(key, text);
		return text;
	}
	
	/**
	 * 转义脚本
	 * @param name 命令名称
	 * @param freemarkerParse 是否使用freemarker解析脚本
	 * @return
	 */
	public String escapeScript(String name,Boolean freemarkerParse) {
		Command command = getCommand(name);
		if(freemarkerParse) {
			EngineParam engineParam = new EngineParam();
			engineParam.setName(name);
			engineParam.setCurrentScripts(currentScripts);
			engineParam.getParams().putAll(this.getParams());
			String selectSql = ENGINE.parseScript(engineParam);
			
			return selectSql;
		}
		return command.getText();
	}
	
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
	 * 通过别名获取缓存的值
	 * @param name
	 * @param alias
	 * @return
	 */
	public Long getNumberByAlias(String name,String alias) {
		String aliasKey = this.name+":"+name+":replaceNumber:sequence:alias:"+alias;
		Long number = (Long) params.get(aliasKey);
		
		if(params.containsKey(aliasKey) && number != null) {
			return number;
		}
		return null;
	}
	
	/**
	 * 在执行脚本前替换,后面的数字自增1
	 * @param name 脚本名称
	 * @param alias 当前序列值的别名
	 * @return
	 */
	public Long replaceNumberSequence(String name,String alias) {
		String key = this.name+":"+name+":replaceNumber:sequence";
		String aliasKey = this.name+":"+name+":replaceNumber:sequence:alias:"+alias;
		Long number = (Long) params.get(key);
		if(params.containsKey(key) && number != null) {
			number++;
			params.put(key, number);
			
			//缓存别名对应的值
			if(StringUtils.isNotBlank(alias)) {
				params.put(aliasKey, number);
			}
			return number;
		}
		
		number = replaceNumber(name);
		params.put(key, number);
		
		//缓存别名对应的值
		if(StringUtils.isNotBlank(alias)) {
			params.put(aliasKey, number);
		}
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
		engineParam.getParams().putAll(this.getParams());
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
	 * 设置变量,并且返回当前值
	 * @param key
	 * @param value
	 * @return
	 */
	public Object setGetVariable(String key,Object value) {
		String key2 = this.name+":setGetVariable:"+key;
		params.put(key2, value);
		return value;
	}
	
	/**
	 * 设置变量,并且返回当前值
	 * @param key
	 * @param value
	 * @return
	 */
	public void setVariable(String key,Object value) {
		String key2 = this.name+":setGetVariable:"+key;
		params.put(key2, value);
	}
	
	/**
	 * 获得变量
	 * @param key
	 * @param value
	 * @return
	 */
	public Object getVariable(String key) {
		String key2 = this.name+":setGetVariable:"+key;
		return params.get(key2);
	}
	
	/**
	 * 得到uuid
	 * @return
	 */
	public String getUuid() {
		return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
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
		engineParam.getParams().putAll(this.getParams());
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
		engineParam.getParams().putAll(this.getParams());
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
