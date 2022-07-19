package com.project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer){
        configurer.enable();
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry){
        registry.jsp("/WEB-INF/views/", ".jsp");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");}

}
