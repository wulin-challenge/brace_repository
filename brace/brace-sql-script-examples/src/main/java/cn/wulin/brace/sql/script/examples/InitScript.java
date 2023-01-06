package cn.wulin.brace.sql.script.examples;

import java.util.HashMap;
import java.util.Map;

import org.lin.linfreemarker.lin.LinfreemarkerHolder;
import org.lin.linfreemarker.lin.LinfreemarkerThreadParams;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.wulin.brace.sql.script.ResourceScript;

/**
 * 初始化脚本
 * @author wulin
 *
 */
@Component
public class InitScript implements InitializingBean{
	
	@Autowired
	private ResourceScript resourceScript;

	@Override
	public void afterPropertiesSet() throws Exception {
		
		Map<String,Object> params = new HashMap<>();
		params.put("websiteCode", "itzhishi");
		
		LinfreemarkerThreadParams threadParams= new LinfreemarkerThreadParams();
		threadParams.setOutSrc(true);
		LinfreemarkerHolder.setParam(threadParams);
		resourceScript.executeScript(params,"init_script2",false);
		
	}

}
