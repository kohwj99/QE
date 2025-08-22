package com.example.qe.config;

import com.example.qe.util.OperatorFactory;
import com.example.qe.util.OperatorRegistry;
import com.example.qe.util.OperatorScanner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
