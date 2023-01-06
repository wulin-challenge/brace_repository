package cn.wulin.brace.sql.script.freemarker;

import org.lin.linfreemarker.core.TemplateElement;

/**
 * 网站分类持有者
 * @author wulin
 *
 */
public class TemplateElementHolder {
	private static final ThreadLocal<TemplateElement> WEBSITE_CODE_HOLDER = new ThreadLocal<>();

	public static void setDomain(TemplateElement element) {
		WEBSITE_CODE_HOLDER.set(element);
	}
	
	public static TemplateElement getDomain() {
		return WEBSITE_CODE_HOLDER.get();
	}
	
	public static void remove() {
		WEBSITE_CODE_HOLDER.remove();
	}
}
