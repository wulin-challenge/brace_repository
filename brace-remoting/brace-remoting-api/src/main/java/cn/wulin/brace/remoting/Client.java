package cn.wulin.brace.remoting;


/**
 * Remoting Client. (API/SPI, Prototype, ThreadSafe)
 * <p>
 * <a href="http://en.wikipedia.org/wiki/Client%E2%80%93server_model">Client/Server</a>
 *
 * @author qian.lei
 * @see com.alibaba.dubbo.remoting.Transporter#connect(com.alibaba.dubbo.common.URL, ChannelHandler)
 */
public interface Client extends Endpoint, Channel, Resetable {

    /**
     * reconnect.
     */
    void reconnect() throws RemotingException;


}