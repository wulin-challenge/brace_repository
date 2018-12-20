package cn.wulin.brace.utils.serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import cn.wulin.brace.api.serialize.Serializable;
import cn.wulin.brace.common.exception.BraceException;
import cn.wulin.ioc.URL;
import de.javakaffee.kryoserializers.KryoReflectionFactorySupport;

/**
 * 使用kryo作为序列化
 * 优势:对象无需实现 Serializable接口,序列化效率高
 * 参考文章
 * http://www.iocoder.cn/RPC/laoxu/rpc-serialize-1/
 * 注意:关于kryo序列化问题文章: http://www.360doc.com/content/18/0511/08/36490684_753005092.shtml
 * 解决问题的文章: https://github.com/magro/kryo-serializers
 * @author wubo
 *
 */
@SuppressWarnings("unchecked")
public class KryoSerialize implements Serializable{

	@Override
	public <T> byte[] serialize(URL url, T obj) throws BraceException {
		Kryo kryo = kryoLocal.get();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Output output = new Output(byteArrayOutputStream);//<1>
        kryo.writeClassAndObject(output, obj);//<2>
        output.close();
        return byteArrayOutputStream.toByteArray();
	}

	@Override
	public <T> T deserialize(URL url, byte[] data, Class<T> clazz) throws BraceException {
		Kryo kryo = kryoLocal.get();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        Input input = new Input(byteArrayInputStream);// <1>
        input.close();
        return (T) kryo.readClassAndObject(input);//<2>
	}
	
    private static final ThreadLocal<Kryo> kryoLocal = new ThreadLocal<Kryo>() {//<3>
        @Override
        protected Kryo initialValue() {
            Kryo kryo = new KryoReflectionFactorySupport();
            kryo.setReferences(true);//默认值为true,强调作用
            kryo.setRegistrationRequired(false);//默认值为false,强调作用
            return kryo;
        }
    };

}
