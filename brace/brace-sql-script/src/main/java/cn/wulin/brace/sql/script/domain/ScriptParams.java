package cn.wulin.brace.sql.script.domain;

import java.util.HashMap;
import java.util.Map;

import cn.wulin.brace.sql.script.ResourceScript;

/**
 * 脚本参数
 * @author wulin
 *
 */
public class ScriptParams {
	
	/**
	 * 脚本的公共参数
	 */
	private Map<String,Object> params = new HashMap<>();
	
	/**
	 * 执行的命令名
	 * 当name=ResourceScript.ALL_SCRIPT,表示执行所有脚本,否则就只执行当前脚本
	 */
	private String name = ResourceScript.ALL_SCRIPT;
	
	/**
	 * 可扩展的引擎参数
	 */
	private Class<? extends EngineParam> engineParamClass = EngineParam.class;
	
	/**
	 * 是否保存脚本
	 */
	private Boolean saveScript;
	
	/**
	 * 是否执行freemarker解析
	 */
	private Boolean freemarkerParse = true;
	
	/**
	 * 在数据库执行前对命令进行freemarker解析
	 */
	private Boolean executeBeforeParse = false;

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Class<? extends EngineParam> getEngineParamClass() {
		return engineParamClass;
	}

	public void setEngineParamClass(Class<? extends EngineParam> engineParamClass) {
		this.engineParamClass = engineParamClass;
	}

	public Boolean getSaveScript() {
		return saveScript;
	}

	public void setSaveScript(Boolean saveScript) {
		this.saveScript = saveScript;
	}

	public Boolean getFreemarkerParse() {
		return freemarkerParse;
	}

	public void setFreemarkerParse(Boolean freemarkerParse) {
		this.freemarkerParse = freemarkerParse;
	}

	public Boolean getExecuteBeforeParse() {
		return executeBeforeParse;
	}

	public void setExecuteBeforeParse(Boolean executeBeforeParse) {
		this.executeBeforeParse = executeBeforeParse;
	}

}
