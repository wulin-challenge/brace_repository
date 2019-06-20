package cn.wulin.brace.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.ho.yaml.Yaml;

/**
 * 读取yml配置文件的工具类
 * @author wubo
 *
 */
@SuppressWarnings("unchecked")
public class YamlUtil {
	
	/**
	 * application.yml文件路径
	 */
	private static final String APPLICATION_PATH = System.getProperty("user.dir")+"/config/application.yml";
	private static Map<String,Object> application;
	

	static{
		File  applicationFile = new File(APPLICATION_PATH);
		try {
			application = (Map<String, Object>) Yaml.loadType(applicationFile, HashMap.class);
		} catch (FileNotFoundException e) {
			LoggerUtils.error(APPLICATION_PATH+" 文件没有找到!", e);;
		}
	}
	
	private static Map<String,Object> getApplicationYaml(){
		return application;
	}
	
	/**
	 * 使用 xx.yy.zz 的格式读取yml的数据
	 * @param key
	 * @param defaultValue 默认值
	 * @param clazz
	 * @return
	 */
	public static <T> T getValue(String key,T defaultValue,Class<T> clazz){
		if(StringUtils.isBlank(key)){
			return null;
		}
		Map<String,Object> currentMap = getApplicationYaml();
		String[] keyArray = key.split("\\.");
		for (int i=0;i<keyArray.length;i++) {
			if(currentMap != null){
				if(i == keyArray.length-1){
					T t = (T)currentMap.get(keyArray[i]);
					
					if(t == null && defaultValue != null){
						t = defaultValue;
					}
					return t;
				}else{
					currentMap = (Map<String, Object>) currentMap.get(keyArray[i]);
				}
			}else{
				if(defaultValue != null){
					return defaultValue;
				}
				LoggerUtils.error("在 application.yml 文件中找不到 "+key+"属性的值!");
				return null;
			}
		}
		return null;
	}
	
	/**
	 * 使用 xx.yy.zz 的格式读取yml的数据
	 * @param key
	 * @param clazz
	 * @return
	 */
	public static <T> T getValue(String key,Class<T> clazz){
		return getValue(key, null, clazz);
	}
	
	/**
	 * 使用 xx.yy.zz 的格式读取yml的数据
	 * @param key
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> getListValue(String key,Class<T> clazz){
		return getListValue(key, null, clazz);
	}
	
	/**
	 * 使用 xx.yy.zz 的格式读取yml的数据
	 * @param key
	 * @param defaultValue 默认值
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> getListValue(String key,List<T> defaultValue,Class<T> clazz){
		List<T> listValue = new ArrayList<T>();
		if(StringUtils.isBlank(key)){
			return null;
		}
		Object currentValue = getApplicationYaml();
		String[] keyArray = key.split("\\.");
		for (int i=0;i<keyArray.length;i++) {
			
			if(currentValue != null){
				if(currentValue instanceof HashMap){
					Map<String,Object> currentMap = (Map<String,Object>)currentValue;
					currentValue = currentMap.get(keyArray[i]);
				}else if(currentValue instanceof ArrayList){
					List<Map<String,Object>> currentArray = (List<Map<String, Object>>) currentValue;
					for (Map<String, Object> map : currentArray) {
						T t = (T)map.get(keyArray[i]);
						if(t != null){
							listValue.add(t);
						}
					}
				}
			}else{
				if(defaultValue != null){
					return defaultValue;
				}
				LoggerUtils.error("在 application.yml 文件中找不到 "+key+"属性的值!");
				return null;
			}
		}
		
		if(listValue.size()==0 && defaultValue != null){
			return defaultValue;
		}
		
		if(listValue.size()==0){
			LoggerUtils.error("在 application.yml 文件中找不到 "+key+"属性的值!");
		}
		return listValue;
	}
}
