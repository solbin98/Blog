package com.project.config;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DBConfig {
    @Bean(destroyMethod = "close")
    public DataSource dataSource(){
        DataSource ds = new DataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost/blog?characterEncoding=utf8");
        ds.setUsername("root");
        ds.setPassword("232423e");
        ds.setInitialSize(5);
        ds.setMaxActive(20);
        return ds;
    }
}
