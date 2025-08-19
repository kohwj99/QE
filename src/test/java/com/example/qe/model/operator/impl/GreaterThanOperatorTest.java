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

@DisplayName("GreaterThanOperator Tests")
class GreaterThanOperatorTest {

    private static final Logger logger = LoggerFactory.getLogger(GreaterThanOperatorTest.class);

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
    @DisplayName("apply_givenValidIntegerField_shouldReturnGreaterThanCondition")
    void apply_givenValidIntegerField_shouldReturnGreaterThanCondition() throws Exception {
        String json = """
        {
          "type": "NumericQuery",
          "column": "age",
          "operator": "greaterThan",
          "value": 18
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Integer greaterThan SQL: {}", sql);

        assertAll("Integer greaterThan validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("age"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("18"), "SQL should contain value"),
                () -> assertTrue(sql.contains(">"), "SQL should contain greater than operator")
        );
    }

    @Test
    @DisplayName("apply_givenValidDoubleField_shouldReturnGreaterThanCondition")
    void apply_givenValidDoubleField_shouldReturnGreaterThanCondition() throws Exception {
        String json = """
        {
          "type": "NumericQuery",
          "column": "price",
          "operator": "greaterThan",
          "value": 99.99
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Double greaterThan SQL: {}", sql);

        assertAll("Double greaterThan validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("price"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("99.99"), "SQL should contain decimal value"),
                () -> assertTrue(sql.contains(">"), "SQL should contain greater than operator")
        );
    }

    @Test
    @DisplayName("apply_givenValidDateField_shouldReturnGreaterThanCondition")
    void apply_givenValidDateField_shouldReturnGreaterThanCondition() throws Exception {
        String json = """
        {
          "type": "DateQuery",
          "column": "order_date",
          "operator": "greaterThan",
          "value": "2025-01-01"
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Date greaterThan SQL: {}", sql);

        assertAll("Date greaterThan validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("order_date"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("2025-01-01"), "SQL should contain date value"),
                () -> assertTrue(sql.contains(">"), "SQL should contain greater than operator")
        );
    }

    @Test
    @DisplayName("apply_givenZeroValue_shouldReturnGreaterThanCondition")
    void apply_givenZeroValue_shouldReturnGreaterThanCondition() throws Exception {
        String json = """
        {
          "type": "NumericQuery",
          "column": "balance",
          "operator": "greaterThan",
          "value": 0
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Zero value greaterThan SQL: {}", sql);

        assertAll("Zero value greaterThan validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("balance"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("0"), "SQL should contain zero value"),
                () -> assertTrue(sql.contains(">"), "SQL should contain greater than operator")
        );
    }

    @Test
    @DisplayName("apply_givenNegativeValue_shouldReturnGreaterThanCondition")
    void apply_givenNegativeValue_shouldReturnGreaterThanCondition() throws Exception {
        String json = """
        {
          "type": "NumericQuery",
          "column": "temperature",
          "operator": "greaterThan",
          "value": -10.5
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Negative value greaterThan SQL: {}", sql);

        assertAll("Negative value greaterThan validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("temperature"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("-10.5"), "SQL should contain negative value"),
                () -> assertTrue(sql.contains(">"), "SQL should contain greater than operator")
        );
    }

    @Test
    @DisplayName("apply_givenLargeValue_shouldReturnGreaterThanCondition")
    void apply_givenLargeValue_shouldReturnGreaterThanCondition() throws Exception {
        String json = """
        {
          "type": "NumericQuery",
          "column": "user_id",
          "operator": "greaterThan",
          "value": 1000000
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Large value greaterThan SQL: {}", sql);

        assertAll("Large value greaterThan validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("user_id"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("1000000"), "SQL should contain large value"),
                () -> assertTrue(sql.contains(">"), "SQL should contain greater than operator")
        );
    }

    @Test
    @DisplayName("apply_givenUnsupportedStringType_shouldThrowException")
    void apply_givenUnsupportedStringType_shouldThrowException() {
        String json = """
        {
          "type": "StringQuery",
          "column": "name",
          "operator": "greaterThan",
          "value": "John"
        }
        """;

        assertThrows(Exception.class, () -> {
            Query query = mapper.readValue(json, Query.class);
            query.toCondition(dsl, context);
        }, "Should throw exception for greaterThan on String type");
    }

    @Test
    @DisplayName("apply_givenInvalidOperatorName_shouldThrowException")
    void apply_givenInvalidOperatorName_shouldThrowException() {
        String json = """
        {
          "type": "NumericQuery",
          "column": "age",
          "operator": "invalidGreaterThan",
          "value": 18
        }
        """;

        assertThrows(Exception.class, () -> {
            Query query = mapper.readValue(json, Query.class);
            query.toCondition(dsl, context);
        }, "Should throw exception for invalid operator name");
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
              "column": "age",
              "operator": "greaterThan",
              "value": 65
            },
            {
              "type": "NumericQuery",
              "column": "salary",
              "operator": "greaterThan",
              "value": 100000.0
            },
            {
              "type": "DateQuery",
              "column": "hire_date",
              "operator": "greaterThan",
              "value": "2020-01-01"
            }
          ]
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Complex nested greaterThan SQL: {}", sql);

        assertAll("Complex nested greaterThan validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.toLowerCase().contains("or"), "SQL should contain OR operator"),
                () -> assertTrue(sql.contains("age"), "SQL should contain age column"),
                () -> assertTrue(sql.contains("65"), "SQL should contain age value"),
                () -> assertTrue(sql.contains("salary"), "SQL should contain salary column"),
                () -> assertTrue(sql.contains("100000"), "SQL should contain salary value"),
                () -> assertTrue(sql.contains("hire_date"), "SQL should contain hire_date column"),
                () -> assertTrue(sql.contains("2020-01-01"), "SQL should contain date value")
        );
    }

    @Test
    @DisplayName("apply_givenScientificNotation_shouldReturnGreaterThanCondition")
    void apply_givenScientificNotation_shouldReturnGreaterThanCondition() throws Exception {
        String json = """
        {
          "type": "NumericQuery",
          "column": "precision_value",
          "operator": "greaterThan",
          "value": 1.23E-4
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Scientific notation greaterThan SQL: {}", sql);

        assertAll("Scientific notation greaterThan validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("precision_value"), "SQL should contain column name"),
                () -> assertTrue(sql.contains(">"), "SQL should contain greater than operator")
        );
    }
}
