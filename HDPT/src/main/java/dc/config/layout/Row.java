/*
 * @(#)Row.java Created on Oct 23, 2012 6:05:16 PM
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
public class Row extends Grid {

	/**
	 * @param define
	 */
	public Row(Element define) {
		super(define);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dc.config.layout.LayoutItem#getType()
	 */
	@Override
	public String getType() {
		return "row";
	}

}
