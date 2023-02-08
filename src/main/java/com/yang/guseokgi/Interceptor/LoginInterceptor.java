package com.yang.guseokgi.Interceptor;

import com.yang.guseokgi.dto.account.AccountSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    //로그인 인터셉터 처리
    public List needLogin = Arrays.asList("/account/**", "/trade/**", "/chat/**", "/post/myPost/**", "/post/write/**",
            "/post/update/**", "/post/delete/**", "/favorite/**", "/notification/**",
            "/inquiry/**");

    //로그인 인터셉터 비처리
    public List notNeedLogin = Arrays.asList("/");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AccountSession accountSession = (AccountSession) request.getSession().getAttribute("accountSession");

        if(accountSession != null) {
            return true;
        }

        response.sendRedirect("/login");
        return false;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {
    }

}
