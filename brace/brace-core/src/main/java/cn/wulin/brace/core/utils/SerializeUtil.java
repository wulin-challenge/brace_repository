package cn.wulin.brace.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.wulin.brace.api.serialize.Serializable;
import cn.wulin.brace.common.exception.BraceException;
import cn.wulin.ioc.URL;
import cn.wulin.ioc.extension.InterfaceExtensionLoader;

public class SerializeUtil {
	
	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(SerializeUtil.class);
	/**
     * 序列化（对象 -> 字节数组）
     */
    public static <T> byte[] serialize(T obj){
    	return serialize(obj,null);
    }
	/**
     * 序列化（对象 -> 字节数组）
     */
    public static <T> byte[] serialize(T obj,String protocol) {
    	Serializable seri = InterfaceExtensionLoader.getExtensionLoader(Serializable.class).getAdaptiveExtension();
    	try {
			return seri.serialize(getUrl(protocol,obj.getClass()), obj);
		} catch (BraceException e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    /**
     * 反序列化（字节数组 -> 对象）
     */
    public static <T> T deserialize(byte[] data, Class<T> clazz){
    	return deserialize(data, clazz,null);
    }

    /**
     * 反序列化（字节数组 -> 对象）
     */
    public static <T> T deserialize(byte[] data, Class<T> clazz,String protocol) {
    	Serializable seri = InterfaceExtensionLoader.getExtensionLoader(Serializable.class).getAdaptiveExtension();
    	try {
			return seri.deserialize(getUrl(protocol,clazz), data, clazz);
		} catch (BraceException e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    private static <T> URL getUrl(String protocol,Class<T> clazz){
    	URL url = new URL(protocol,"0.0.0.0",0000);
    	return url;
    }
}
