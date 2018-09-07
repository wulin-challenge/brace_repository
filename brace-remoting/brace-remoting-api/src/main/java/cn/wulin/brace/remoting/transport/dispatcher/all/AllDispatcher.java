package cn.wulin.brace.remoting.transport.dispatcher.all;

import cn.wulin.brace.remoting.ChannelHandler;
import cn.wulin.brace.remoting.Dispatcher;
import cn.wulin.ioc.URL;

/**
 * 默认的线程池配置
 *
 * @author chao.liuc
 */
public class AllDispatcher implements Dispatcher {

    public static final String NAME = "all";

    public ChannelHandler dispatch(ChannelHandler handler, URL url) {
        return new AllChannelHandler(handler, url);
    }

}