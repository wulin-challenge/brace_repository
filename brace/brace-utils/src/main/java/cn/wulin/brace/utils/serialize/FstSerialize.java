package cn.wulin.brace.utils.serialize;

import org.nustaq.serialization.FSTConfiguration;

import cn.wulin.brace.api.serialize.Serializable;
import cn.wulin.brace.common.exception.BraceException;
import cn.wulin.ioc.URL;

/**
 * 该序列化暂时只能用于测试,不能用于生产环境
 * @author wubo
 *
 */
@SuppressWarnings("unchecked")
public class FstSerialize implements Serializable{
	
	private FSTConfiguration configuration = FSTConfiguration
//			.createDefaultConfiguration();
			.createStructConfiguration();


	@Override
	public <T> byte[] serialize(URL url, T obj) throws BraceException {
		return configuration.asByteArray(obj);
	}

	@Override
	public <T> T deserialize(URL url, byte[] data, Class<T> clazz) throws BraceException {
		return (T) configuration.asObject(data);
	}

}
