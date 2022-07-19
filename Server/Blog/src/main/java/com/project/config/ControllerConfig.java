package com.project.config;

import com.project.controller.HomeController;
import com.project.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ServiceConfig.class)
public class ControllerConfig {
    @Autowired
    private CategoryService categoryService;

    @Bean
    public HomeController homeController(){
        return new HomeController();
    }
}
