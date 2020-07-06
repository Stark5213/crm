package com.xxxx.crm.config;

import com.xxxx.crm.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
    //将拦截器LoginInterceptor交给IOC维护
    @Bean
    public LoginInterceptor loginInterceptor(){
        return new LoginInterceptor();
    }

    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
       registry.addInterceptor(loginInterceptor()).
               //拦截的路径
               addPathPatterns("/**").
               excludePathPatterns("/index","/user/login","/css/**","/images/**","/js/**","/lib/**");
    }
}
