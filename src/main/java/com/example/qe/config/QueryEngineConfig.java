package com.example.qe.config;

import com.example.qe.util.OperatorFactory;
import com.example.qe.util.OperatorRegistry;
import com.example.qe.util.OperatorScanner;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class QueryEngineConfig {

    private static final String OPERATOR_BASE_PACKAGE = "com.example.qe.model.operator.impl";

    @Bean
    public OperatorRegistry operatorRegistry() {
        return new OperatorRegistry();
    }

    @Bean
    public OperatorScanner operatorScanner(OperatorRegistry operatorRegistry) {
        OperatorScanner scanner = new OperatorScanner(operatorRegistry);
        scanner.scanAndRegister(OPERATOR_BASE_PACKAGE);
        return scanner;
    }

    @Bean
    public OperatorFactory operatorFactory(OperatorRegistry operatorRegistry) {
        return new OperatorFactory(operatorRegistry);
    }

    @Bean
    public DataSource dataSource(
            @Value("${spring.datasource.url}") String url,
            @Value("${spring.datasource.username}") String username,
            @Value("${spring.datasource.password}") String password,
            @Value("${spring.datasource.driver-class-name}") String driverClassName) {

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driverClassName);

        return new HikariDataSource(config);
    }

    @Bean
    public DSLContext dslContext(DataSource dataSource) {
        return DSL.using(dataSource, SQLDialect.DEFAULT);
    }
}
