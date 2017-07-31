/*
 * @(#)Widget.java Created on Oct 24, 2012 8:55:37 AM
 *
 * 版权：版权所有 B-Soft 保留所有权力。
 */
package dc.config.layout;

import java.util.Map;

import org.dom4j.Element;

/**
 * 
 * 
 * @author <a href="mailto:zhengs@bsoft.com.cn">zhengshi</a>
 */
public class Widget extends LayoutItem {

	private String id;

	public Widget(Element define) {
		super(define);
		id = define.attributeValue("id");
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, Object> asMap() {
		Map<String, Object> m = super.asMap();
		m.put("id", id);
		return m;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dc.config.layout.LayoutItem#getType()
	 */
	@Override
	public String getType() {
		return "widget";
	}
}
