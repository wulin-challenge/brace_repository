package cn.wulin.brace.sql.script.properties;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "sql.script", ignoreUnknownFields = true)
public class SqlScriptProperties {
	
	private List<String> paths = new ArrayList<>();
	public List<String> getPaths() {
		return paths;
	}

	public void setPaths(List<String> paths) {
		this.paths = paths;
	}
	
}
