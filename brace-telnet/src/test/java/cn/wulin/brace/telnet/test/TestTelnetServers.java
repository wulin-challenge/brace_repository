package cn.wulin.brace.telnet.test;

import java.io.IOException;

import cn.wulin.brace.remoting.RemotingException;
import cn.wulin.brace.telnet.TelnetServers;

public class TestTelnetServers {
	
	public static void main(String[] args) throws RemotingException, IOException {
		TelnetServers.bind();
		System.out.println("启动成功!");
		System.in.read();
	}

}
