package cn.wulin.brace.demo.unrar.dao;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wulin.basic.commons.thread.ThreadFactoryImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.wulin.brace.utils.PathUtil;

/**
 * json本地存储实现
 * @author wulin
 *
 */
public class JsonLocalStore<T> {
	private final static Logger LOGGER = LoggerFactory.getLogger(JsonLocalStore.class);
	
	/**
	 * 只能采用单线程
	 */
	private final static ExecutorService EXECUTORS = Executors.newFixedThreadPool(1, new ThreadFactoryImpl("JsonLocalStore"));
	
	private final static ScheduledExecutorService SCHUDEL_EXECUTORS = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryImpl("JsonLocalStore_schudel"));

	private static final ConcurrentMap<String, CopyOnWriteArraySet<Object>> CACHE_DATA = new ConcurrentHashMap<>();

	/**
	 * 是否进行异步写
	 */
	private Boolean asyncWrite = true;
	
	public JsonLocalStore(Boolean asyncWrite) {
		this.asyncWrite = asyncWrite;
		SCHUDEL_EXECUTORS.scheduleWithFixedDelay(()->{
			
			Set<String> keySet = CACHE_DATA.keySet();
			if(keySet == null) {
				return;
			}
			for (String filePath : keySet) {
				Set<Object> dataSet = CACHE_DATA.get(filePath);
				List<Object> dataList = new ArrayList<>(dataSet);
				dataSet.removeAll(dataList);
				writeLine(new File(filePath), dataList, true);
			}
		}, 5, 10, TimeUnit.SECONDS);
	}
	
	public JsonLocalStore() {
		this(true);
	}

	/**
	 * 
	 * @param jsonFile
	 * @param data
	 * @return 异步写返回值始终为true
	 */
	public boolean writeJson(File jsonFile,Object data) {
		if(asyncWrite) {
			EXECUTORS.submit(()->{
				write(jsonFile,data);
			});
			return true;
		}
		return write(jsonFile,data);
	}
	
	/**
	 * 
	 * @param jsonFile
	 * @param data
	 * @return 异步写返回值始终为true
	 */
	public boolean writeJsonLine(File jsonFile,Object data,boolean append) {
		if(asyncWrite) {
			try {
				
				CopyOnWriteArraySet<Object> dataSet = CACHE_DATA.get(jsonFile.getPath());
				
				if(dataSet == null) {
					synchronized (this) {
						dataSet = CACHE_DATA.get(jsonFile.getPath());
						if(dataSet == null) {
							dataSet = new CopyOnWriteArraySet<>();
							CACHE_DATA.put(jsonFile.getPath(), dataSet);
						}
					}
				}
				dataSet.add(data);
			} catch (Exception e) {
				LOGGER.error("写入队列失败",e);
			}
			
			return true;
		}
		return writeLine(jsonFile,data,true);
	}
	
	private boolean write(File jsonFile,Object data) {
		String json = JSON.toJSONString(data);
		try {
			synchronized (this) {
				FileUtils.writeStringToFile(jsonFile, json, Charset.forName("UTF-8"));
			}
			return true;
		} catch (Exception e) {
			LOGGER.error("将json写入{}文件失败",jsonFile.getPath(),e);
		}
		return false;
	}
	
	private boolean writeLine(File jsonFile,Object data,boolean append) {
		String json = JSON.toJSONString(data);
		List<Object> lines = new ArrayList<>();
		lines.add(json);
		try {
			synchronized (this) {
				FileUtils.writeLines(jsonFile, "UTF-8",lines, append);
			}
			return true;
		} catch (Exception e) {
			LOGGER.error("将json写入{}文件失败",jsonFile.getPath(),e);
		}
		return false;
	}
	
	private boolean writeLine(File jsonFile,List<Object> dataList,boolean append) {
		if(dataList == null || dataList.size() == 0) {
			return false;
		}
		List<String> lines = new ArrayList<>();
		for (Object object : dataList) {
			String json = JSON.toJSONString(object);
			lines.add(json);
		}
		
		try {
			synchronized (this) {
				FileUtils.writeLines(jsonFile, "UTF-8",lines, append);
			}
			return true;
		} catch (Exception e) {
			LOGGER.error("将json写入{}文件失败",jsonFile.getPath(),e);
		}
		return false;
	}
	
	public List<T> readJsonList(String parentPaht,String fileName,Class<T> clazz) {
		return readJsonList(PathUtil.pathSplicing(parentPaht, fileName), clazz);
	}
	
	public List<T> readJsonList(String jsonPah,Class<T> clazz) {
		File jsonFile = new File(jsonPah);
		return readJsonList(jsonFile, clazz);
	}
	
	public List<T> readJsonList(File jsonFile,Class<T> clazz) {
		String json = readJsonString(jsonFile);
		
		List<T> parseArray = JSON.parseArray(json, clazz);
		return parseArray;
	}
	
	public T readJsonObject(String parentPaht,String fileName,Class<T> clazz) {
		return readJsonObject(PathUtil.pathSplicing(parentPaht, fileName), clazz);
	}
	
	public T readJsonObject(String jsonPah,Class<T> clazz) {
		File jsonFile = new File(jsonPah);
		return readJsonObject(jsonFile, clazz);
	}
	
	public T readJsonObject(File jsonFile,Class<T> clazz) {
		String json = readJsonString(jsonFile);
		return JSON.parseObject(json,clazz);
	}

	public JSONObject readJsonObject(String parentPaht,String fileName) {
		return readJsonObject(PathUtil.pathSplicing(parentPaht, fileName));
	}
	
	public JSONObject readJsonObject(String jsonPah) {
		File jsonFile = new File(jsonPah);
		return readJsonObject(jsonFile);
	}
	
	public JSONObject readJsonObject(File jsonFile) {
		String json = readJsonString(jsonFile);
		return JSON.parseObject(json);
	}
	
	public String readJsonString(String parentPaht,String fileName) {
		return readJsonString(PathUtil.pathSplicing(parentPaht, fileName));
	}
	
	public String readJsonString(String jsonPah) {
		File jsonFile = new File(jsonPah);
		return readJsonString(jsonFile);
	}
	
	public String readJsonString(File jsonFile) {
		if(!jsonFile.exists() || !jsonFile.isFile()) {
			return null;
		}
		
		try {
			String json = FileUtils.readFileToString(jsonFile,Charset.forName("UTF-8"));
			return json;
		} catch (Exception e) {
			LOGGER.error("读取{}json文件失败",jsonFile.getPath(),e);
		}
		return null;
	}
	
	public List<String> readJsonList(File jsonFile) {
		if(!jsonFile.exists() || !jsonFile.isFile()) {
			return null;
		}
		try {
			List<String> readLines = FileUtils.readLines(jsonFile,Charset.forName("UTF-8"));
			return readLines;
		} catch (Exception e) {
			LOGGER.error("读取{}json文件失败",jsonFile.getPath(),e);
		}
		return null;
	}
	
	
}
