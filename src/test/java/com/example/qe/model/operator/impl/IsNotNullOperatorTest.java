package com.example.qe.model.operator.impl;

import com.example.qe.model.query.Query;
import com.example.qe.util.OperatorFactory;
import com.example.qe.util.OperatorRegistry;
import com.example.qe.util.OperatorScanner;
import com.example.qe.util.QueryExecutionContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.support.DefaultConversionService;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("IsNotNullOperator Tests")
class IsNotNullOperatorTest {

    private static final Logger logger = LoggerFactory.getLogger(IsNotNullOperatorTest.class);

    private ObjectMapper mapper;
    private DSLContext dsl;
    private QueryExecutionContext context;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        dsl = DSL.using(SQLDialect.DEFAULT);

        OperatorRegistry registry = new OperatorRegistry();
        OperatorScanner scanner = new OperatorScanner(registry);
        scanner.scanAndRegister("com.example.qe.model.operator");

        OperatorFactory factory = new OperatorFactory(registry);
        context = new QueryExecutionContext(factory, new DefaultConversionService());
    }

    @Test
    @DisplayName("apply_givenValidStringField_shouldReturnIsNotNullCondition")
    void apply_givenValidStringField_shouldReturnIsNotNullCondition() throws Exception {
        String json = """
        {
          "type": "StringQuery",
          "column": "email",
          "operator": "isNotNull",
          "value": null
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("String isNotNull SQL: {}", sql);

        assertAll("String isNotNull validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("email"), "SQL should contain column name"),
                () -> assertTrue(sql.toLowerCase().contains("is not null"), "SQL should contain IS NOT NULL operator")
        );
    }

    @Test
    @DisplayName("apply_givenValidIntegerField_shouldReturnIsNotNullCondition")
    void apply_givenValidIntegerField_shouldReturnIsNotNullCondition() throws Exception {
        String json = """
        {
          "type": "NumericQuery",
          "column": "parent_id",
          "operator": "isNotNull",
          "value": null
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Integer isNotNull SQL: {}", sql);

        assertAll("Integer isNotNull validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("parent_id"), "SQL should contain column name"),
                () -> assertTrue(sql.toLowerCase().contains("is not null"), "SQL should contain IS NOT NULL operator")
        );
    }

    @Test
    @DisplayName("apply_givenComplexNestedQuery_shouldReturnValidCondition")
    void apply_givenComplexNestedQuery_shouldReturnValidCondition() throws Exception {
        String json = """
        {
          "type": "OrQuery",
          "children": [
            {
              "type": "StringQuery",
              "column": "phone",
              "operator": "isNotNull",
              "value": null
            },
            {
              "type": "StringQuery",
              "column": "email",
              "operator": "isNotNull",
              "value": null
            }
          ]
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Complex nested isNotNull SQL: {}", sql);

        assertAll("Complex nested isNotNull validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.toLowerCase().contains("or"), "SQL should contain OR operator"),
                () -> assertTrue(sql.contains("phone"), "SQL should contain phone column"),
                () -> assertTrue(sql.contains("email"), "SQL should contain email column"),
                () -> assertTrue(sql.toLowerCase().contains("is not null"), "SQL should contain IS NOT NULL operators")
        );
    }
}
