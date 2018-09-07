package cn.wulin.brace.utils.serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cn.wulin.brace.api.serialize.Serializable;
import cn.wulin.brace.common.exception.BraceException;
import cn.wulin.ioc.URL;

@SuppressWarnings("unchecked")
public class JdkSerialize implements Serializable{

	@Override
	public <T> byte[] serialize(URL url, T obj) throws BraceException{
	   if(obj==null) throw new NullPointerException();  
	      
	    ByteArrayOutputStream os = new ByteArrayOutputStream();  
	    ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(os);
			out.writeObject(obj);  
		} catch (Exception e) {
			throw new BraceException(e.getMessage(), e);
		}  
	    return os.toByteArray(); 
	}

	@Override
	public <T> T deserialize(URL url, byte[] data, Class<T> clazz) throws BraceException {
		if(data==null) throw new NullPointerException();  
	      
	    ByteArrayInputStream is = new ByteArrayInputStream(data);  
	    ObjectInputStream in;
		try {
			in = new ObjectInputStream(is);
			return (T) in.readObject();  
		} catch (Exception e) {
			throw new BraceException(e.getMessage(), e);
		}  
	}

}
