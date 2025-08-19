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

@DisplayName("LessThanEqualOperator Tests")
class LessThanEqualOperatorTest {

    private static final Logger logger = LoggerFactory.getLogger(LessThanEqualOperatorTest.class);

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
    @DisplayName("apply_givenValidIntegerField_shouldReturnLessThanEqualCondition")
    void apply_givenValidIntegerField_shouldReturnLessThanEqualCondition() throws Exception {
        String json = """
        {
          "type": "NumericQuery",
          "column": "maximum_age",
          "operator": "lessThanEqual",
          "value": 65
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Integer lessThanEqual SQL: {}", sql);

        assertAll("Integer lessThanEqual validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("maximum_age"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("65"), "SQL should contain value"),
                () -> assertTrue(sql.contains("<="), "SQL should contain less than or equal operator")
        );
    }

    @Test
    @DisplayName("apply_givenValidDoubleField_shouldReturnLessThanEqualCondition")
    void apply_givenValidDoubleField_shouldReturnLessThanEqualCondition() throws Exception {
        String json = """
        {
          "type": "NumericQuery",
          "column": "temperature",
          "operator": "lessThanEqual",
          "value": 37.5
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Double lessThanEqual SQL: {}", sql);

        assertAll("Double lessThanEqual validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("temperature"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("37.5"), "SQL should contain decimal value"),
                () -> assertTrue(sql.contains("<="), "SQL should contain less than or equal operator")
        );
    }

    @Test
    @DisplayName("apply_givenValidDateField_shouldReturnLessThanEqualCondition")
    void apply_givenValidDateField_shouldReturnLessThanEqualCondition() throws Exception {
        String json = """
        {
          "type": "DateQuery",
          "column": "deadline",
          "operator": "lessThanEqual",
          "value": "2025-12-31"
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Date lessThanEqual SQL: {}", sql);

        assertAll("Date lessThanEqual validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("deadline"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("2025-12-31"), "SQL should contain date value"),
                () -> assertTrue(sql.contains("<="), "SQL should contain less than or equal operator")
        );
    }

    @Test
    @DisplayName("apply_givenZeroValue_shouldReturnLessThanEqualCondition")
    void apply_givenZeroValue_shouldReturnLessThanEqualCondition() throws Exception {
        String json = """
        {
          "type": "NumericQuery",
          "column": "deficit",
          "operator": "lessThanEqual",
          "value": 0
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Zero value lessThanEqual SQL: {}", sql);

        assertAll("Zero value lessThanEqual validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("deficit"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("0"), "SQL should contain zero value"),
                () -> assertTrue(sql.contains("<="), "SQL should contain less than or equal operator")
        );
    }

    @Test
    @DisplayName("apply_givenNegativeValue_shouldReturnLessThanEqualCondition")
    void apply_givenNegativeValue_shouldReturnLessThanEqualCondition() throws Exception {
        String json = """
        {
          "type": "NumericQuery",
          "column": "balance",
          "operator": "lessThanEqual",
          "value": -50.25
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Negative value lessThanEqual SQL: {}", sql);

        assertAll("Negative value lessThanEqual validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("balance"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("-50.25"), "SQL should contain negative value"),
                () -> assertTrue(sql.contains("<="), "SQL should contain less than or equal operator")
        );
    }

    @Test
    @DisplayName("apply_givenUnsupportedStringType_shouldThrowException")
    void apply_givenUnsupportedStringType_shouldThrowException() {
        String json = """
        {
          "type": "StringQuery",
          "column": "name",
          "operator": "lessThanEqual",
          "value": "John"
        }
        """;

        assertThrows(Exception.class, () -> {
            Query query = mapper.readValue(json, Query.class);
            query.toCondition(dsl, context);
        }, "Should throw exception for lessThanEqual on String type");
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
              "column": "quantity",
              "operator": "lessThanEqual",
              "value": 1000
            },
            {
              "type": "NumericQuery",
              "column": "price",
              "operator": "lessThanEqual",
              "value": 99.99
            },
            {
              "type": "DateQuery",
              "column": "expiry_date",
              "operator": "lessThanEqual",
              "value": "2025-06-30"
            }
          ]
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Complex nested lessThanEqual SQL: {}", sql);

        assertAll("Complex nested lessThanEqual validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.toLowerCase().contains("and"), "SQL should contain AND operator"),
                () -> assertTrue(sql.contains("quantity"), "SQL should contain quantity column"),
                () -> assertTrue(sql.contains("1000"), "SQL should contain quantity value"),
                () -> assertTrue(sql.contains("price"), "SQL should contain price column"),
                () -> assertTrue(sql.contains("99.99"), "SQL should contain price value"),
                () -> assertTrue(sql.contains("expiry_date"), "SQL should contain date column"),
                () -> assertTrue(sql.contains("2025-06-30"), "SQL should contain date value")
        );
    }
}
