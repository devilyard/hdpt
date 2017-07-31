/*
 * @(#)ValidLogon.java Created on Sep 10, 2012 9:30:05 AM
 *
 * ��Ȩ����Ȩ���� B-Soft ��������Ȩ����
 */
package dc.auth;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import dc.ServerCode;
import dc.util.HttpHelper;
import dc.util.uuid.UUID;
import dc.util.uuid.UUIDInitException;

/**
 * 
 * @author <a href="mailto:zhengs@bsoft.com.cn">zhengshi</a>
 */
@Component
@Aspect
public class ValidLogon {

	private static final Logger logger = LoggerFactory
			.getLogger(ValidLogon.class);

	@Pointcut("execution(* dc.controller.MtcController.getlablsit(..))||" +
			"execution(* dc.controller.MtcController.getlabhtml(..))||execution(* dc.controller.MtcController.edtpwd(..))")
	public void invoke() {
	}

	@Around("invoke()")
	public Object process(ProceedingJoinPoint pjp) throws Throwable {
		
		Object[] args = pjp.getArgs();
		HttpServletRequest request = (HttpServletRequest) args[0];
		HttpServletResponse response = (HttpServletResponse) args[1];
		
		if(request.getSession().getAttribute("map")==null){
			HttpHelper.writeJSON(ServerCode.NOT_LOGON, "NotLogon.", null,
					response);
			return null;
		}
		return pjp.proceed(args);
	}
}
