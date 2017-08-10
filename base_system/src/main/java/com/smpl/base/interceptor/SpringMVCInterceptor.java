package com.smpl.base.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * SpringMVC自定义 拦截器
 */
public class SpringMVCInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //首页为公开链接
        //TODO:公开链接 可作成 文件配置 的方式  后期
        String url=request.getRequestURI();
        if (url.indexOf("index.smpl")>0){
            return true;
        }
        HttpSession session=request.getSession();
        String userName= (String) session.getAttribute("userName");

        if (!StringUtils.isEmpty(userName)){
            return true;
        }
       request.getRequestDispatcher("/index").forward(request,response);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
