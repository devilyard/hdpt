/*
 * @(#)ContextArgumentResolver.java Created on 2012-10-10 上午 10:56:30
 * 
 * 版权：版权所有 B-Soft 保留所有权力。
 */
package dc.util.method.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import ctd.accredit.User;
import ctd.accredit.UsersController;
import ctd.util.AppContextHolder;
import ctd.util.context.Context;
import ctd.util.context.UserContext;

/**
 * 
 * @author <a href="mailto:rishyonn@gmail.com">zhengshi</a>
 */
public class ContextArgumentResolver implements HandlerMethodArgumentResolver {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.method.support.HandlerMethodArgumentResolver#
	 * supportsParameter(org.springframework.core.MethodParameter)
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> paramType = parameter.getParameterType();
		return Context.class.isAssignableFrom(paramType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.method.support.HandlerMethodArgumentResolver#
	 * resolveArgument(org.springframework.core.MethodParameter,
	 * org.springframework.web.method.support.ModelAndViewContainer,
	 * org.springframework.web.context.request.NativeWebRequest,
	 * org.springframework.web.bind.support.WebDataBinderFactory)
	 */
	@Override
	public Object resolveArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = webRequest
				.getNativeRequest(HttpServletRequest.class);
		HttpServletResponse response = webRequest
				.getNativeResponse(HttpServletResponse.class);
		User user = getUser(request);
		Context ctx = createContext(request);
		ctx.put(ctx.HTTP_REQUEST, request);
		ctx.put(ctx.HTTP_RESPONSE, response);
		if (user != null) {
			Context userCtx = new UserContext(user);
			ctx.putCtx("user", userCtx);
		}
		return ctx;
	}

	public static User getUser(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return null;
		}
		String uid = (String) session.getAttribute("uid");
		if (uid == null) {
			return null;
		}
		return UsersController.instance().getUser(uid);
	}

	public static Context createContext(HttpServletRequest request) {
		Context ctx = new Context();
		ctx.put("_applicationContext", AppContextHolder.get());
		HttpSession session = request.getSession(false);
		if (session == null) {
			return ctx;
		}
		ctx.put("_webSession", session);
		return ctx;
	}
}
