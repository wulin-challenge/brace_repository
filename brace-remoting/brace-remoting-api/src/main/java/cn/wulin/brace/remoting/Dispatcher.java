package cn.wulin.brace.remoting;

import cn.wulin.brace.remoting.transport.dispatcher.all.AllDispatcher;
import cn.wulin.ioc.Constants;
import cn.wulin.ioc.URL;
import cn.wulin.ioc.extension.Adaptive;
import cn.wulin.ioc.extension.SPI;

/**
 * ChannelHandlerWrapper (SPI, Singleton, ThreadSafe)
 *
 * @author chao.liuc
 */
@SPI(AllDispatcher.NAME)
public interface Dispatcher {

    /**
     * dispatch the message to threadpool.
     *
     * @param handler
     * @param url
     * @return channel handler
     */
    @Adaptive({Constants.DISPATCHER_KEY, "dispather", "channel.handler"})
    // 后两个参数为兼容旧配置
    ChannelHandler dispatch(ChannelHandler handler, URL url);

}