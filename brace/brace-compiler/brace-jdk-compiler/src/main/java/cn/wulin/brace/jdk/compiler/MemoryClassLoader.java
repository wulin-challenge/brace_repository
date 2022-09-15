package cn.wulin.brace.jdk.compiler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.jar.Manifest;

/**
 * Load class from byte[] which is compiled in memory.
 * 
 * @author michael
 */
public class MemoryClassLoader extends URLClassLoader {

	// class name to class bytes:
	Map<String, byte[]> classBytes = new HashMap<String, byte[]>();
	
	private static MemoryClassLoader memoryClassLoader;
	private static ConcurrentMap<String,Class<?>> CLASS_CACHE = new ConcurrentHashMap<>();

//	public MemoryClassLoader(Map<String, byte[]> classBytes) {
//		super(new URL[0], MemoryClassLoader.class.getClassLoader());
//		this.classBytes.putAll(classBytes);
//	}
	
	public static MemoryClassLoader getInstance() {
		
		if(memoryClassLoader == null) {
			memoryClassLoader = new MemoryClassLoader();
		}
		return memoryClassLoader;
	}
	
	public void addClass(Map<String, byte[]> classBytes) {
		this.classBytes.putAll(classBytes);
	}

	private MemoryClassLoader() {
		super(new URL[0], MemoryClassLoader.class.getClassLoader());
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		byte[] buf = classBytes.get(name);
		if (buf == null) {
			Class<?> clazz = CLASS_CACHE.get(name);
			if(clazz != null) {
				return clazz;
			}
			return super.findClass(name);
		}
		classBytes.remove(name);
		Class<?> clazz = defineClass(name, buf, 0, buf.length);
		CLASS_CACHE.put(name, clazz);
		return clazz;
	}

	
	
	//////////////////////////////////////
	
	@Override
	public InputStream getResourceAsStream(String name) {
		System.out.println("public InputStream getResourceAsStream(String name) {");
		return super.getResourceAsStream(name);
	}

	@Override
	public void close() throws IOException {
		System.out.println("public void close() throws IOException {");
		super.close();
	}

	@Override
	protected void addURL(URL url) {
		System.out.println("protected void addURL(URL url) {");
		super.addURL(url);
	}

	@Override
	public URL[] getURLs() {
		System.out.println("public URL[] getURLs() {");
		return super.getURLs();
	}

	@Override
	protected Package definePackage(String name, Manifest man, URL url) throws IllegalArgumentException {
		System.out.println("protected Package definePackage(String name, Manifest man, URL url) throws IllegalArgumentException {");
		return super.definePackage(name, man, url);
	}

	@Override
	public URL findResource(String name) {
		System.out.println("public URL findResource(String name) {");
		return super.findResource(name);
	}

	@Override
	public Enumeration<URL> findResources(String name) throws IOException {
		System.out.println("public Enumeration<URL> findResources(String name) throws IOException {");
		Enumeration<URL> findResources = super.findResources(name);
		return findResources;
	}

	@Override
	protected PermissionCollection getPermissions(CodeSource codesource) {
		System.out.println("protected PermissionCollection getPermissions(CodeSource codesource) {");
		return super.getPermissions(codesource);
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		System.out.println("public Class<?> loadClass(String name) throws ClassNotFoundException {");
		return super.loadClass(name);
	}

	@Override
	protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		System.out.println("protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {");
		return super.loadClass(name, resolve);
	}

	@Override
	protected Object getClassLoadingLock(String className) {
		System.out.println("protected Object getClassLoadingLock(String className) {");
		return super.getClassLoadingLock(className);
	}

	@Override
	public URL getResource(String name) {
		System.out.println("public URL getResource(String name) {");
		return super.getResource(name);
	}

	@Override
	public Enumeration<URL> getResources(String name) throws IOException {
		System.out.println("public Enumeration<URL> getResources(String name) throws IOException {");
		return super.getResources(name);
	}

	@Override
	protected Package definePackage(String name, String specTitle, String specVersion, String specVendor,
			String implTitle, String implVersion, String implVendor, URL sealBase) throws IllegalArgumentException {
		
		System.out.println("protected Package definePackage(String name, String specTitle, String specVersion, String specVendor,");
		return super.definePackage(name, specTitle, specVersion, specVendor, implTitle, implVersion, implVendor, sealBase);
	}

	@Override
	protected Package getPackage(String name) {
		System.out.println("protected Package getPackage(String name) {");
		return super.getPackage(name);
	}

	@Override
	protected Package[] getPackages() {
		System.out.println("protected Package[] getPackages() {");
		return super.getPackages();
	}

	@Override
	protected String findLibrary(String libname) {
		System.out.println("protected String findLibrary(String libname) {");
		return super.findLibrary(libname);
	}

	@Override
	public void setDefaultAssertionStatus(boolean enabled) {
		System.out.println("public void setDefaultAssertionStatus(boolean enabled) {");
		super.setDefaultAssertionStatus(enabled);
	}

	@Override
	public void setPackageAssertionStatus(String packageName, boolean enabled) {
		System.out.println("public void setPackageAssertionStatus(String packageName, boolean enabled) {");
		super.setPackageAssertionStatus(packageName, enabled);
	}

	@Override
	public void setClassAssertionStatus(String className, boolean enabled) {
		System.out.println("public void setClassAssertionStatus(String className, boolean enabled) {");
		super.setClassAssertionStatus(className, enabled);
	}

	@Override
	public void clearAssertionStatus() {
		System.out.println("public void clearAssertionStatus() {");
		super.clearAssertionStatus();
	}
	

}
