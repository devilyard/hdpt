/*
 * @(#)Layout.java Created on Oct 23, 2012 5:46:30 PM
 *
 * 版权：版权所有 B-Soft 保留所有权力。
 */
package dc.config.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;

import ctd.config.DocConfig;

/**
 * 
 * 
 * @author <a href="mailto:zhengs@bsoft.com.cn">zhengshi</a>
 */
public class Layout extends DocConfig {

	private static final long serialVersionUID = 3286193142521572425L;

	private String id;
	private List<LayoutItem> items = new ArrayList<LayoutItem>();

	public Layout() {

	}

	public Layout(Document define) {
		setDefineDoc(define);
	}

	public void setDefineDoc(Document define) {
		super.setDefineDoc(define);
		Element root = define.getRootElement();
		id = root.attributeValue("id");
		@SuppressWarnings("unchecked")
		List<Element> wl = root.selectNodes("/layout/*");
		if (wl != null && wl.size() > 0) {
			for (Element e : wl) {
				if ("row".equals(e.getName())) {
					items.add(new Row(e));
				} else if ("col".equals(e.getName())) {
					items.add(new Column(e));
				} else if ("widget".equals(e.getName())) {
					items.add(new Widget(e));
				}
			}
		}
	}

	/**
	 * @return
	 */
	public Map<String, Object> asMap() {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("id", id);
		if (items.size() > 0) {
			List<Map<String, Object>> wl = new ArrayList<Map<String, Object>>(
					items.size());
			for (LayoutItem li : items) {
				wl.add(li.asMap());
			}
			m.put("items", wl);
		}
		return m;
	}
}
