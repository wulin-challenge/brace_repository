package cn.wulin.brace.remoting.netty4;

import cn.wulin.brace.remoting.ChannelHandler;
import cn.wulin.brace.remoting.Client;
import cn.wulin.brace.remoting.RemotingException;
import cn.wulin.brace.remoting.Server;
import cn.wulin.brace.remoting.Transporter;
import cn.wulin.ioc.URL;

/**
 * @author ding.lid
 * @author qinliujie
 */
public class NettyTransporter implements Transporter {

    public static final String NAME = "netty4";
    
    public Server bind(URL url, ChannelHandler listener) throws RemotingException {
        return new NettyServer(url, listener);
    }

    public Client connect(URL url, ChannelHandler listener) throws RemotingException {
        return new NettyClient(url, listener);
    }

}