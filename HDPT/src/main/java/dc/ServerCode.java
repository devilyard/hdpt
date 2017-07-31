/*
 * @(#)ServerCode.java Created on Sep 12, 2012 4:13:36 PM
 *
 * 版权：版权所有 B-Soft 保留所有权力。
 */
package dc;

/**
 * 
 * 
 * @author <a href="mailto:zhengs@bsoft.com.cn">zhengshi</a>
 */
public abstract class ServerCode {

	public static final int OK = 200;
	
	public static final int TARGET_OT_FOUND = 404;
	
	public static final int INVALID_UUID = 405;

	public static final int CONTENT_VIEW_NOT_DEFINED = 406;
	
	public static final int SERVICE_PROCESS_FAILED = 205;
	
	public static final int NOT_LOGON = 201; 
	
	public static final int DATA_ACCESS_FAILED=203;
	
	public static final int REDIRECT_TO_VIEW = 20;
	
}
