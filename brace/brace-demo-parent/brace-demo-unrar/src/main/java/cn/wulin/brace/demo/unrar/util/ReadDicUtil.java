package cn.wulin.brace.demo.unrar.util;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 读取字典工具类
 */
public class ReadDicUtil {
	private final static Logger LOGGER = LoggerFactory.getLogger(ReadDicUtil.class); 
	private final static String PWD_DIC_PATH = System.getProperty("user.dir")+"/config/pwd.dic";
	private final static String BLANK_REGEX = "[ ]+";
	
	/**
	 * 字典注释
	 */
	private final static String DIC_ANNOTATION = "--";

	/**
	 * 读取字典集合
	 * @param addMargeDic 添加合并字典,提高解码效率
	 * @return
	 */
	public static List<String[]> readPwdDicList(Boolean addMargeDic){
		List<String[]> dicList = new ArrayList<>();
		
		try {
			List<String> readLines = FileUtils.readLines(new File(PWD_DIC_PATH), Charset.forName("UTF-8"));
			for (String line : readLines) {
				if(StringUtils.isBlank(line) || line.startsWith(DIC_ANNOTATION)) {
					continue;
				}
				
				String[] pwdDic = line.split(BLANK_REGEX);
				dicList.add(pwdDic);
				
				//对密码进行合并添加
				if(addMargeDic) {
					String margeString = getString(pwdDic);
					dicList.add(new String[]{margeString});
					dicList.add(new String[]{margeString.toLowerCase()});
					dicList.add(new String[]{margeString.toUpperCase()});
				}
			}
		} catch (Exception e) {
			LOGGER.error("读取密码字典失败!",e);
		}
		return dicList;
	}
	
	/**
	 * 将字符串数组转为无分隔字符串
	 * @param arrays
	 * @return
	 */
	public static String getString(String[] arrays) {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < arrays.length; i++) {
			s.append(arrays[i]);
		}
		return s.toString();
	}
}
