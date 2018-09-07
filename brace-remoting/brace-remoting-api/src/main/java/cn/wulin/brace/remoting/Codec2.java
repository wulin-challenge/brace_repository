package cn.wulin.brace.remoting;

import java.io.IOException;

import cn.wulin.brace.remoting.buffer.ChannelBuffer;
import cn.wulin.ioc.Constants;
import cn.wulin.ioc.extension.Adaptive;
import cn.wulin.ioc.extension.SPI;

/**
 * @author <a href="mailto:gang.lvg@taobao.com">kimi</a>
 */
@SPI
public interface Codec2 {

    @Adaptive({Constants.CODEC_KEY})
    void encode(Channel channel, ChannelBuffer buffer, Object message) throws IOException;

    @Adaptive({Constants.CODEC_KEY})
    Object decode(Channel channel, ChannelBuffer buffer) throws IOException;


    enum DecodeResult {
        NEED_MORE_INPUT, SKIP_SOME_INPUT
    }

}

