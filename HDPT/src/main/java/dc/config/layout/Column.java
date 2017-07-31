/*
 * @(#)Column.java Created on Oct 24, 2012 8:52:22 AM
 *
 * 版权：版权所有 B-Soft 保留所有权力。
 */
package dc.config.layout;

import org.dom4j.Element;

/**
 * 
 * 
 * @author <a href="mailto:zhengs@bsoft.com.cn">zhengshi</a>
 */
public class Column extends Grid {

	/**
	 * @param define
	 */
	public Column(Element define) {
		super(define);
	}

	/* (non-Javadoc)
	 * @see dc.config.layout.LayoutItem#getType()
	 */
	@Override
	public String getType() {
		return "column";
	}

	
}
