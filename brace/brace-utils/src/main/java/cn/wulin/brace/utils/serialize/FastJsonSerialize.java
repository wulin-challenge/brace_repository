package cn.wulin.brace.utils.serialize;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

import cn.wulin.brace.api.serialize.Serializable;
import cn.wulin.brace.common.exception.BraceException;
import cn.wulin.ioc.URL;

/**
 * 优势:对象无需实现 Serializable接口
 * 劣势:序列化效率一般
 * JSON序列化注意对枚举类型的特殊处理；额外补充类名可以在反序列化时获得更丰富的信息。
 * 参考文章:
 * http://www.iocoder.cn/RPC/laoxu/rpc-serialize-2/
 * @author wubo
 *
 */
public class FastJsonSerialize implements Serializable{
	private static final String charsetName = "UTF-8";

	@Override
	public <T> byte[] serialize(URL url, T obj) throws BraceException {
		SerializeWriter out = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(out);
        serializer.config(SerializerFeature.WriteEnumUsingToString, true);//<1>
        serializer.config(SerializerFeature.WriteClassName, true);//<1>
        serializer.write(obj);
        return out.toBytes(charsetName);
	}

	@Override
	public <T> T deserialize(URL url, byte[] data, Class<T> clazz) throws BraceException {
		return JSON.parseObject(new String(data), clazz);
	}

}
