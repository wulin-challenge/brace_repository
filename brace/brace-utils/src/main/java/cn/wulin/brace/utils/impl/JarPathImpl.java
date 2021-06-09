package cn.wulin.brace.utils.impl;

import org.apache.commons.lang3.StringUtils;

import cn.wulin.brace.utils.PathUtil;
import cn.wulin.ioc.logging.Logger;
import cn.wulin.ioc.logging.LoggerFactory;

/**
 * 得到启动jar的实现类
 * @author wulin
 *
 */
public class JarPathImpl {
	private static final Logger LOGGER = LoggerFactory.getLogger(JarPathImpl.class);
	
	private Class<?> starterClass;
	
	
	public JarPathImpl(Class<?> starterClass) {
		super();
		this.starterClass = starterClass;
	}
	

	public JarPathImpl() {
		this(getMainClass());
	}
	
	/**
	 * 获取启动类
	 * @return
	 */
	public static Class<?> getMainClass(){
		Class<?> forName = null;
		try {
			String mainClassName = getMainClassName();
			forName = Class.forName(mainClassName);
		} catch (ClassNotFoundException e) {
			LOGGER.error("启动类没有找到",e);
		}
		return forName;
	}
	
	/**
	 * 获取启动类全限定名称
	 * @return
	 */
	public static String getMainClassName() {
		StackTraceElement[] stackTraceElements = new RuntimeException().getStackTrace();
		for (StackTraceElement stackTraceElement : stackTraceElements) {
			if ("main".equals(stackTraceElement.getMethodName())) {
				return stackTraceElement.getClassName();
			}
		}
		return "";
	}


	/**
	 * 得到jar文件
	 * <p>
	 * 注意:路径中的!的意思是: 这个感叹号意思是后面的路径不是实际存在的路径，可以理解是压缩的路径
	 * 
	 * @return
	 */
	public String getJarPath() {
		String jarPathOfMark = getJarPathOfMark();
		if (StringUtils.isNotBlank(jarPathOfMark)) {
			return jarPathOfMark;
		}

		String rootDirectory = getRootDirectory();
		String classpath = getClasspath();

		String jarSuffix = getJarSuffix(rootDirectory, classpath);
		String jarName = getJarName(jarSuffix);
		String jarPath = PathUtil.pathSplicing(rootDirectory, jarName);
		return jarPath;
	}

	/**
	 * 得到jar的名称
	 * <p> 主要的情况有 jarname.jar/xxx
	 * <p> jarname.jar
	 * @param jarSuffix
	 * @return
	 */
	private String getJarName(String jarSuffix) {
		if(StringUtils.isBlank(jarSuffix)) {
			throw new RuntimeException("没有获取到jar文件");
		}
		if(jarSuffix.contains("/")) {
			int index = jarSuffix.indexOf("/");
			return jarSuffix.substring(0, index);
		}
		return jarSuffix;
	}

	/**
	 * 过期jar文件前缀
	 * @param rootDirectory
	 * @param classpath
	 * @return
	 */
	private String getJarSuffix(String rootDirectory, String classpath) {
		int classPathIndex = classpath.indexOf(rootDirectory) + rootDirectory.length() + 1;
		String jarSuffix = classpath.substring(classPathIndex);
		return jarSuffix;
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
		String classpath = starterClass.getProtectionDomain().getCodeSource().getLocation().getPath();
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
	 * 采用虚拟路径进行判断
	 * 
	 * @return
	 */
	private String getJarPathOfMark() {
		String classpath = starterClass.getProtectionDomain().getCodeSource().getLocation().getPath();
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
