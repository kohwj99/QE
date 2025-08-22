package com.example.qe.model.query.impl;

import com.example.qe.util.OperatorFactory;
import com.example.qe.util.OperatorRegistry;
import com.example.qe.util.OperatorScanner;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class NumericQueryTest {

    private static final Logger logger = LoggerFactory.getLogger(NumericQueryTest.class);

    private ObjectMapper mapper;
    private DSLContext dsl;
    private OperatorFactory operatorFactory;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        dsl = DSL.using(SQLDialect.DEFAULT);

        // Setup OperatorRegistry and scan operators
        OperatorRegistry registry = new OperatorRegistry();
        OperatorScanner scanner = new OperatorScanner(registry);
        scanner.scanAndRegister("com.example.qe.model.operator");

        OperatorFactory factory = new OperatorFactory(registry);
        operatorFactory = factory;

        logger.info("Setup complete for NumericQuery testing");
    }

    @Test
    void testNumericQueryWithInteger() throws Exception {
        String json = """
        {
          "type": "NumericQuery",
          "column": "age",
          "operator": "equals",
          "value": 25
        }
        """;

        NumericQuery query = mapper.readValue(json, NumericQuery.class);
        Condition condition = query.toCondition(dsl, operatorFactory);
        String sql = dsl.renderInlined(condition);

        logger.info("Integer NumericQuery SQL: {}", sql);

        assertAll("Integer value validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("age"), "SQL should contain column 'age'"),
                () -> assertTrue(sql.contains("25"), "SQL should contain value '25'"),
                () -> assertTrue(sql.contains("="), "SQL should contain equals operator")
        );
    }

    @Test
    void testNumericQueryWithDecimal() throws Exception {
        String json = """
        {
          "type": "NumericQuery",
          "column": "price",
          "operator": "greaterThan",
          "value": 99.99
        }
        """;

        NumericQuery query = mapper.readValue(json, NumericQuery.class);
        Condition condition = query.toCondition(dsl, operatorFactory);
        String sql = dsl.renderInlined(condition);

        logger.info("Decimal NumericQuery SQL: {}", sql);

        assertAll("Decimal value validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("price"), "SQL should contain column 'price'"),
                () -> assertTrue(sql.contains("99.99"), "SQL should contain decimal value '99.99'"),
                () -> assertTrue(sql.contains(">"), "SQL should contain greater than operator")
        );
    }

    @Test
    void testNumericQueryWithLargeDecimal() throws Exception {
        String json = """
        {
          "type": "NumericQuery",
          "column": "revenue",
          "operator": "lessThanEqual",
          "value": 1234567.89
        }
        """;

        NumericQuery query = mapper.readValue(json, NumericQuery.class);
        Condition condition = query.toCondition(dsl, operatorFactory);
        String sql = dsl.renderInlined(condition);

        logger.info("Large decimal NumericQuery SQL: {}", sql);

        assertAll("Large decimal value validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("revenue"), "SQL should contain column 'revenue'"),
                () -> assertTrue(sql.contains("1234567.89"), "SQL should contain large decimal value"),
                () -> assertTrue(sql.contains("<="), "SQL should contain less than or equal operator")
        );
    }

    @Test
    void testNumericQueryWithNegativeValues() throws Exception {
        String json = """
        {
          "type": "NumericQuery",
          "column": "balance",
          "operator": "greaterThanEqual",
          "value": -500.25
        }
        """;

        NumericQuery query = mapper.readValue(json, NumericQuery.class);
        Condition condition = query.toCondition(dsl, operatorFactory);
        String sql = dsl.renderInlined(condition);

        logger.info("Negative value NumericQuery SQL: {}", sql);

        assertAll("Negative value validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("balance"), "SQL should contain column 'balance'"),
                () -> assertTrue(sql.contains("-500.25"), "SQL should contain negative decimal value"),
                () -> assertTrue(sql.contains(">="), "SQL should contain greater than or equal operator")
        );
    }

    @Test
    void testNumericQueryWithZero() throws Exception {
        String json = """
        {
          "type": "NumericQuery",
          "column": "score",
          "operator": "notEquals",
          "value": 0
        }
        """;

        NumericQuery query = mapper.readValue(json, NumericQuery.class);
        Condition condition = query.toCondition(dsl, operatorFactory);
        String sql = dsl.renderInlined(condition);

        logger.info("Zero value NumericQuery SQL: {}", sql);

        assertAll("Zero value validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("score"), "SQL should contain column 'score'"),
                () -> assertTrue(sql.contains("0"), "SQL should contain zero value"),
                () -> assertTrue(sql.contains("<>") || sql.contains("!="), "SQL should contain not equals operator")
        );
    }

    @Test
    void testNumericQueryPrecision() throws Exception {
        String json = """
        {
          "type": "NumericQuery",
          "column": "precise_value",
          "operator": "equals",
          "value": 123.456789
        }
        """;

        NumericQuery query = mapper.readValue(json, NumericQuery.class);
        Condition condition = query.toCondition(dsl, operatorFactory);
        String sql = dsl.renderInlined(condition);

        logger.info("High precision NumericQuery SQL: {}", sql);

        assertAll("High precision value validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("precise_value"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("123.456789"), "SQL should contain high precision value"),
                () -> assertTrue(sql.contains("="), "SQL should contain equals operator")
        );
    }

    @Test
    void testComplexNumericQueryCombination() throws Exception {
        String json = """
        {
          "type": "AndQuery",
          "children": [
            {
              "type": "NumericQuery",
              "column": "age",
              "operator": "greaterThanEqual",
              "value": 18
            },
            {
              "type": "NumericQuery",
              "column": "salary",
              "operator": "lessThan",
              "value": 100000.50
            },
            {
              "type": "OrQuery",
              "children": [
                {
                  "type": "NumericQuery",
                  "column": "bonus",
                  "operator": "equals",
                  "value": 0
                },
                {
                  "type": "NumericQuery",
                  "column": "commission",
                  "operator": "greaterThan",
                  "value": 1500.75
                }
              ]
            }
          ]
        }
        """;

        var query = mapper.readValue(json, com.example.qe.model.query.Query.class);
        Condition condition = query.toCondition(dsl, operatorFactory);
        String sql = dsl.renderInlined(condition);

        logger.info("Complex numeric query combination SQL: {}", sql);

        assertAll("Complex query validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.toLowerCase().contains("and"), "SQL should contain AND operator"),
                () -> assertTrue(sql.toLowerCase().contains("or"), "SQL should contain OR operator"),
                () -> assertTrue(sql.contains("age"), "SQL should contain age column"),
                () -> assertTrue(sql.contains("salary"), "SQL should contain salary column"),
                () -> assertTrue(sql.contains("bonus"), "SQL should contain bonus column"),
                () -> assertTrue(sql.contains("commission"), "SQL should contain commission column"),
                () -> assertTrue(sql.contains("18"), "SQL should contain integer value 18"),
                () -> assertTrue(sql.contains("100000.5"), "SQL should contain decimal salary value"),
                () -> assertTrue(sql.contains("1500.75"), "SQL should contain decimal commission value")
        );
    }

    @Test
    void testNumericQueryValueClass() {
        NumericQuery query = new NumericQuery("test_column", "equals", new BigDecimal("42.5"));

        assertEquals(BigDecimal.class, query.getValueClass(),
                "NumericQuery should return BigDecimal.class as value class");

        logger.info("✓ NumericQuery correctly uses BigDecimal as value class");
    }
}
