package cn.wulin.brace.demo.starter.jar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import cn.wulin.brace.utils.PathUtil;

/**
 * 未完成
 * 
 * @author ThinkPad
 *
 */
public class TestReadCurrentJar {

	public static void main(String[] args) {
		String jarPath = new TestReadCurrentJar().getJarPath();
		System.out.println("jar文件的路径---------------------------");
		System.out.println(jarPath);
	}

	/**
	 * 得到启动项目的所在根目录
	 * 
	 * @return
	 */
	private String getRootDirectory() {
		String rootDir = System.getProperty("user.dir");
		return PathUtil.replaceSprit(replaceMark(rootDir));
	}

	/**
	 * 得到启动项目的classpath路径
	 * <p>
	 * 注意:路径中的!的意思是: 这个感叹号意思是后面的路径不是实际存在的路径，可以理解是压缩的路径
	 * 
	 * @return
	 */
	private String getClasspath() {
		String classpath = Thread.currentThread().getContextClassLoader().getResource("").getPath();

		System.out.println("claspath1: " + classpath);
		return PathUtil.replaceSprit(replaceMark(classpath));
	}

	/**
	 * 替换感叹号标记
	 * 
	 * @return
	 */
	private String replaceMark(String path) {
		return path.replace("!", "");
	}

	/**
	 * 得到jar文件
	 * <p>
	 * 注意:路径中的!的意思是: 这个感叹号意思是后面的路径不是实际存在的路径，可以理解是压缩的路径
	 * 
	 * @return
	 */
	private String getJarPath() {
		String jarPathOfMark = getJarPathOfMark();
		if (StringUtils.isNotBlank(jarPathOfMark)) {
			return jarPathOfMark;
		}

		String rootDirectory = getRootDirectory();
		String classpath = getClasspath();

		int classPathIndex = classpath.indexOf(rootDirectory) + rootDirectory.length() + 1;
		String jarSuffix = classpath.substring(classPathIndex);

		int index = jarSuffix.indexOf("/");
		String jarName = jarSuffix.substring(0, index);
		String jarPath = PathUtil.pathSplicing(rootDirectory, jarName);
		return jarPath;
	}

	/**
	 * 采用虚拟路径进行判断
	 * 
	 * @return
	 */
	private String getJarPathOfMark() {
		String classpath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		classpath = PathUtil.replaceSprit(classpath);
		
		if (!classpath.contains("!")) {
			return null;
		}
		
		if(classpath.startsWith("file:/")) {
			if (System.getProperty("os.name").contains("dows")) {
				classpath = classpath.replaceFirst("file:/", "");
			}else {
				classpath = classpath.replaceFirst("file:/", "/");
			}
			
		}else {
			if (System.getProperty("os.name").contains("dows")) {
				classpath = classpath.substring(1, classpath.length());
			}
		}
		
		int index = classpath.indexOf("!");
		return classpath.substring(0, index);
	}

}
