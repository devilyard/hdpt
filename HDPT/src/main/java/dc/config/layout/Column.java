/*
 * @(#)Column.java Created on Oct 24, 2012 8:52:22 AM
 *
 * ��Ȩ����Ȩ���� B-Soft ��������Ȩ����
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
