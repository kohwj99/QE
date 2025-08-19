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

@DisplayName("GreaterThanEqualOperator Tests")
class GreaterThanEqualOperatorTest {

    private static final Logger logger = LoggerFactory.getLogger(GreaterThanEqualOperatorTest.class);

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
    @DisplayName("apply_givenValidIntegerField_shouldReturnGreaterThanEqualCondition")
    void apply_givenValidIntegerField_shouldReturnGreaterThanEqualCondition() throws Exception {
        String json = """
        {
          "type": "NumericQuery",
          "column": "minimum_age",
          "operator": "greaterThanEqual",
          "value": 18
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Integer greaterThanEqual SQL: {}", sql);

        assertAll("Integer greaterThanEqual validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("minimum_age"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("18"), "SQL should contain value"),
                () -> assertTrue(sql.contains(">="), "SQL should contain greater than or equal operator")
        );
    }

    @Test
    @DisplayName("apply_givenValidDoubleField_shouldReturnGreaterThanEqualCondition")
    void apply_givenValidDoubleField_shouldReturnGreaterThanEqualCondition() throws Exception {
        String json = """
        {
          "type": "NumericQuery",
          "column": "rating",
          "operator": "greaterThanEqual",
          "value": 4.0
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Double greaterThanEqual SQL: {}", sql);

        assertAll("Double greaterThanEqual validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("rating"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("4") || sql.contains("4.0"), "SQL should contain decimal value"),
                () -> assertTrue(sql.contains(">="), "SQL should contain greater than or equal operator")
        );
    }

    @Test
    @DisplayName("apply_givenValidDateField_shouldReturnGreaterThanEqualCondition")
    void apply_givenValidDateField_shouldReturnGreaterThanEqualCondition() throws Exception {
        String json = """
        {
          "type": "DateQuery",
          "column": "start_date",
          "operator": "greaterThanEqual",
          "value": "2025-01-01"
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Date greaterThanEqual SQL: {}", sql);

        assertAll("Date greaterThanEqual validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("start_date"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("2025-01-01"), "SQL should contain date value"),
                () -> assertTrue(sql.contains(">="), "SQL should contain greater than or equal operator")
        );
    }

    @Test
    @DisplayName("apply_givenZeroValue_shouldReturnGreaterThanEqualCondition")
    void apply_givenZeroValue_shouldReturnGreaterThanEqualCondition() throws Exception {
        String json = """
        {
          "type": "NumericQuery",
          "column": "score",
          "operator": "greaterThanEqual",
          "value": 0
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Zero value greaterThanEqual SQL: {}", sql);

        assertAll("Zero value greaterThanEqual validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("score"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("0"), "SQL should contain zero value"),
                () -> assertTrue(sql.contains(">="), "SQL should contain greater than or equal operator")
        );
    }

    @Test
    @DisplayName("apply_givenUnsupportedStringType_shouldThrowException")
    void apply_givenUnsupportedStringType_shouldThrowException() {
        String json = """
        {
          "type": "StringQuery",
          "column": "name",
          "operator": "greaterThanEqual",
          "value": "John"
        }
        """;

        assertThrows(Exception.class, () -> {
            Query query = mapper.readValue(json, Query.class);
            query.toCondition(dsl, context);
        }, "Should throw exception for greaterThanEqual on String type");
    }

    @Test
    @DisplayName("apply_givenComplexNestedQuery_shouldReturnValidCondition")
    void apply_givenComplexNestedQuery_shouldReturnValidCondition() throws Exception {
        String json = """
        {
          "type": "OrQuery",
          "children": [
            {
              "type": "NumericQuery",
              "column": "experience_years",
              "operator": "greaterThanEqual",
              "value": 5
            },
            {
              "type": "NumericQuery",
              "column": "salary",
              "operator": "greaterThanEqual",
              "value": 75000.0
            }
          ]
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Complex nested greaterThanEqual SQL: {}", sql);

        assertAll("Complex nested greaterThanEqual validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.toLowerCase().contains("or"), "SQL should contain OR operator"),
                () -> assertTrue(sql.contains("experience_years"), "SQL should contain experience_years column"),
                () -> assertTrue(sql.contains("5"), "SQL should contain experience value"),
                () -> assertTrue(sql.contains("salary"), "SQL should contain salary column"),
                () -> assertTrue(sql.contains("75000"), "SQL should contain salary value")
        );
    }
}
