package cn.wulin.brace.demo.processor;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * 注意: 自定义注解 CompileAnnotation 必须要在另外的maven模块使用,不能再本模块使用,否则可能不生效
 * @author ThinkPad
 *
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes(value = {"cn.wulin.brace.demo.processor.CompileAnnotation"})//这里写这个处理器可以处理的注解
public class AnnotationProcessorDemo extends AbstractProcessor{
	

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
		try {
			System.out.println("------=====成功执行,hello world0");
			
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
