package cn.wulin.brace.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import cn.wulin.brace.utils.impl.JarPathImpl;

public class PathUtil {
	/**
	 * 冒号(:) 是因为windows的跟了有冒号
	 */
	private static final String regex = "[^:\\.\\./]+/\\.\\./";
	
	/**
	 * 替换反斜杠,解决在windows,linux下的路径问题
	 * @param path
	 * @return
	 */
	public static String replaceSprit(String path){
		if(StringUtils.isEmpty(path)){
			return "";
		}
		path = path.replace("\\\\", "/"); //// Java中4个反斜杠表示一个反斜杠
		path = path.replace("\\", "/"); 
		path = path.replace("//", "/"); // 处理 d:xx//yy/zz的问题
		return pathFormat(path);
	}
	
	/**
	 * 路径格式
	 * <p> 专门处理../../这种相对路径
	 * <p> 例如 路径为 d:aa/bb/cc/xx/yy/zz/../../../dd/ff/g.txt;
	 * <p> 格式后的路径为 d:aa/bb/cc/dd/ff/g.txt
	 */
	public static String pathFormat(String str){
		StringBuilder newStr = new StringBuilder();
		Matcher matcher = Pattern.compile(regex).matcher(str);
		if(matcher.find()){
			matcher.reset();
			int start = 0;
			int end = 0;
			while(matcher.find()){
				end = matcher.start();
				newStr.append(str.substring(start, end));
				start = matcher.end();
				newStr.append(str.substring(start));
				return pathFormat(newStr.toString());
			}
		}
		return str;
	}
	
	/**
	 * 路径的拼接
	 * @param prefixPath 前缀路径,不能为空
	 * @param suffixPath 后缀路径,可以为空
	 * @return
	 */
	public static String pathSplicing(String prefixPath,String suffixPath) {
		prefixPath = replaceSprit(prefixPath);
		suffixPath = replaceSprit(suffixPath);
		
		if(!prefixPath.endsWith("/")) {
			prefixPath +="/";
		}
		
		if(StringUtils.isBlank(suffixPath)) {
			return prefixPath;
		}
		
		if(suffixPath.startsWith("/")) {
			suffixPath = suffixPath.substring(1);
		}
		return prefixPath+suffixPath;
	}
	
	/**
	 * 得到启动jar文件的路径
	 * @return
	 */
	public static String getJarPath() {
		return new JarPathImpl().getJarPath();
	}
}
