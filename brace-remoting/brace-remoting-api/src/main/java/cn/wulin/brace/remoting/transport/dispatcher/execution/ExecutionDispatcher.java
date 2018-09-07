package cn.wulin.brace.remoting.transport.dispatcher.execution;

import cn.wulin.brace.remoting.ChannelHandler;
import cn.wulin.brace.remoting.Dispatcher;
import cn.wulin.ioc.URL;

/**
 * 除发送全部使用线程池处理
 *
 * @author chao.liuc
 */
public class ExecutionDispatcher implements Dispatcher {

    public static final String NAME = "execution";

    public ChannelHandler dispatch(ChannelHandler handler, URL url) {
        return new ExecutionChannelHandler(handler, url);
    }

}