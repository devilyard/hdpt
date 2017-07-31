/*
 * @(#)HttpSessionHolder.java Created on Sep 10, 2012 9:18:08 AM
 *
 * 版权：版权所有 B-Soft 保留所有权力。
 */
package dc.auth;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

/**
 * 
 * 
 * @author <a href="mailto:zhengs@bsoft.com.cn">zhengshi</a>
 */
public class HttpSessionHolder {

	private static Map<String, HttpSession> sessions = new HashMap<String, HttpSession>();

	public static void add(HttpSession session) {
		sessions.put(session.getId(), session);
	}

	public static HttpSession get(String sessionId) {
		return sessions.get(sessionId);
	}

	public static void remove(String sessionId) {
		sessions.remove(sessionId);
	}
}
