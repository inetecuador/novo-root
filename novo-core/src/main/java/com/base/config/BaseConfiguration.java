package com.base.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * BaseConfiguration.
 *
 * @author vsangucho on 10/03/2023
 * @version 1.0
 * @since 1.0.0
 */
@Configuration
@EnableJpaRepositories(basePackages = {"com.base.repository"})
@EntityScan(basePackages = {"com.base.entity"})
@ComponentScan(basePackages = {"com.base"})
@EnableTransactionManagement
public class BaseConfiguration {


}
