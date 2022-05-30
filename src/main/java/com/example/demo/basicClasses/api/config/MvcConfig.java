package com.example.demo.basicClasses.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/afterLogin").setViewName("afterLogin");
        registry.addViewController("/").setViewName("login");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/admin_customer").setViewName("admin_customer");
        registry.addViewController("/admin_orders").setViewName("admin_orders");
        registry.addViewController("/admin_services").setViewName("admin_services");
        registry.addViewController("/admin_locations").setViewName("admin_locations");
        registry.addViewController("/admin_specifications").setViewName("admin_specifications");

    }


}