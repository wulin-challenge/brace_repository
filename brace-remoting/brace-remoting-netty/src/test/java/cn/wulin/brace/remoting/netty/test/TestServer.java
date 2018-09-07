package cn.wulin.brace.remoting.netty.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cn.wulin.brace.remoting.Channel;
import cn.wulin.brace.remoting.RemotingException;
import cn.wulin.brace.remoting.exchange.ExchangeChannel;
import cn.wulin.brace.remoting.exchange.ExchangeHandler;
import cn.wulin.brace.remoting.exchange.ExchangeServer;
import cn.wulin.brace.remoting.exchange.Exchangers;
import cn.wulin.brace.remoting.telnet.support.TelnetUtils;
import cn.wulin.brace.remoting.util.NamedThreadFactory;
import cn.wulin.ioc.URL;

public class TestServer {
	// 定时任务执行器
    private static final ScheduledExecutorService retryExecutor = Executors.newScheduledThreadPool(1, new NamedThreadFactory("test-scheduled", true));
	
    public static void main(String[] args) {
		URL url = new URL("brace", "localhost", 9999);
		try {
			ExchangeServer server = Exchangers.bind(url, new TestServerExchangeHandler());
			
			TestFullSendScheduled command = new TestFullSendScheduled(server);
			retryExecutor.scheduleWithFixedDelay(command, 0, 5, TimeUnit.SECONDS);
			System.in.read();
		} catch (RemotingException | IOException e) {
			e.printStackTrace();
		}
	}
    
    private static class TestFullSendScheduled implements Runnable{
    	final ExchangeServer server;
    	
		public TestFullSendScheduled(ExchangeServer server) {
			super();
			this.server = server;
		}

		@Override
		public void run() {
			Collection<Channel> channels = server.getChannels();
			for (Channel channel : channels) {
				try {
					channel.send("\r\nh我我的扣扣aha");
				} catch (RemotingException e) {
					e.printStackTrace();
				}
			}
			
		}
    }

	private static class TestServerExchangeHandler implements ExchangeHandler{

		@Override
		public void connected(Channel channel) throws RemotingException {
			System.out.println("connected");
		}

		@Override
		public void disconnected(Channel channel) throws RemotingException {
			System.out.println("disconnected");
			
		}

		@Override
		public void sent(Channel channel, Object message) throws RemotingException {
			System.out.println("sent");
			
		}

		@Override
		public void received(Channel channel, Object message) throws RemotingException {
			System.out.println("received");
			
		}

		@Override
		public void caught(Channel channel, Throwable exception) throws RemotingException {
			System.out.println("caught");
			
		}

		@Override
		public String telnet(Channel channel, String message) throws RemotingException {
			List<String> s1 = Arrays.asList(new String[]{"1","2","3"});
			List<String> s2 = Arrays.asList(new String[]{"4","555","6"});
			List<String> s3 = Arrays.asList(new String[]{"a","b","c"});
			List<List<String>> table = new ArrayList<List<String>>();
			table.add(s1);
			table.add(s2);
			table.add(s3);
			
//			String list = TelnetUtils.toList(table);
			String list = TelnetUtils.toTable(new String[]{"AA","B","C C"}, table);
			
			System.out.println("telnet");
			channel.send(list+ "\r\n输入的信息:"+message+"\r\nbrace>");
			return null;
		}

		@Override
		public Object reply(ExchangeChannel channel, Object request) throws RemotingException {
			System.out.println("reply");
			return null;
		}
		
	}
}
