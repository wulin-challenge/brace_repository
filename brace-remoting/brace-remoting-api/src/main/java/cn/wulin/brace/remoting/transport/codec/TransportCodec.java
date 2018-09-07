package cn.wulin.brace.remoting.transport.codec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.wulin.brace.remoting.Channel;
import cn.wulin.brace.remoting.buffer.ChannelBuffer;
import cn.wulin.brace.remoting.buffer.ChannelBufferInputStream;
import cn.wulin.brace.remoting.buffer.ChannelBufferOutputStream;
import cn.wulin.brace.remoting.serialize.ObjectInput;
import cn.wulin.brace.remoting.serialize.ObjectOutput;
import cn.wulin.brace.remoting.transport.AbstractCodec;
import cn.wulin.ioc.util.StringUtils;

/**
 * TransportCodec
 *
 * @author william.liangf
 */
public class TransportCodec extends AbstractCodec {

    public void encode(Channel channel, ChannelBuffer buffer, Object message) throws IOException {
        OutputStream output = new ChannelBufferOutputStream(buffer);
        ObjectOutput objectOutput = getSerialization(channel).serialize(channel.getUrl(), output);
        encodeData(channel, objectOutput, message);
        objectOutput.flushBuffer();
    }

    public Object decode(Channel channel, ChannelBuffer buffer) throws IOException {
        InputStream input = new ChannelBufferInputStream(buffer);
        return decodeData(channel, getSerialization(channel).deserialize(channel.getUrl(), input));
    }

    protected void encodeData(Channel channel, ObjectOutput output, Object message) throws IOException {
        encodeData(output, message);
    }

    protected Object decodeData(Channel channel, ObjectInput input) throws IOException {
        return decodeData(input);
    }

    protected void encodeData(ObjectOutput output, Object message) throws IOException {
        output.writeObject(message);
    }

    protected Object decodeData(ObjectInput input) throws IOException {
        try {
            return input.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("ClassNotFoundException: " + StringUtils.toString(e));
        }
    }
}