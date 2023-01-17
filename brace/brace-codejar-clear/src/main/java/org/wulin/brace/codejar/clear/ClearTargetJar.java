package org.wulin.brace.codejar.clear;

import java.io.File;
import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import cn.hutool.core.io.FileUtil;

/**
 * 清理target目录下的jar
 * @author wulin
 *
 */
public class ClearTargetJar {
	private static final ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
	//private static final String ROOT_DIR= "file:G:/code_workspace/workspace/**/target/*";
//	private static final String ROOT_DIR= "file:D:/software/programming/java/IDE/eclipse/workspace/**/target/*";
	private static final String ROOT_DIR= "file:D:/software/helpProgramming/git_client/git/workspace_directory/**/target/*";


	public static void main(String[] args) {
		ClearTargetJar clear = new ClearTargetJar();
		clear.xx();
		
	}
	
	public void xx() {
		try {
			Resource[] resources=resolver.getResources(ROOT_DIR);
			System.out.println(resources.length);
			
			int i = 0;
			for (Resource resource : resources) {
				
				File file = resource.getFile();
				Boolean flag = FileUtil.del(file);
				if(flag) {
					System.out.println(i+" ==> "+resource);
				}
				
				i++;
			}
			System.out.println(resources.length);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
