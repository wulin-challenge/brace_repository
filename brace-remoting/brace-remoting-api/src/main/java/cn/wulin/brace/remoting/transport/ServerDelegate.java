package cn.wulin.brace.remoting.transport;

import java.net.InetSocketAddress;
import java.util.Collection;

import cn.wulin.brace.remoting.Channel;
import cn.wulin.brace.remoting.ChannelHandler;
import cn.wulin.brace.remoting.RemotingException;
import cn.wulin.brace.remoting.Server;
import cn.wulin.ioc.URL;

/**
 * ServerDelegate
 *
 * @author william.liangf
 */
public class ServerDelegate implements Server {

    private transient Server server;

    public ServerDelegate() {
    }

    public ServerDelegate(Server server) {
        setServer(server);
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public boolean isBound() {
        return server.isBound();
    }

    public void reset(URL url) {
        server.reset(url);
    }

    public Collection<Channel> getChannels() {
        return server.getChannels();
    }

    public Channel getChannel(InetSocketAddress remoteAddress) {
        return server.getChannel(remoteAddress);
    }

    public URL getUrl() {
        return server.getUrl();
    }

    public ChannelHandler getChannelHandler() {
        return server.getChannelHandler();
    }

    public InetSocketAddress getLocalAddress() {
        return server.getLocalAddress();
    }

    public void send(Object message) throws RemotingException {
        server.send(message);
    }

    public void send(Object message, boolean sent) throws RemotingException {
        server.send(message, sent);
    }

    public void close() {
        server.close();
    }

    public void close(int timeout) {
        server.close(timeout);
    }

    @Override
    public void startClose() {
        server.startClose();
    }

    public boolean isClosed() {
        return server.isClosed();
    }
}