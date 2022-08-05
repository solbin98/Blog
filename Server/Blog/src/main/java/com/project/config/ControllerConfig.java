package com.project.config;

import com.project.controller.AdminLoginController;
import com.project.controller.BoardController;
import com.project.controller.CategoryController;
import com.project.controller.CommentController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ServiceConfig.class)
public class ControllerConfig {
    @Bean
    public CategoryController categoryController(){
        return new CategoryController();
    }

    @Bean
    public BoardController boardController() {return new BoardController();}

    @Bean
    public CommentController commentController() { return new CommentController();}

    @Bean
    public AdminLoginController adminLoginController() { return new AdminLoginController();}
}
