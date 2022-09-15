/*
 * Copyright 1999-2011 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.wulin.brace.jdk.compiler2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.DiagnosticListener;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Log;

import cn.wulin.ioc.util.ClassHelper;

/**
 * JdkCompiler. (SPI, Singleton, ThreadSafe)
 *
 * @author william.liangf
 */
@SuppressWarnings("resource")
public class JdkCompiler2 extends AbstractCompiler2 {

    private final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    private final DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<JavaFileObject>();

    private final ClassLoaderImpl classLoader;

    private final JavaFileManagerImpl javaFileManager;

    private volatile List<String> options;

	public JdkCompiler2() {
        options = new ArrayList<String>();
        options.add("-target"); 
        options.add("1.8");
        
        //StandardJavaFileManager manager = compiler.getStandardFileManager(diagnosticCollector, null, null);
        StandardJavaFileManager manager = getStandardFileManager(diagnosticCollector, null, null);
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
//        if (loader instanceof URLClassLoader
//                && (!loader.getClass().getName().equals("sun.misc.Launcher$AppClassLoader"))) {
//            try {
//                URLClassLoader urlClassLoader = (URLClassLoader) loader;
//                List<File> files = new ArrayList<File>();
//                for (URL url : urlClassLoader.getURLs()) {
//                    files.add(new File(url.getFile()));
//                }
//                manager.setLocation(StandardLocation.CLASS_PATH, files);
//            } catch (IOException e) {
//                throw new IllegalStateException(e.getMessage(), e);
//            }
//        }
        classLoader = AccessController.doPrivileged(new PrivilegedAction<ClassLoaderImpl>() {
            public ClassLoaderImpl run() {
            	URLClassLoader urlClassLoader = (URLClassLoader) loader;
                return new ClassLoaderImpl(urlClassLoader.getURLs(),loader);
            }
        });
        javaFileManager = new JavaFileManagerImpl(manager, classLoader);
    }
	
	 private URL[] getClassLoaderURL(JavaFileManager.Location location) {
     	ListBuffer var3 = new ListBuffer();
 		
 		SpringJavaFileManager2 manager = JdkCompiler2.getStandardFileManager(null, null, null);
 		URL[] urls = manager.getURL(location);
 		if(urls != null) {
 			for (URL url : urls) {
 				var3.add(url);
				}
 		}
 		
 		return (URL[]) var3.toArray(new URL[var3.size()]);
     }
	
	/**
	 * 自定义Java文件管理器
	 *
	 * @param var1
	 * @param var2
	 * @param var3
	 * @return
	 */
	public static SpringJavaFileManager2 getStandardFileManager(DiagnosticListener<? super JavaFileObject> var1, Locale var2, Charset var3) {
		Context var4 = new Context();
		var4.put(Locale.class, var2);
		if (var1 != null) {
			var4.put(DiagnosticListener.class, var1);
		}

		PrintWriter var5 = var3 == null ? new PrintWriter(System.err, true) : new PrintWriter(new OutputStreamWriter(System.err, var3), true);
		var4.put(Log.outKey, var5);
		return new SpringJavaFileManager2(var4, true, var3);
	}

    @Override
    public Class<?> doCompile(String name, String sourceCode) throws Throwable {
        int i = name.lastIndexOf('.');
        String packageName = i < 0 ? "" : name.substring(0, i);
        String className = i < 0 ? name : name.substring(i + 1);
        JavaFileObjectImpl javaFileObject = new JavaFileObjectImpl(className, sourceCode);
        javaFileManager.putFileForInput(StandardLocation.SOURCE_PATH, packageName,
                className + ClassUtils2.JAVA_EXTENSION, javaFileObject);
        Boolean result = compiler.getTask(null, javaFileManager, diagnosticCollector, options,
                null, Arrays.asList(new JavaFileObject[]{javaFileObject})).call();
        if (result == null || !result.booleanValue()) {
        	List<Diagnostic<? extends JavaFileObject>> diagnostics = diagnosticCollector.getDiagnostics();
        	
            throw new IllegalStateException("Compilation failed. class: " + name + ", diagnostics: " + diagnostics);
        }
        return classLoader.loadClass(name);
    }

    private static final class JavaFileObjectImpl extends SimpleJavaFileObject {

        private final CharSequence source;
        private ByteArrayOutputStream bytecode;

        public JavaFileObjectImpl(final String baseName, final CharSequence source) {
            super(ClassUtils2.toURI(baseName + ClassUtils2.JAVA_EXTENSION), Kind.SOURCE);
            this.source = source;
        }

        JavaFileObjectImpl(final String name, final Kind kind) {
            super(ClassUtils2.toURI(name), kind);
            source = null;
        }

        public JavaFileObjectImpl(URI uri, Kind kind) {
            super(uri, kind);
            source = null;
        }

        @Override
        public CharSequence getCharContent(final boolean ignoreEncodingErrors) throws UnsupportedOperationException {
            if (source == null) {
                throw new UnsupportedOperationException("source == null");
            }
            return source;
        }

        @Override
        public InputStream openInputStream() {
            return new ByteArrayInputStream(getByteCode());
        }

        @Override
        public OutputStream openOutputStream() {
            return bytecode = new ByteArrayOutputStream();
        }

        public byte[] getByteCode() {
            return bytecode.toByteArray();
        }
    }

    private static final class JavaFileManagerImpl extends ForwardingJavaFileManager<JavaFileManager> {

        private final ClassLoaderImpl classLoader;

        private final Map<URI, JavaFileObject> fileObjects = new HashMap<URI, JavaFileObject>();

        public JavaFileManagerImpl(JavaFileManager fileManager, ClassLoaderImpl classLoader) {
            super(fileManager);
            this.classLoader = classLoader;
        }

