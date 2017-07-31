/*
 * @(#)DocumentCompressor.java Created on Jan 15, 2013 10:17:08 AM
 *
 * ��Ȩ����Ȩ���� B-Soft ��������Ȩ����
 */
package com.bsoft.xds;

import java.io.IOException;

/**
 * 
 * 
 * @author <a href="mailto:zhengs@bsoft.com.cn">zhengshi</a>
 */
public interface DocumentCompressor {

	/**
	 * @return ѹ���㷨�����ơ�
	 */
	public String getAlgorithm();
	
	/**
	 * ѹ�����ݡ�
	 * 
	 * @param source
	 * @return
	 * @throws IOException
	 */
	public byte[] compress(byte[] source) throws IOException;
	
	/**
	 * ��ѹ���ݡ�
	 * 
	 * @param source
	 * @return
	 * @throws IOException
	 */
	public byte[] decompress(byte[] source)  throws IOException;
}
