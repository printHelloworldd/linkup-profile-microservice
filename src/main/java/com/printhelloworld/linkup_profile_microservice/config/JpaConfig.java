package com.printhelloworld.linkup_profile_microservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.printhelloworld.linkup_profile_microservice.repository.jpa")
public class JpaConfig {

}