        @Override
        public FileObject getFileForInput(Location location, String packageName, String relativeName) throws IOException {
            FileObject o = fileObjects.get(uri(location, packageName, relativeName));
            if (o != null)
                return o;
            return super.getFileForInput(location, packageName, relativeName);
        }

        public void putFileForInput(StandardLocation location, String packageName, String relativeName, JavaFileObject file) {
            fileObjects.put(uri(location, packageName, relativeName), file);
        }

        private URI uri(Location location, String packageName, String relativeName) {
            return ClassUtils2.toURI(location.getName() + '/' + packageName + '/' + relativeName);
        }

        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String qualifiedName, Kind kind, FileObject outputFile)
                throws IOException {
            JavaFileObject file = new JavaFileObjectImpl(qualifiedName, kind);
            classLoader.add(qualifiedName, file);
            return file;
        }

        @Override
        public ClassLoader getClassLoader(JavaFileManager.Location location) {
        	return classLoader;
////        	return classLoader.getParent();
////        	
////        	URL[] classLoaderURL = getClassLoaderURL(location);
////        	
////        	for (URL url : classLoaderURL) {
////				System.out.println(url);
////			}
//    		try {
//    			
//    			URLClassLoader urlClassLoader = (URLClassLoader) classLoader.getParent();
//    			URL[] urLs = urlClassLoader.getURLs();
//    			for (URL url : urLs) {
//                    System.out.println(url);
//                }
//    			
//    			Class loaderClass = Class.forName("org.springframework.boot.loader.LaunchedURLClassLoader");
//    			Class[] var4 = new Class[]{URL[].class, ClassLoader.class};
//    			Constructor var5 = loaderClass.getConstructor(var4);
//    			ClassLoader springLoader = (ClassLoader) var5.newInstance(urLs,  classLoader);
//    			
//    			return springLoader;
//    		} catch (Throwable var6) {
//    			var6.printStackTrace();
//    		}
////            return null;
//            return null;
        }
        
        private URL[] getClassLoaderURL(JavaFileManager.Location location) {
        	ListBuffer var3 = new ListBuffer();
    		SpringJavaFileManager2 manager = JdkCompiler2.getStandardFileManager(null, null, null);
    		URL[] urls = manager.getURL(location);
    		if(urls != null) {
    			for (URL url : urls) {
    				var3.add(url);
				}
    		}
    		
    		return (URL[]) var3.toArray(new URL[var3.size()]);
        }
        
        @Override
        public String inferBinaryName(Location loc, JavaFileObject file) {
            if (file instanceof JavaFileObjectImpl)
                return file.getName();
            return super.inferBinaryName(loc, file);
        }

        @Override
        public Iterable<JavaFileObject> list(Location location, String packageName, Set<Kind> kinds, boolean recurse)
                throws IOException {
        	
            Iterable<JavaFileObject> result = super.list(location, packageName, kinds, false);

            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            List<URL> urlList = new ArrayList<URL>();
            Enumeration<URL> e = contextClassLoader.getResources(packageName);
            while (e.hasMoreElements()) {
                urlList.add(e.nextElement());
            }

            ArrayList<JavaFileObject> files = new ArrayList<JavaFileObject>();

            if (location == StandardLocation.CLASS_PATH && kinds.contains(JavaFileObject.Kind.CLASS)) {
                for (JavaFileObject file : fileObjects.values()) {
                    if (file.getKind() == Kind.CLASS && file.getName().startsWith(packageName)) {
                        files.add(file);
                    }
                }

                files.addAll(classLoader.files());
            } else if (location == StandardLocation.SOURCE_PATH && kinds.contains(JavaFileObject.Kind.SOURCE)) {
                for (JavaFileObject file : fileObjects.values()) {
                    if (file.getKind() == Kind.SOURCE && file.getName().startsWith(packageName)) {
                        files.add(file);
                    }
                }
            }

            for (JavaFileObject file : result) {
                files.add(file);
            }

            return files;
        }
    }

    private final class ClassLoaderImpl extends URLClassLoader {

        private final Map<String, JavaFileObject> classes = new HashMap<String, JavaFileObject>();

        ClassLoaderImpl(URL[] urls,final ClassLoader parentClassLoader) {
            super(urls,parentClassLoader);
        }

		Collection<JavaFileObject> files() {
            return Collections.unmodifiableCollection(classes.values());
        }

        @Override
        protected Class<?> findClass(final String qualifiedClassName) throws ClassNotFoundException {
            JavaFileObject file = classes.get(qualifiedClassName);
            if (file != null) {
                byte[] bytes = ((JavaFileObjectImpl) file).getByteCode();
                return defineClass(qualifiedClassName, bytes, 0, bytes.length);
            }
            try {
                return ClassHelper.forNameWithCallerClassLoader(qualifiedClassName, getClass());
            } catch (ClassNotFoundException nf) {
                return super.findClass(qualifiedClassName);
            }
        }

        void add(final String qualifiedClassName, final JavaFileObject javaFile) {
            classes.put(qualifiedClassName, javaFile);
        }

        @Override
        protected synchronized Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException {
            return super.loadClass(name, resolve);
        }

        @Override
        public InputStream getResourceAsStream(final String name) {
            if (name.endsWith(ClassUtils2.CLASS_EXTENSION)) {
                String qualifiedClassName = name.substring(0, name.length() - ClassUtils2.CLASS_EXTENSION.length()).replace('/', '.');
                JavaFileObjectImpl file = (JavaFileObjectImpl) classes.get(qualifiedClassName);
                if (file != null) {
                    return new ByteArrayInputStream(file.getByteCode());
                }
            }
            return super.getResourceAsStream(name);
        }
    }


}
