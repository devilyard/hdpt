/*
 * @(#)LogonInterface.java Created on Sep 26, 2012 2:31:56 PM
 *
 * 版权：版权所有 B-Soft 保留所有权力。
 */
package dc.service.ssdev;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import ctd.accredit.User;
import ctd.accredit.UsersController;
import ctd.service.core.Logon;
import ctd.service.core.Service;
import ctd.util.context.Context;
import dc.ServerCode;
import dc.auth.HttpSessionHolder;
import dc.util.HttpHelper;

/**
 * 
 * 
 * @author <a href="mailto:zhengs@bsoft.com.cn">zhengshi</a>
 */
public class LogonInterface extends Logon {

	/*
	 * (non-Javadoc)
	 * 
	 * @see ctd.service.core.Logon#execute(java.util.Map, java.util.Map,
	 * ctd.util.context.Context)
	 */
	public void execute(Map<String, Object> req, Map<String, Object> res,
			Context ctx) {
		super.execute(req, res, ctx);
		HttpServletRequest request = (HttpServletRequest) ctx
				.get(Context.HTTP_REQUEST);
		processExtendOperation(request, res, ctx);

		// @@ 请求处理完成后，更新SESSIONID。
		Cookie cookie = new Cookie(HttpHelper.SESSIONID_KEY_NAME,
				request.getRequestedSessionId());
		cookie.setPath("/");
		cookie.setDomain(request.getServerName());
		cookie.setMaxAge(request.getSession().getMaxInactiveInterval());
		HttpServletResponse response = (HttpServletResponse) ctx
				.get(Context.HTTP_RESPONSE);
		response.addCookie(cookie);
	}

	/**
	 * @param req
	 * @param res
	 * @param ctx
	 */
	private void processExtendOperation(HttpServletRequest request,
			Map<String, Object> res, Context ctx) {
		int code = (Integer) res.get(LogonInterface.RES_CODE);
		if (code == 200) {
			HttpSession session = request.getSession();
			String uid = (String) session.getAttribute("uid");
			if (uid == null) {
				return;
			}
			res.put(HttpHelper.SESSIONID_KEY_NAME, session.getId());
			HttpSessionHolder.add(session);
			User user = UsersController.instance().getUser(uid);
			session.setAttribute("user", user);
//			String roleId = user.get("role.id");
//			try {
//				session.setAttribute("layout",
//						dynamiclyContentView(roleId, ctx));
//			} catch (HibernateException e) {
//				res.put(Service.RES_CODE, ServerCode.SERVICE_PROCESS_FAILED);
//				res.put(Service.RES_MESSAGE,
//						"Database error: " + e.getMessage());
//				return;
//			} catch (ExpException e) {
//				res.put(Service.RES_CODE, ServerCode.CONTENT_VIEW_NOT_DEFINED);
//				res.put(Service.RES_MESSAGE, "No content view matched.");
//				return;
//			}
			// @@ 如果url里带了mpiId参数，登录后直接跳转到EHRView浏览界面。
			String mpiId = request.getParameter("mpiId");
			if (!StringUtils.isEmpty(mpiId)) {
				res.put(Service.RES_CODE, ServerCode.REDIRECT_TO_VIEW);
				res.put("uuid", session.getId() + "-" + mpiId);
			}
		}
	}

	/**
	 * @param roleId
	 * @param ctx
	 * @return
	 * @throws ExpException
	 */
	/*@SuppressWarnings("unchecked")
	private List<String> dynamiclyContentView(String roleId, Context ctx)
			throws ExpException, HibernateException {
		SessionFactory sessionFactory = AppContextHolder.get().getBean(
				SessionFactory.class);
		String hql = new StringBuilder(
				"select EXP as EXP, LAYOUT as LAYOUT from SYS_JobLayout")
				.append(" where JOB=:JOB").toString();
		Session session = null;
		List<Map<String, Object>> list = null;
		try {
			session = sessionFactory.openSession();
			Query query = session.createQuery(hql).setResultTransformer(
					Transformers.ALIAS_TO_ENTITY_MAP);
			query.setString("JOB", roleId);
			list = query.list();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		if (list == null || list.isEmpty()) {
			return new ArrayList<String>(0);
		}
		List<String> matched = new ArrayList<String>();
		int max = 0;// 匹配条件个数
		for (Map<String, Object> m : list) {
			String exp = (String) m.get("EXP");
			if (false == (Boolean) ExpRunner.run(exp, ctx)) {
				continue;
			}
			int count = 0;
			for (int i = 0; i < exp.length(); i++) {
				if (exp.charAt(i) == '[') {
					count += 1;
				}
			}
			if (max < count) {
				max = count;
				matched.add(0, exp);
			} else {
				matched.add(exp);
			}
		}
		return matched;
	}*/
}
