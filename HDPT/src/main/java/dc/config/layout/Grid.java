/*
 * @(#)Grid.java Created on Oct 24, 2012 9:04:24 AM
 *
 * 版权：版权所有 B-Soft 保留所有权力。
 */
package dc.config.layout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

/**
 * 
 * 
 * @author <a href="mailto:zhengs@bsoft.com.cn">zhengshi</a>
 */
public abstract class Grid extends LayoutItem {

	private List<LayoutItem> items = new ArrayList<LayoutItem>();

	public Grid(Element define) {
		super(define);
		@SuppressWarnings("unchecked")
		List<Element> wl = define.selectNodes("*");
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

	public Map<String, Object> asMap() {
		Map<String, Object> m = super.asMap();
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
