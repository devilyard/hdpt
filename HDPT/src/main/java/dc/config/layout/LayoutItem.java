/*
 * @(#)LayoutItem.java Created on Oct 24, 2012 2:22:08 PM
 *
 * 版权：版权所有 B-Soft 保留所有权力。
 */
package dc.config.layout;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Element;

/**
 * 
 * 
 * @author <a href="mailto:zhengs@bsoft.com.cn">zhengshi</a>
 */
public abstract class LayoutItem {

	protected Element define;
	private String width;
	private String height;
	private String minWidth;
	private String minHeight;

	public LayoutItem(Element define) {
		this.define = define;
		width = define.attributeValue("width");
		height = define.attributeValue("height");
		minWidth=define.attributeValue("minWidth");
		minHeight=define.attributeValue("minHeight");
	}

	public Element getDefine() {
		return define;
	}

	public Map<String, Object> asMap() {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("width", width);
		m.put("height", height);
		m.put("minWidth", minWidth);
		m.put("minHeight", minHeight);
		m.put("type", getType());
		return m;
	}

	/**
	 * @return
	 */
	public abstract String getType();
}
