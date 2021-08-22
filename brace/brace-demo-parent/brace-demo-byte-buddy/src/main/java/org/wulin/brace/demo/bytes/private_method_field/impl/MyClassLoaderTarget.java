package org.wulin.brace.demo.bytes.private_method_field.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.lang.Pair;

/**
 * 这是要被修改的目录类
 * @author ThinkPad
 *
 */
class MyClassLoaderTarget {

	private  static String[] DRUID_CLASS_PREFIX = new String[]{"com.alibaba.druid.",
    		"io.seata.sqlparser.druid."};
	
	public void print() {
		for (String string : DRUID_CLASS_PREFIX) {
			print2(string);
		}
	}
	
	private void print2(String x) {
		System.out.println(x);
	}
	
	private static URL[] getDruidUrls(String path) {
        List<URL> urls = new ArrayList<>();
        try {
			urls.add(new URL("http://xxx"));
			urls.add(new URL(path));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
        return urls.toArray(new URL[0]);
    }
	
    static Pair<String[],URL[]> get() {
    	URL[] druidUrls = getDruidUrls("http://abc");
    	return new Pair<>(DRUID_CLASS_PREFIX,druidUrls);
    }
}


