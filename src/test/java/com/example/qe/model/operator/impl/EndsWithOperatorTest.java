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

@DisplayName("EndsWithOperator Tests")
class EndsWithOperatorTest {

    private static final Logger logger = LoggerFactory.getLogger(EndsWithOperatorTest.class);

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
    @DisplayName("apply_givenValidStringFieldWithSuffix_shouldReturnEndsWithCondition")
    void apply_givenValidStringFieldWithSuffix_shouldReturnEndsWithCondition() throws Exception {
        String json = """
                {
                  "type": "StringQuery",
                  "column": "email",
                  "operator": "endsWith",
                  "value": "@gmail.com"
                }
                """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("String endsWith SQL: {}", sql);

        assertAll("String endsWith validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("email"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("@gmail.com") || sql.contains("%@gmail.com"), "SQL should contain suffix value"),
                () -> assertTrue(sql.toLowerCase().contains("like"), "SQL should contain LIKE operator for suffix matching")
        );
    }

    @Test
    @DisplayName("apply_givenValidStringFieldWithFileExtension_shouldReturnEndsWithCondition")
    void apply_givenValidStringFieldWithFileExtension_shouldReturnEndsWithCondition() throws Exception {
        String json = """
                {
                  "type": "StringQuery",
                  "column": "filename",
                  "operator": "endsWith",
                  "value": ".pdf"
                }
                """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("String endsWith file extension SQL: {}", sql);

        assertAll("String endsWith file extension validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("filename"), "SQL should contain column name"),
                () -> assertTrue(sql.contains(".pdf"), "SQL should contain file extension suffix")
        );
    }

    @Test
    @DisplayName("apply_givenUnsupportedIntegerType_shouldThrowException")
    void apply_givenUnsupportedIntegerType_shouldThrowException() {
        String json = """
                {
                  "type": "NumericQuery",
                  "column": "age",
                  "operator": "endsWith",
                  "value": 25
                }
                """;

        assertThrows(Exception.class, () -> {
            Query query = mapper.readValue(json, Query.class);
            query.toCondition(dsl, context);
        }, "Should throw exception for endsWith on Integer type");
    }

    @Test
    @DisplayName("apply_givenComplexNestedQuery_shouldReturnValidCondition")
    void apply_givenComplexNestedQuery_shouldReturnValidCondition() throws Exception {
        String json = """
                {
                  "type": "AndQuery",
                  "children": [
                    {
                      "type": "StringQuery",
                      "column": "email",
                      "operator": "endsWith",
                      "value": "@company.com"
                    },
                    {
                      "type": "StringQuery",
                      "column": "filename",
                      "operator": "endsWith",
                      "value": ".doc"
                    }
                  ]
                }
                """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Complex nested endsWith SQL: {}", sql);

        assertAll("Complex nested endsWith validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.toLowerCase().contains("and"), "SQL should contain AND operator"),
                () -> assertTrue(sql.contains("email"), "SQL should contain email column"),
                () -> assertTrue(sql.contains("@company.com"), "SQL should contain email suffix"),
                () -> assertTrue(sql.contains("filename"), "SQL should contain filename column"),
                () -> assertTrue(sql.contains(".doc"), "SQL should contain file extension suffix")
        );
    }
}
