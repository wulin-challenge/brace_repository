package cn.wulin.brace.remoting.serialize.hessian;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.wulin.brace.remoting.serialize.ObjectInput;
import cn.wulin.brace.remoting.serialize.ObjectOutput;
import cn.wulin.brace.remoting.serialize.Serialization;
import cn.wulin.ioc.URL;

/**
 * @author ding.lid
 */
public class Hessian2Serialization implements Serialization {

    public static final byte ID = 2;

    public byte getContentTypeId() {
        return ID;
    }

    public String getContentType() {
        return "x-application/hessian2";
    }

    public ObjectOutput serialize(URL url, OutputStream out) throws IOException {
        return new Hessian2ObjectOutput(out);
    }

    public ObjectInput deserialize(URL url, InputStream is) throws IOException {
        return new Hessian2ObjectInput(is);
    }

}