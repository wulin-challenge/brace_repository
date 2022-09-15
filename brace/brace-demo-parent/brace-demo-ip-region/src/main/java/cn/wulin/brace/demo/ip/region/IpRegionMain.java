package cn.wulin.brace.demo.ip.region;

import java.io.IOException;
import java.util.Map;

import net.ipip.ipdb.City;
import net.ipip.ipdb.CityInfo;

public class IpRegionMain {
	public static void main(String[] args) {
		new IpRegionMain().query();
	}

	private void query() {
		
		String path = Thread.currentThread().getContextClassLoader().getResource("data/ipipwanzheng.ipdb").getPath();
		 try {
			City db = new City(path);
			
			CityInfo findInfo = db.findInfo("117.162.178.133", "CN");
			
			Map<String, String> findMap = db.findMap("117.162.178.133", "CN");
			System.out.println(findInfo);
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
