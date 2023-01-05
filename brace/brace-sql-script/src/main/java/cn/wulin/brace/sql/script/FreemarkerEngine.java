package cn.wulin.brace.sql.script;

import java.io.StringWriter;
import java.io.Writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.wulin.brace.sql.script.domain.EngineParam;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * freemarkerEngine
 * @author ThinkPad
 *
 */
public class FreemarkerEngine {
	private final static Logger LOGGER = LoggerFactory.getLogger(FreemarkerEngine.class);
	
	private Configuration cfg;

	public FreemarkerEngine() {
		cfg = new Configuration(Configuration.VERSION_2_3_25);
		cfg.setDefaultEncoding("UTF-8");
	}
	
	public String parseScript(EngineParam engineParam) {
		
		String text = engineParam.getCurrentCommand().getText().trim();
		
		StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
		stringTemplateLoader.putTemplate(engineParam.getName(),text);
		cfg.setTemplateLoader(stringTemplateLoader);
	   
	    try (Writer writer = new StringWriter()){
			Template template = cfg.getTemplate(engineParam.getName());
			
			template.process(engineParam,writer);
			String processText = writer.toString();
			return processText;
		} catch (Exception e) {
			LOGGER.error("freemarker执行出错!",e);
		} 
		return null;
	}
	

}
