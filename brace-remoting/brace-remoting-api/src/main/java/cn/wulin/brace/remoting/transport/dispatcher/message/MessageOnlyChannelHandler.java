package cn.wulin.brace.remoting.transport.dispatcher.message;

import java.util.concurrent.ExecutorService;

import cn.wulin.brace.remoting.Channel;
import cn.wulin.brace.remoting.ChannelHandler;
import cn.wulin.brace.remoting.ExecutionException;
import cn.wulin.brace.remoting.RemotingException;
import cn.wulin.brace.remoting.transport.dispatcher.ChannelEventRunnable;
import cn.wulin.brace.remoting.transport.dispatcher.ChannelEventRunnable.ChannelState;
import cn.wulin.brace.remoting.transport.dispatcher.WrappedChannelHandler;
import cn.wulin.ioc.URL;

public class MessageOnlyChannelHandler extends WrappedChannelHandler {

    public MessageOnlyChannelHandler(ChannelHandler handler, URL url) {
        super(handler, url);
    }

    public void received(Channel channel, Object message) throws RemotingException {
        ExecutorService cexecutor = executor;
        if (cexecutor == null || cexecutor.isShutdown()) {
            cexecutor = SHARED_EXECUTOR;
        }
        try {
            cexecutor.execute(new ChannelEventRunnable(channel, handler, ChannelState.RECEIVED, message));
        } catch (Throwable t) {
            throw new ExecutionException(message, channel, getClass() + " error when process received event .", t);
        }
    }

}