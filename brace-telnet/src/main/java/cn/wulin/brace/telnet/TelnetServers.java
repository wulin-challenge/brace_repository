package cn.wulin.brace.telnet;

import java.util.Collection;

import cn.wulin.brace.remoting.Channel;
import cn.wulin.brace.remoting.RemotingException;
import cn.wulin.brace.remoting.exchange.ExchangeHandler;
import cn.wulin.brace.remoting.exchange.ExchangeServer;
import cn.wulin.brace.remoting.exchange.Exchangers;
import cn.wulin.ioc.URL;
import cn.wulin.ioc.util.ConfigUtils;
import cn.wulin.ioc.util.NetUtils;
import cn.wulin.ioc.util.StringUtils;

public class TelnetServers {
	private static ExchangeServer server;
	
	public static void bind() throws RemotingException{
		bind(getDefualtUrl(),getExchangeHandler());
	}
	
	public static void bind(ExchangeHandler handler) throws RemotingException{
		bind(getDefualtUrl(), handler);
	}
	
	public static void bind(URL url,ExchangeHandler handler) throws RemotingException{
		//设置主机host
		String telnetHost = ConfigUtils.getProperty(TelnetConstants.TELNET_HOST_KEY);
		telnetHost = StringUtils.isBlank(telnetHost)?url.getHost():telnetHost;
		//设置主机port
		String telnetPortStr = ConfigUtils.getProperty(TelnetConstants.TELNET_PORT_KEY);
		int telnetPort = StringUtils.isBlank(telnetPortStr)?url.getPort():Integer.parseInt(telnetPortStr);
		//构建url
		url = new URL(TelnetConstants.TELNET_PROTOCOL_KEY, telnetHost, telnetPort);
		//使用netty4作为通信框架
		url = url.addParameter("server", "netty4");
		server = Exchangers.bind(url, handler);
	}
	
	/**
	 * 发送telnet消息
	 * @param message
	 * @throws RemotingException
	 */
	public static void sendTelnetMassage(Object message) throws RemotingException{
		if(server == null){
			return;
		}
		Collection<Channel> channels = server.getChannels();
		if(channels != null && channels.size()>0){
			for (Channel channel : channels) {
				channel.send(message);
			}
		}
	}
	
	public static ExchangeServer getExchangeServer(){
		return server;
	}
	
	private static URL getDefualtUrl(){
		String telnetHost = NetUtils.getLocalHost();
		URL url = new URL(TelnetConstants.TELNET_PROTOCOL_KEY, telnetHost, TelnetConstants.TELNET_DEFUALT_PORT);
		return url;
	}
	
	private static ExchangeHandler getExchangeHandler(){
		ExchangeHandler handler= new TelnetExchangeHandler();
		return handler;
	}
	
}
