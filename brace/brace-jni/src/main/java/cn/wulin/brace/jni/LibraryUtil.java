package cn.wulin.brace.jni;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class LibraryUtil {
	
	/**
	 * 加载动态库
	 * @param dirs 动态库目录
	 * @param suffix 动态库后缀
	 */
	public static void loadLibraryDirs(String dirs, String suffix) {
		
		File dirsPath = new File(dirs);
		
		if(StringUtils.isBlank(dirs) || StringUtils.isBlank(suffix)) {
			throw new RuntimeException("dirs 或者 suffix 不能为空");
		}
		
		if(!dirsPath.isDirectory()) {
			throw new RuntimeException("dirs 必须是目录");
		}
		
		final String suffix2 = suffix.trim();
		File[] libFiles = dirsPath.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				String[] names = file.getName().split("\\.");
				if(names == null || names.length <2) {
					return false;
				}
				
				String tempSuffix = names[names.length -1].trim();
				if(suffix2.equalsIgnoreCase(tempSuffix)) {
					return true;
				}
				return false;
			}
		});
		
		if(libFiles == null || libFiles.length == 0) {
			throw new RuntimeException("没有找到要加载的动态库文件, 目录: "+dirs);
		}
		
		new LoadingLibrary(libFiles).load();
	}
	
	public static void loadLibraryFile(String libFile) {
		loadLibraryFile(new File(libFile));
	}
	
	public static void loadLibraryFile(File libFile) {
		if(libFile == null || !libFile.isFile()) {
			//加载失败
			throw new RuntimeException("libFile 不能空且必须是文件");
		}
		
		System.load(libFile.getAbsolutePath());
	}
	
}

class LoadingLibrary{
	List<File> libFiles = new ArrayList<>();
	
	/**
	 * 加载出错的文件
	 */
	List<File> errorFiles = new ArrayList<>();
	

	public LoadingLibrary(File[] libFiles) {
		super();
		this.libFiles = new ArrayList<>(Arrays.asList(libFiles));
	}
	
	public void load() {
		while(true) {
			boolean loading = loading();
			
			//完全加载成
			if(loading && errorFiles.size() == 0) {
				return;
			}
			
			//加载成功一部分,继续加载
			if(loading && errorFiles.size() > 0) {
				libFiles.clear();
				libFiles.addAll(errorFiles);
				errorFiles.clear();
				continue;
			}
			
			throw new RuntimeException("缺少必须的依赖!");
		}
	}
	
	/**
	 * 至少需要加载成功一次,则返回true,否则false
	 * @return
	 */
	private boolean loading() {
		boolean flag = false;
		for (File libFile : libFiles) {
			try {
				LibraryUtil.loadLibraryFile(libFile);
				flag = true;
			} catch (Throwable e) {
				errorFiles.add(libFile);
			}
		}
		return flag;
	}
}
