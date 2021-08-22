package cn.wulin.brace.demo.unrar.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * cmd工具类
 * @author wulin
 *
 */
public class CmdUtil {
	private final static Logger LOGGER = LoggerFactory.getLogger(CmdUtil.class); 
	
	/**
	 * 默认切分行数据的默认表达式
	 * <p> 表示以2空格或者两个以上的空客分隔行数据
	 */
	public final static String DATA_REGEX = "[ ]{2,}";
	
	/**
	 * 执行命令并返回String,这里返回的索引为0的行数据
	 * @param cmd
	 * @return
	 */
	public static String execToString(String cmd) {
		return execToString(0, cmd);
	}
	
	/**
	 * 执行命令好指定返回哪一行的数据
	 * @param rowHeader 指定那几行为行头
	 * @param cmd
	 * @return
	 */
	public static String execToString(int rowHeader,String cmd) {
		List<String> exec = exec(cmd);
		if(exec == null || exec.size() == 0) {
			return null;
		}
		return exec.get(rowHeader);
	}
	
	/**
	 * 执行命令得到数据
	 * @param columns 每一行的列名
	 * @param cmd 执行的命令
	 * @return
	 */
	public static List<String> exec(String cmd) {
		Process process = null;
		BufferedReader br = null;
		try {
			process = Runtime.getRuntime().exec(cmd);
			br =new BufferedReader(new InputStreamReader(process.getInputStream(), Charset.forName("UTF-8")));
			List<String> dataLines = IOUtils.readLines(br);
			return dataLines;
		} catch (Exception e) {
			LOGGER.error("执行命令{}出错了!",cmd,e);
		}finally {
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
					LOGGER.error("关闭cmd 的io流出错了!",e);
				}
				process.destroy();
			}
		}
		return null;
	}
			
	/**
	 * 执行命令得到数据
	 * @param rowHeader 指定那几行为行头
	 * @param columns 每一行的列名
	 * @param cmd 执行的命令
	 * @param separator 一行的数据分隔符
	 * @return
	 */
	public static List<Map<String,Object>> exec(String[] columns,String cmd) {
		return exec(1, columns, cmd, DATA_REGEX);
	}

	/**
	 * 执行命令得到数据
	 * @param rowHeader 指定那几行为行头
	 * @param columns 每一行的列名
	 * @param cmd 执行的命令
	 * @param separator 一行的数据分隔符
	 * @return
	 */
	public static List<Map<String,Object>> exec(int rowHeader,String[] columns,String cmd,String separator) {
		List<String> dataLines = exec(cmd);
		if(dataLines == null || dataLines.size() == 0) {
			return null;
		}
		
		List<Map<String,Object>> table = new ArrayList<>();
		for (int rowNumber = 0; rowNumber < dataLines.size(); rowNumber++) {
			String rowString = dataLines.get(rowNumber);
			if(rowNumber< rowHeader) {
				continue;
			}
			
			String[] values = rowString.trim().split(separator);
			if(columns.length != values.length) {
				throw new RuntimeException( "给定的字段列的解析后的行的列的长度不一致");
			}
			
			Map<String,Object> row = new LinkedHashMap<String,Object>();
			for (int i = 0; i < values.length; i++) {
				row.put(columns[i], values[i]);
			}
			
			table.add(row);
		}
		return table;
	}
}
