package cn.wulin.brace.utils.serialize;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import cn.wulin.brace.api.serialize.Serializable;
import cn.wulin.brace.common.exception.BraceException;
import cn.wulin.ioc.URL;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.DefaultIdStrategy;
import io.protostuff.runtime.IdStrategy;
import io.protostuff.runtime.RuntimeSchema;

/**
 * 优势:对象无需实现 Serializable接口,序列化效率高
 * 异常:不能正取序列化数组中有null的问题
 * @author ThinkPad
 *
 */
@SuppressWarnings("unchecked")
public class ProtostuffSerialize implements Serializable{
	
	private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();
	private Objenesis objenesis = new ObjenesisStd(); // <2>

	@Override
	public <T> byte[] serialize(URL url, T obj) throws BraceException {
	   Class<T> cls = (Class<T>) obj.getClass();
       LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
       try {
           Schema<T> schema = getSchema(cls);
           return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
       } catch (Exception e) {
           throw new BraceException(e.getMessage(), e);
       } finally {
           buffer.clear();
       }
	}

	@Override
	public <T> T deserialize(URL url, byte[] data, Class<T> clazz) throws BraceException {
		 try {
            T message = objenesis.newInstance(clazz); // <1>
            Schema<T> schema = getSchema(clazz);
            ProtostuffIOUtil.mergeFrom(data, message, schema);
            return message;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
	}
	
    private static <T> Schema<T> getSchema(Class<T> cls) {
        Schema<T> schema = (Schema<T>) cachedSchema.get(cls);
        if (schema == null) {
        	DefaultIdStrategy defaultIdStrategy = new DefaultIdStrategy(IdStrategy.ALLOW_NULL_ARRAY_ELEMENT,null,0);
            schema = RuntimeSchema.createFrom(cls, Collections.emptySet(),defaultIdStrategy);
            cachedSchema.put(cls, schema);
        }
        return schema;
    }

}
