package com.example.qe.config;

import com.example.qe.util.OperatorFactory;
import com.example.qe.util.OperatorRegistry;
import com.example.qe.util.OperatorScanner;
import com.example.qe.util.QueryExecutionContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

@Configuration
public class QueryEngineConfig {

    private static final String OPERATOR_BASE_PACKAGE = "com.example.qe.operator.impl";

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
    public ConversionService conversionService() {
        return new DefaultConversionService();
    }

    @Bean
    public QueryExecutionContext queryExecutionContext(
            OperatorFactory operatorFactory,
            ConversionService conversionService
    ) {
        return new QueryExecutionContext(operatorFactory, conversionService);
    }
}

