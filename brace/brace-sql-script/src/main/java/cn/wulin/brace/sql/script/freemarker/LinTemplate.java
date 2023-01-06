package cn.wulin.brace.sql.script.freemarker;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import org.lin.linfreemarker.core.Environment;
import org.lin.linfreemarker.core.ParserConfiguration;
import org.lin.linfreemarker.template.Configuration;
import org.lin.linfreemarker.template.ObjectWrapper;
import org.lin.linfreemarker.template.SimpleHash;
import org.lin.linfreemarker.template.Template;
import org.lin.linfreemarker.template.TemplateException;
import org.lin.linfreemarker.template.TemplateHashModel;
import org.lin.linfreemarker.template.TemplateModel;

public class LinTemplate extends Template{

	public LinTemplate(String name, Reader reader, Configuration cfg, String encoding) throws IOException {
		super(name, reader, cfg, encoding);
	}

	public LinTemplate(String name, Reader reader, Configuration cfg) throws IOException {
		super(name, reader, cfg);
	}

	public LinTemplate(String name, String sourceCode, Configuration cfg) throws IOException {
		super(name, sourceCode, cfg);
	}

	public LinTemplate(String name, String sourceName, Reader reader, Configuration cfg,
			ParserConfiguration customParserConfiguration, String encoding) throws IOException {
		super(name, sourceName, reader, cfg, customParserConfiguration, encoding);
	}

	public LinTemplate(String name, String sourceName, Reader reader, Configuration cfg, String encoding)
			throws IOException {
		super(name, sourceName, reader, cfg, encoding);
	}

	public LinTemplate(String name, String sourceName, Reader reader, Configuration cfg) throws IOException {
		super(name, sourceName, reader, cfg);
	}

	@Override
	public Environment createProcessingEnvironment(Object dataModel, Writer out, ObjectWrapper wrapper)
			throws TemplateException, IOException {
		final TemplateHashModel dataModelHash;
        if (dataModel instanceof TemplateHashModel) {
            dataModelHash = (TemplateHashModel) dataModel;
        } else {
            if (wrapper == null) {
                wrapper = getObjectWrapper();
            }

            if (dataModel == null) {
                dataModelHash = new SimpleHash(wrapper);
            } else {
                TemplateModel wrappedDataModel = wrapper.wrap(dataModel);
                if (wrappedDataModel instanceof TemplateHashModel) {
                    dataModelHash = (TemplateHashModel) wrappedDataModel;
                } else if (wrappedDataModel == null) {
                    throw new IllegalArgumentException(
                            wrapper.getClass().getName() + " converted " + dataModel.getClass().getName() + " to null.");
                } else {
                    throw new IllegalArgumentException(
                            wrapper.getClass().getName() + " didn't convert " + dataModel.getClass().getName()
                            + " to a TemplateHashModel. Generally, you want to use a Map<String, Object> or a "
                            + "JavaBean as the root-map (aka. data-model) parameter. The Map key-s or JavaBean "
                            + "property names will be the variable names in the template.");
                }
            }
        }
        return new LinEnvironment(this, dataModelHash, out);
	}

	@Override
	public Environment createProcessingEnvironment(Object dataModel, Writer out) throws TemplateException, IOException {
		return createProcessingEnvironment(dataModel, out,null);
	}
	
	

}
