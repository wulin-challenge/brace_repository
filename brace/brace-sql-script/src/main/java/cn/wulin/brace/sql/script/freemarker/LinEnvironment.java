package cn.wulin.brace.sql.script.freemarker;

import java.io.IOException;
import java.io.Writer;

import org.lin.linfreemarker.core.Environment;
import org.lin.linfreemarker.core.TemplateElement;
import org.lin.linfreemarker.template.Template;
import org.lin.linfreemarker.template.TemplateException;
import org.lin.linfreemarker.template.TemplateHashModel;

public class LinEnvironment extends Environment{

	public LinEnvironment(Template template, TemplateHashModel rootDataModel, Writer out) {
		super(template, rootDataModel, out);
	}

	@Override
	public void visit(TemplateElement element) throws IOException, TemplateException {
		TemplateElementHolder.setDomain(element);
		super.visit(element);
	}
	
	

	
	
	
	
	

}
