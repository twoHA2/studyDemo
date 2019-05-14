package com.southintel.zaokin.base.advice;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: xumeng
 * @Date: 2018/5/9/009 16:24
 * @Description: 过滤器配置  处理ServerletInputStream 不能重复读取的问题
 */
@Configuration
public class FilterConfig {

    /**
     * 配置过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean someFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(postDataDumperFilter());
        registration.addUrlPatterns("/*");
        registration.setName("postDataDumperFilter");
        return registration;
    }

    @Bean(name = "postDataDumperFilter")
    public PostDataDumperFilter postDataDumperFilter(){
        return new PostDataDumperFilter();
    }
}
