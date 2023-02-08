package com.yang.guseokgi.Interceptor;

import com.yang.guseokgi.dto.manager.ManagerSession;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

public class ManagerLoginInterceptor implements HandlerInterceptor {

    //로그인 인터셉터 처리
    public List needLogin = Arrays.asList("/manager/**");

    //로그인 인터셉터 비처리
    public List notNeedLogin = Arrays.asList("/");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ManagerSession managerSession = (ManagerSession) request.getSession().getAttribute("managerSession");

        if(managerSession != null) {
            return true;
        }

        response.sendRedirect("/managerLogin");
        return false;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {
    }
}
