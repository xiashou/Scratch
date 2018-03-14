package com.tsingma.common.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.tsingma.core.util.Utils;

/**
 * 权限验证拦截器
 * @author xiashou
 * @since 2015/04/10
 */
public class AuthorityInterceptor implements Interceptor {

	private static final long serialVersionUID = -7668791469176996612L;
	
	private static Logger log = Logger.getLogger("SLog");
	
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		String result = null;
		HttpServletRequest request = ServletActionContext.getRequest();
//		HttpServletResponse response = ServletActionContext.getResponse();
		String requestUri = request.getRequestURI();
		
		try {
			result = invocation.invoke();
			return result;
		} catch(Exception e) {
			log.error("IP:" + AuthorityInterceptor.getIpAddress(request) + " | Request:" + requestUri);
			//通过instanceof判断到底是什么异常类型   
            if (e instanceof RuntimeException) {
                //未知的运行时异常   
                RuntimeException re = (RuntimeException) e;
                log.error(re.getMessage().trim());
            }
            log.error(Utils.getErrorMessage(e));
		}
		return result;
	}
	
	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		} 
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	@Override
	public void destroy() {
	}

	@Override
	public void init() {
	}
	
}
