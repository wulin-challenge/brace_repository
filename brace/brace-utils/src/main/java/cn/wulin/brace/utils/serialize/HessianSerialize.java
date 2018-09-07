package cn.wulin.brace.utils.serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

import cn.wulin.brace.api.serialize.Serializable;
import cn.wulin.brace.common.exception.BraceException;
import cn.wulin.ioc.URL;

@SuppressWarnings("unchecked")
public class HessianSerialize implements Serializable{

	@Override
	public <T> byte[] serialize(URL url, T obj) throws BraceException {
		if(obj==null) throw new NullPointerException();  
	      
	    ByteArrayOutputStream os = new ByteArrayOutputStream();  
	    Hessian2Output ho = new Hessian2Output(os);  
	    try {
			ho.writeObject(obj);
		} catch (Exception e) {
			throw new BraceException(e.getMessage(), e);
		}  
	    return os.toByteArray();  
	}

	@Override
	public <T> T deserialize(URL url, byte[] data, Class<T> clazz) throws BraceException {
		if(data==null) throw new NullPointerException();  
	      
	    ByteArrayInputStream is = new ByteArrayInputStream(data);  
	    Hessian2Input hi = new Hessian2Input(is);  
	    try {
			return (T) hi.readObject();
		} catch (Exception e) {
			throw new BraceException(e.getMessage(), e);
		}  
	}

}
