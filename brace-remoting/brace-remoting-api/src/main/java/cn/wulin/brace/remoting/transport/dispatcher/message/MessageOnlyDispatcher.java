package cn.wulin.brace.remoting.transport.dispatcher.message;

import cn.wulin.brace.remoting.ChannelHandler;
import cn.wulin.brace.remoting.Dispatcher;
import cn.wulin.ioc.URL;

/**
 * 只有message receive使用线程池.
 *
 * @author chao.liuc
 */
public class MessageOnlyDispatcher implements Dispatcher {

    public static final String NAME = "message";

    public ChannelHandler dispatch(ChannelHandler handler, URL url) {
        return new MessageOnlyChannelHandler(handler, url);
    }

}