package com.bsoft.xds;

import com.bsoft.xds.exception.DocumentEntryException;

public interface TemplateFormater {

	/**
	 * ��֧�ֵ��ĵ��汾��
	 * 
	 * @return
	 */
	public String getAcceptableVersion();

	/**
	 * @return Դ�ĵ��ĸ�ʽ��
	 */
	public String getSourceFormat();

	/**
	 * @return ת����ĸ�ʽ��
	 */
	public String getDestFormat();

	/**
	 * ִ��ת����
	 * 
	 * @param input
	 * @return
	 * @throws DocumentEntryException
	 */
	public Object run(byte[] input) throws DocumentEntryException;
}
