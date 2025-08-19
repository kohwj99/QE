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

@DisplayName("EqualsOperator Tests")
class EqualsOperatorTest {

    private static final Logger logger = LoggerFactory.getLogger(EqualsOperatorTest.class);

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
    @DisplayName("apply_givenValidIntegerField_shouldReturnEqualsCondition")
    void apply_givenValidIntegerField_shouldReturnEqualsCondition() throws Exception {
        String json = """
        {
          "type": "NumericQuery",
          "column": "age",
          "operator": "equals",
          "value": 25
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Integer equals SQL: {}", sql);

        assertAll("Integer equals validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("age"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("25"), "SQL should contain value"),
                () -> assertTrue(sql.contains("="), "SQL should contain equals operator")
        );
    }

    @Test
    @DisplayName("apply_givenValidDoubleField_shouldReturnEqualsCondition")
    void apply_givenValidDoubleField_shouldReturnEqualsCondition() throws Exception {
        String json = """
        {
          "type": "NumericQuery",
          "column": "price",
          "operator": "equals",
          "value": 19.99
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Double equals SQL: {}", sql);

        assertAll("Double equals validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("price"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("19.99"), "SQL should contain decimal value"),
                () -> assertTrue(sql.contains("="), "SQL should contain equals operator")
        );
    }

    @Test
    @DisplayName("apply_givenValidStringField_shouldReturnEqualsCondition")
    void apply_givenValidStringField_shouldReturnEqualsCondition() throws Exception {
        String json = """
        {
          "type": "StringQuery",
          "column": "name",
          "operator": "equals",
          "value": "John Doe"
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("String equals SQL: {}", sql);

        assertAll("String equals validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("name"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("John Doe"), "SQL should contain string value"),
                () -> assertTrue(sql.contains("="), "SQL should contain equals operator")
        );
    }

    @Test
    @DisplayName("apply_givenValidBooleanField_shouldReturnEqualsCondition")
    void apply_givenValidBooleanField_shouldReturnEqualsCondition() throws Exception {
        String json = """
        {
          "type": "BoolQuery",
          "column": "active",
          "operator": "equals",
          "value": true
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Boolean equals SQL: {}", sql);

        assertAll("Boolean equals validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("active"), "SQL should contain column name"),
                () -> assertTrue(sql.toLowerCase().contains("true") || sql.contains("1"),
                        "SQL should contain boolean value"),
                () -> assertTrue(sql.contains("="), "SQL should contain equals operator")
        );
    }

    @Test
    @DisplayName("apply_givenValidDateField_shouldReturnEqualsCondition")
    void apply_givenValidDateField_shouldReturnEqualsCondition() throws Exception {
        String json = """
        {
          "type": "DateQuery",
          "column": "created_date",
          "operator": "equals",
          "value": "2025-01-15"
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Date equals SQL: {}", sql);

        assertAll("Date equals validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("created_date"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("2025-01-15"), "SQL should contain date value"),
                () -> assertTrue(sql.contains("="), "SQL should contain equals operator")
        );
    }

    @Test
    @DisplayName("apply_givenZeroValue_shouldReturnEqualsCondition")
    void apply_givenZeroValue_shouldReturnEqualsCondition() throws Exception {
        String json = """
        {
          "type": "NumericQuery",
          "column": "count",
          "operator": "equals",
          "value": 0
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Zero value equals SQL: {}", sql);

        assertAll("Zero value equals validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("count"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("0"), "SQL should contain zero value"),
                () -> assertTrue(sql.contains("="), "SQL should contain equals operator")
        );
    }

    @Test
    @DisplayName("apply_givenNegativeValue_shouldReturnEqualsCondition")
    void apply_givenNegativeValue_shouldReturnEqualsCondition() throws Exception {
        String json = """
        {
          "type": "NumericQuery",
          "column": "balance",
          "operator": "equals",
          "value": -50.25
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Negative value equals SQL: {}", sql);

        assertAll("Negative value equals validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("balance"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("-50.25"), "SQL should contain negative value"),
                () -> assertTrue(sql.contains("="), "SQL should contain equals operator")
        );
    }

    @Test
    @DisplayName("apply_givenEmptyString_shouldReturnEqualsCondition")
    void apply_givenEmptyString_shouldReturnEqualsCondition() throws Exception {
        String json = """
        {
          "type": "StringQuery",
          "column": "description",
          "operator": "equals",
          "value": ""
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Empty string equals SQL: {}", sql);

        assertAll("Empty string equals validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("description"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("="), "SQL should contain equals operator")
        );
    }

    @Test
    @DisplayName("apply_givenSpecialCharacters_shouldReturnEqualsCondition")
    void apply_givenSpecialCharacters_shouldReturnEqualsCondition() throws Exception {
        String json = """
        {
          "type": "StringQuery",
          "column": "code",
          "operator": "equals",
          "value": "ABC-123_$%"
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Special characters equals SQL: {}", sql);

        assertAll("Special characters equals validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("code"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("ABC-123_$%"), "SQL should contain special characters"),
                () -> assertTrue(sql.contains("="), "SQL should contain equals operator")
        );
    }

    @Test
    @DisplayName("apply_givenInvalidOperatorName_shouldThrowException")
    void apply_givenInvalidOperatorName_shouldThrowException() {
        String json = """
        {
          "type": "NumericQuery",
          "column": "age",
          "operator": "invalidEquals",
          "value": 25
        }
        """;

        assertThrows(Exception.class, () -> {
            Query query = mapper.readValue(json, Query.class);
            query.toCondition(dsl, context);
        }, "Should throw exception for invalid operator name");
    }

    @Test
    @DisplayName("apply_givenUnsupportedTypeForEquals_shouldThrowException")
    void apply_givenUnsupportedTypeForEquals_shouldThrowException() {
        // Test with a type that doesn't support equals operator (if any)
        String json = """
        {
          "type": "DateQuery",
          "column": "created_date",
          "operator": "like",
          "value": "2025-01-15"
        }
        """;

        assertThrows(Exception.class, () -> {
            Query query = mapper.readValue(json, Query.class);
            query.toCondition(dsl, context);
        }, "Should throw exception for unsupported operator-type combination");
    }

    @Test
    @DisplayName("apply_givenComplexNestedEquals_shouldReturnValidCondition")
    void apply_givenComplexNestedEquals_shouldReturnValidCondition() throws Exception {
        String json = """
        {
          "type": "AndQuery",
          "children": [
            {
              "type": "StringQuery",
              "column": "category",
              "operator": "equals",
              "value": "Electronics"
            },
            {
              "type": "NumericQuery",
              "column": "stock",
              "operator": "equals",
              "value": 100
            },
            {
              "type": "BoolQuery",
              "column": "available",
              "operator": "equals",
              "value": true
            }
          ]
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Complex nested equals SQL: {}", sql);

        assertAll("Complex nested equals validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.toLowerCase().contains("and"), "SQL should contain AND operator"),
                () -> assertTrue(sql.contains("category"), "SQL should contain category column"),
                () -> assertTrue(sql.contains("Electronics"), "SQL should contain category value"),
                () -> assertTrue(sql.contains("stock"), "SQL should contain stock column"),
                () -> assertTrue(sql.contains("100"), "SQL should contain stock value"),
                () -> assertTrue(sql.contains("available"), "SQL should contain available column")
        );
    }
}
