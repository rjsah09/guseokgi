package com.yang.guseokgi.config;

import com.yang.guseokgi.Interceptor.LoginInterceptor;
import com.yang.guseokgi.Interceptor.ManagerLoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LoginInterceptor interceptor = new LoginInterceptor();
        registry.addInterceptor(interceptor)
                .addPathPatterns(interceptor.needLogin)
                .excludePathPatterns(interceptor.notNeedLogin);

        ManagerLoginInterceptor managerInterceptor = new ManagerLoginInterceptor();
        registry.addInterceptor(managerInterceptor)
                .addPathPatterns(managerInterceptor.needLogin)
                .excludePathPatterns(managerInterceptor.notNeedLogin);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/home.html");
    }

}
