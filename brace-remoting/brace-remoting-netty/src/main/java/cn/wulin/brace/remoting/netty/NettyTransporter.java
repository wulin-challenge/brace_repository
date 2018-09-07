package cn.wulin.brace.remoting.netty;

import cn.wulin.brace.remoting.ChannelHandler;
import cn.wulin.brace.remoting.Client;
import cn.wulin.brace.remoting.RemotingException;
import cn.wulin.brace.remoting.Server;
import cn.wulin.brace.remoting.Transporter;
import cn.wulin.ioc.URL;

/**
 * @author ding.lid
 */
public class NettyTransporter implements Transporter {

    public static final String NAME = "netty";

    public Server bind(URL url, ChannelHandler listener) throws RemotingException {
        return new NettyServer(url, listener);
    }

    public Client connect(URL url, ChannelHandler listener) throws RemotingException {
        return new NettyClient(url, listener);
    }

}