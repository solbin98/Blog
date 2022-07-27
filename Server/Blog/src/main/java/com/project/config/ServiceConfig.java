package com.project.config;

import com.project.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;


@Configuration
@Import(DBConfig.class)
public class ServiceConfig {
    @Autowired
    DataSource dataSource;

    @Bean
    public CategoryService categoryService(){
        return new CategoryService(dataSource);
    }

    @Bean
    public BoardService boardService(){
        return new BoardService(dataSource);
    }

    @Bean
    public CommentService commentService() { return new CommentService(dataSource); }

    @Bean
    public TagService tagService() { return new TagService(dataSource); }

    @Bean
    public BoardTagService boardTagService() { return new BoardTagService(dataSource); }

    @Bean
    public FileService fileService() { return new FileService(dataSource); }

}
