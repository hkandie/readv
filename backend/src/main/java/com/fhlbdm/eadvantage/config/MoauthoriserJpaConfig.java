package com.fhlbdm.eadvantage.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

/**
 * Standalone JPA wiring for the "moauthoriser" client-credentials store.
 * This is a member oauthoriser datasource.
 */
@Configuration
@ConditionalOnProperty(prefix = "moauthoriser", name = "enabled", havingValue = "true")
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.fhlbdm.eadvantage.api.repository",
        entityManagerFactoryRef = "moauthoriserEntityManagerFactory",
        transactionManagerRef = "moauthoriserTransactionManager")
public class MoauthoriserJpaConfig {

    @Bean
    @ConfigurationProperties("moauthoriser.jdbc")
    public DataSourceProperties moauthoriserDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource moauthoriserDataSource(
            @Qualifier("moauthoriserDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean moauthoriserEntityManagerFactory(
            @Qualifier("moauthoriserDataSource") DataSource dataSource,
            @Value("${moauthoriser.jpa.hibernate.ddl-auto}") String ddlAuto,
            @Value("${moauthoriser.jpa.properties.hibernate.dialect}") String dialect) {

        Map<String, Object> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.hbm2ddl.auto", ddlAuto);
        jpaProperties.put("hibernate.dialect", dialect);

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setPackagesToScan("com.fhlbdm.eadvantage.api.models");
        factoryBean.setPersistenceUnitName("moauthoriser");
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factoryBean.setJpaPropertyMap(jpaProperties);
        return factoryBean;
    }

    @Bean
    public PlatformTransactionManager moauthoriserTransactionManager(
            @Qualifier("moauthoriserEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
