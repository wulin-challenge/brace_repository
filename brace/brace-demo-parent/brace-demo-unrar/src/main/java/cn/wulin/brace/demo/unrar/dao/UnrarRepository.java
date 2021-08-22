package cn.wulin.brace.demo.unrar.dao;

import java.io.File;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;

import cn.wulin.brace.demo.unrar.domain.Crack;

/**
 * 百度分享存储器
 * @author wulin
 *
 */
public class UnrarRepository {
	private final static Logger LOGGER = LoggerFactory.getLogger(UnrarRepository.class);
	
	/**
	 * 存储前缀
	 */
	private final static String STORE_SUFFIX = System.getProperty("user.dir")+"/store/unrar/";
	
	/**
	 * 缓存
	 * <p> key: 破解密码
	 * <p> value: 是否是破解密码
	 */
	private static final ConcurrentMap<String,Boolean> CACHE = new ConcurrentHashMap<>();
	
	/**
	 * json存储器
	 */
	private static final JsonLocalStore<ConcurrentMap<String,Boolean>> JSON_STORE = new JsonLocalStore<>();
	
	public static UnrarRepository unrarRepository = new UnrarRepository();
	
	private UnrarRepository() {
	}
	
	public static UnrarRepository getInstance() {
		return unrarRepository;
	}
	/**
	 * 保存或者更新
	 * @param Crack
	 * @param writeLocal 是否写到本地,true写到本地,也写到内存,false:只写到内存
	 */
	public void saveOrUpdate(Crack crack,Boolean writeLocal,Boolean writeCache) {
		if(writeCache) {
			CACHE.put(crack.getPassword(), crack.getCrack());
		}
		if(writeLocal) {
			JSON_STORE.writeJsonLine(getStoreFile(crack), crack,true);
		}
	}
	
	/**
	 * 是否包含这个密码
	 * @param pwd
	 * @return
	 */
	public boolean contains(String pwd) {
		return CACHE.containsKey(pwd);
	}
	
	/**
	 * 从本地初始化到内存
	 * @param filePath
	 */
	public void initCacheFromLocal(String filePath) {
		List<String> lineList = JSON_STORE.readJsonList(getStoreFile(filePath));
		if(lineList == null || lineList.size() == 0) {
			return;
		}
		for (String jsonSting : lineList) {
			Crack crack = JSON.parseObject(jsonSting,Crack.class);
			if(crack != null) {
				saveOrUpdate(crack, false,true);
			}
		}
	}
	
	/**
	 * 得到存储的文件
	 * @param crack
	 * @return
	 */
	private File getStoreFile(String filePath) {
		Crack crack = new Crack("", filePath);
		return new File(STORE_SUFFIX,getKey(crack)+".json");
	}
	
	/**
	 * 得到存储的文件
	 * @param crack
	 * @return
	 */
	private File getStoreFile(Crack crack) {
		return new File(STORE_SUFFIX,getKey(crack)+".json");
	}
	
	private String getKey(Crack crack) {
		String fullName = new File(crack.getFilePath()).getName();
		String[] split = fullName.split("\\.");
		return split[0];
	}
}
