package cn.wulin.brace.remoting.transport;

import cn.wulin.brace.remoting.Channel;
import cn.wulin.brace.remoting.ChannelHandler;
import cn.wulin.brace.remoting.RemotingException;

/**
 * ChannelHandlerAdapter.
 *
 * @author qian.lei
 */
public class ChannelHandlerAdapter implements ChannelHandler {

    public void connected(Channel channel) throws RemotingException {
    }

    public void disconnected(Channel channel) throws RemotingException {
    }

    public void sent(Channel channel, Object message) throws RemotingException {
    }

    public void received(Channel channel, Object message) throws RemotingException {
    }

    public void caught(Channel channel, Throwable exception) throws RemotingException {
    }

}