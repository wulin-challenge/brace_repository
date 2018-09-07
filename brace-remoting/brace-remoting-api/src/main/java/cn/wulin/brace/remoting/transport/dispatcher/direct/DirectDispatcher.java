package cn.wulin.brace.remoting.transport.dispatcher.direct;

import cn.wulin.brace.remoting.ChannelHandler;
import cn.wulin.brace.remoting.Dispatcher;
import cn.wulin.ioc.URL;

/**
 * 不派发线程池。
 *
 * @author chao.liuc
 */
public class DirectDispatcher implements Dispatcher {

    public static final String NAME = "direct";

    public ChannelHandler dispatch(ChannelHandler handler, URL url) {
        return handler;
    }

}