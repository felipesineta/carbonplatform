package com.fv.configuracoes;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;

@Configuration
public class DataSourceDev {

    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3307/carbon");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        return dataSource;
    }
    
    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        EclipseLinkJpaVendorAdapter jpaVendorAdapter = new EclipseLinkJpaVendorAdapter();
        jpaVendorAdapter.setGenerateDdl(true);
        jpaVendorAdapter.setShowSql(true);

        return jpaVendorAdapter;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setPackagesToScan("com.fv.models");
        factory.setDataSource(dataSource());
        factory.setJpaVendorAdapter(jpaVendorAdapter());

        factory.getJpaPropertyMap().put(PersistenceUnitProperties.WEAVING, "false");
        factory.getJpaPropertyMap().put(PersistenceUnitProperties.LOGGING_LEVEL, "FINE");
//        factory.getJpaPropertyMap().put(PersistenceUnitProperties.DDL_GENERATION, "drop-and-create-tables");
        factory.getJpaPropertyMap().put(PersistenceUnitProperties.DDL_GENERATION, "create-or-extend-tables");
        
        factory.afterPropertiesSet();
        return factory.getObject();
    }
}
