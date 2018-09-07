package cn.wulin.brace.remoting.transport.dispatcher.connection;

import cn.wulin.brace.remoting.ChannelHandler;
import cn.wulin.brace.remoting.Dispatcher;
import cn.wulin.ioc.URL;

/**
 * connect disconnect 保证顺序.
 *
 * @author chao.liuc
 */
public class ConnectionOrderedDispatcher implements Dispatcher {

    public static final String NAME = "connection";

    public ChannelHandler dispatch(ChannelHandler handler, URL url) {
        return new ConnectionOrderedChannelHandler(handler, url);
    }

}