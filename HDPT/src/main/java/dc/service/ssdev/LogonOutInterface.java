/*
 * @(#)LogonOutInterface.java Created on Nov 9, 2012 10:51:15 AM
 *
 * ��Ȩ����Ȩ���� B-Soft ��������Ȩ����
 */
package dc.service.ssdev;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ctd.service.core.LogonOut;
import ctd.util.context.Context;
import dc.util.HttpHelper;

/**
 * 
 * 
 * @author <a href="mailto:zhengs@bsoft.com.cn">zhengshi</a>
 */
public class LogonOutInterface extends LogonOut {

	/*
	 * (non-Javadoc)
	 * 
	 * @see ctd.service.core.LogonOut#execute(java.util.Map, java.util.Map,
	 * ctd.util.context.Context)
	 */
	public void execute(Map<String, Object> req, Map<String, Object> res,
			Context ctx) {
		super.execute(req, res, ctx);
		HttpServletRequest request = (HttpServletRequest) ctx
				.get(ctx.HTTP_REQUEST);
		HttpSession session = HttpHelper.getSession(request, null);
		if (session != null) {
			session.invalidate();
		}
	}
}
