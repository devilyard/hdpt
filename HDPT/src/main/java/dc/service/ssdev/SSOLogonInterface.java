/*
 * @(#)SwitchInterface.java Created on Sep 28, 2012 5:35:58 PM
 *
 * 版权：版权所有 B-Soft 保留所有权力。
 */
package dc.service.ssdev;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ctd.accredit.User;
import ctd.accredit.UsersController;
import ctd.dictionary.Dictionaries;
import ctd.dictionary.Dictionary;
import ctd.sso.SSOLogon;
import ctd.util.context.Context;
import dc.auth.HttpSessionHolder;
import dc.util.HttpHelper;

/**
 * 
 * 
 * @author <a href="mailto:zhengs@bsoft.com.cn">zhengshi</a>
 */
public class SSOLogonInterface extends SSOLogon {

	/*
	 * (non-Javadoc)
	 * 
	 * @see ctd.sso.SSOLogon#execute(java.util.Map, java.util.Map,
	 * ctd.util.context.Context)
	 */
	public void execute(Map<String, Object> req, Map<String, Object> res,
			Context ctx) {
		super.execute(req, res, ctx);
		int code = (Integer) res.get(LogonInterface.RES_CODE);
		if (code == 200) {
			HttpServletRequest request = (HttpServletRequest) ctx
					.get(ctx.HTTP_REQUEST);
			HttpSession session = request.getSession();
			String uid = (String) session.getAttribute("uid");
			if (uid == null) {
				return;
			}
			User user = UsersController.instance().getUser(uid);
			String roleId = user.get("role.id");
			Dictionary jobDic = Dictionaries.instance().getDic("jobLayout");
			session.setAttribute("Layout",
					jobDic.getItem(roleId).getProperty("Layout"));
			res.put(HttpHelper.SESSIONID_KEY_NAME, session.getId());
			HttpSessionHolder.add(session);			
		}
	}
}
