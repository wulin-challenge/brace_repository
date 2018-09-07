package cn.wulin.brace.api.serialize;

import cn.wulin.brace.common.exception.BraceException;
import cn.wulin.ioc.URL;
import cn.wulin.ioc.extension.Adaptive;
import cn.wulin.ioc.extension.SPI;

@SPI("kryo")
public interface Serializable {

	/**
	 * 序列化对象为二进制
	 * @param url 贯穿整个上下文的url
	 * @param obj 要进行序列化的对象
	 * @return 返回序列化对象的二进制
	 */
	@Adaptive("protocol")
	public <T> byte[] serialize(URL url, T obj) throws BraceException;

	/**
	 * 反序列化二进制为对象
	 * @param url 贯穿整个上下文的url
	 * @param data 二进制对象
	 * @param clazz 要反序列化的对象
	 * @return 返回反序列化二进制的对象
	 */
	@Adaptive("protocol")
	public <T> T deserialize(URL url, byte[] data, Class<T> clazz) throws BraceException;
}
