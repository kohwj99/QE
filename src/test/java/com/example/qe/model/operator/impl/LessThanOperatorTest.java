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

@DisplayName("LessThanOperator Tests")
class LessThanOperatorTest {

    private static final Logger logger = LoggerFactory.getLogger(LessThanOperatorTest.class);

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
    @DisplayName("apply_givenValidIntegerField_shouldReturnLessThanCondition")
    void apply_givenValidIntegerField_shouldReturnLessThanCondition() throws Exception {
        String json = """
        {
          "type": "NumericQuery",
          "column": "quantity",
          "operator": "lessThan",
          "value": 100
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Integer lessThan SQL: {}", sql);

        assertAll("Integer lessThan validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("quantity"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("100"), "SQL should contain value"),
                () -> assertTrue(sql.contains("<"), "SQL should contain less than operator")
        );
    }

    @Test
    @DisplayName("apply_givenValidDoubleField_shouldReturnLessThanCondition")
    void apply_givenValidDoubleField_shouldReturnLessThanCondition() throws Exception {
        String json = """
        {
          "type": "NumericQuery",
          "column": "weight",
          "operator": "lessThan",
          "value": 75.5
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Double lessThan SQL: {}", sql);

        assertAll("Double lessThan validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("weight"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("75.5"), "SQL should contain decimal value"),
                () -> assertTrue(sql.contains("<"), "SQL should contain less than operator")
        );
    }

    @Test
    @DisplayName("apply_givenValidDateField_shouldReturnLessThanCondition")
    void apply_givenValidDateField_shouldReturnLessThanCondition() throws Exception {
        String json = """
        {
          "type": "DateQuery",
          "column": "expiry_date",
          "operator": "lessThan",
          "value": "2025-12-31"
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Date lessThan SQL: {}", sql);

        assertAll("Date lessThan validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("expiry_date"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("2025-12-31"), "SQL should contain date value"),
                () -> assertTrue(sql.contains("<"), "SQL should contain less than operator")
        );
    }

    @Test
    @DisplayName("apply_givenZeroValue_shouldReturnLessThanCondition")
    void apply_givenZeroValue_shouldReturnLessThanCondition() throws Exception {
        String json = """
        {
          "type": "NumericQuery",
          "column": "deficit",
          "operator": "lessThan",
          "value": 0
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Zero value lessThan SQL: {}", sql);

        assertAll("Zero value lessThan validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("deficit"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("0"), "SQL should contain zero value"),
                () -> assertTrue(sql.contains("<"), "SQL should contain less than operator")
        );
    }

    @Test
    @DisplayName("apply_givenNegativeValue_shouldReturnLessThanCondition")
    void apply_givenNegativeValue_shouldReturnLessThanCondition() throws Exception {
        String json = """
        {
          "type": "NumericQuery",
          "column": "temperature",
          "operator": "lessThan",
          "value": -5.75
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Negative value lessThan SQL: {}", sql);

        assertAll("Negative value lessThan validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("temperature"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("-5.75"), "SQL should contain negative value"),
                () -> assertTrue(sql.contains("<"), "SQL should contain less than operator")
        );
    }

    @Test
    @DisplayName("apply_givenUnsupportedStringType_shouldThrowException")
    void apply_givenUnsupportedStringType_shouldThrowException() {
        String json = """
        {
          "type": "StringQuery",
          "column": "name",
          "operator": "lessThan",
          "value": "John"
        }
        """;

        assertThrows(Exception.class, () -> {
            Query query = mapper.readValue(json, Query.class);
            query.toCondition(dsl, context);
        }, "Should throw exception for lessThan on String type");
    }

    @Test
    @DisplayName("apply_givenComplexNestedQuery_shouldReturnValidCondition")
    void apply_givenComplexNestedQuery_shouldReturnValidCondition() throws Exception {
        String json = """
        {
          "type": "AndQuery",
          "children": [
            {
              "type": "NumericQuery",
              "column": "stock",
              "operator": "lessThan",
              "value": 50
            },
            {
              "type": "NumericQuery",
              "column": "price",
              "operator": "lessThan",
              "value": 29.99
            },
            {
              "type": "DateQuery",
              "column": "last_updated",
              "operator": "lessThan",
              "value": "2025-01-01"
            }
          ]
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Complex nested lessThan SQL: {}", sql);

        assertAll("Complex nested lessThan validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.toLowerCase().contains("and"), "SQL should contain AND operator"),
                () -> assertTrue(sql.contains("stock"), "SQL should contain stock column"),
                () -> assertTrue(sql.contains("50"), "SQL should contain stock value"),
                () -> assertTrue(sql.contains("price"), "SQL should contain price column"),
                () -> assertTrue(sql.contains("29.99"), "SQL should contain price value"),
                () -> assertTrue(sql.contains("last_updated"), "SQL should contain date column"),
                () -> assertTrue(sql.contains("2025-01-01"), "SQL should contain date value")
        );
    }
}
