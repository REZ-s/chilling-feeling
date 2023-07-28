package com.joolove.core.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;


/**
 *  작성 목적
 *  (1) 명시적으로 작성하여, datasource 가 hikari 를 이용하여 mysql80 service 를 자동으로 시작할 수 있게 매핑
 *  (2) 다중 스키마 (카탈로그) 사용
*/
@Configuration
public class DataSourceConfig {
    @Primary
    @Bean(name = "dataSourceAuth")
    @ConfigurationProperties("spring.datasource.hikari.auth")
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "dataSourceBilling")
    @ConfigurationProperties("spring.datasource.hikari.billing")
    public DataSource dataSource2() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "dataSourceLog")
    @ConfigurationProperties("spring.datasource.hikari.log")
    public DataSource dataSource3() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "dataSourceMember")
    @ConfigurationProperties("spring.datasource.hikari.member")
    public DataSource dataSource4() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "dataSourceProduct")
    @ConfigurationProperties("spring.datasource.hikari.product")
    public DataSource dataSource5() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "dataSourceRecommendation")
    @ConfigurationProperties("spring.datasource.hikari.recommendation")
    public DataSource dataSource6() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

}
