package com.smpl.base.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.smpl.base.Utils.WeixinUtil;
import com.smpl.base.entity.SessionComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


/**
 * 判断是否已经获取到了用户的openid，没有则跳转到授权界面进行获取
 * @author andy
 *
 */
public class WXSNSInterceptor implements HandlerInterceptor {
	
	@Autowired
	private ApplicationContext context;
	
	public static Logger logger = LoggerFactory.getLogger(WXSNSInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		SessionComponent sessionComponent = SessionComponent.getInstance(context);
		String openid = sessionComponent.getOpenid();
		if(openid!=null && !"".equals(openid)){ //已经获取到关注者openid
			logger.debug("openid : " + openid);
			return true;
		}else{
			String url = request.getRequestURL().toString();
			if (request.getQueryString() != null){
				url += "?" + request.getQueryString();
			}
			String redirectUrl = WeixinUtil.getRedirectUrl(url);
			response.sendRedirect(redirectUrl);
			return false;
		}
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
