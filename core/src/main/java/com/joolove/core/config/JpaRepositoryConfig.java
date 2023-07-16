package com.joolove.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.joolove.core.repository.jpa")
public class JpaRepositoryConfig {

}
