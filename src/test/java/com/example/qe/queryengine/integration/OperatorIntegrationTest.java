// java
package com.example.qe.queryengine.integration;

import com.example.qe.queryengine.operator.OperatorFactory;
import com.example.qe.queryengine.operator.OperatorRegistry;
import com.example.qe.queryengine.operator.OperatorScanner;
import com.example.qe.queryengine.operator.ConditionParser;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class OperatorIntegrationTest {

    protected ConditionParser conditionParser;

    @BeforeAll
    void setupCommon() {
        OperatorRegistry registry = new OperatorRegistry();
        OperatorScanner scanner = new OperatorScanner(registry);
        scanner.scanAndRegister();
        OperatorFactory factory = new OperatorFactory(registry);

        DSLContext dsl = DSL.using(SQLDialect.DEFAULT);
        this.conditionParser = new ConditionParser(factory, dsl);
    }
}