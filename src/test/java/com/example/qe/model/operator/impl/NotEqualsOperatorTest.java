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

@DisplayName("NotEqualsOperator Tests")
class NotEqualsOperatorTest {

    private static final Logger logger = LoggerFactory.getLogger(NotEqualsOperatorTest.class);

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
    @DisplayName("apply_givenValidIntegerField_shouldReturnNotEqualsCondition")
    void apply_givenValidIntegerField_shouldReturnNotEqualsCondition() throws Exception {
        String json = """
        {
          "type": "NumericQuery",
          "column": "status_code",
          "operator": "notEquals",
          "value": 0
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Integer notEquals SQL: {}", sql);

        assertAll("Integer notEquals validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("status_code"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("0"), "SQL should contain value"),
                () -> assertTrue(sql.contains("<>") || sql.contains("!="), "SQL should contain not equals operator")
        );
    }

    @Test
    @DisplayName("apply_givenValidDoubleField_shouldReturnNotEqualsCondition")
    void apply_givenValidDoubleField_shouldReturnNotEqualsCondition() throws Exception {
        String json = """
        {
          "type": "NumericQuery",
          "column": "discount",
          "operator": "notEquals",
          "value": 0.0
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Double notEquals SQL: {}", sql);

        assertAll("Double notEquals validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("discount"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("0") || sql.contains("0.0"), "SQL should contain zero value"),
                () -> assertTrue(sql.contains("<>") || sql.contains("!="), "SQL should contain not equals operator")
        );
    }

    @Test
    @DisplayName("apply_givenValidStringField_shouldReturnNotEqualsCondition")
    void apply_givenValidStringField_shouldReturnNotEqualsCondition() throws Exception {
        String json = """
        {
          "type": "StringQuery",
          "column": "status",
          "operator": "notEquals",
          "value": "deleted"
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("String notEquals SQL: {}", sql);

        assertAll("String notEquals validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("status"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("deleted"), "SQL should contain string value"),
                () -> assertTrue(sql.contains("<>") || sql.contains("!="), "SQL should contain not equals operator")
        );
    }

    @Test
    @DisplayName("apply_givenValidBooleanField_shouldReturnNotEqualsCondition")
    void apply_givenValidBooleanField_shouldReturnNotEqualsCondition() throws Exception {
        String json = """
        {
          "type": "BoolQuery",
          "column": "is_archived",
          "operator": "notEquals",
          "value": true
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Boolean notEquals SQL: {}", sql);

        assertAll("Boolean notEquals validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("is_archived"), "SQL should contain column name"),
                () -> assertTrue(sql.toLowerCase().contains("true") || sql.contains("1"), "SQL should contain boolean value"),
                () -> assertTrue(sql.contains("<>") || sql.contains("!="), "SQL should contain not equals operator")
        );
    }

    @Test
    @DisplayName("apply_givenValidDateField_shouldReturnNotEqualsCondition")
    void apply_givenValidDateField_shouldReturnNotEqualsCondition() throws Exception {
        String json = """
        {
          "type": "DateQuery",
          "column": "deleted_date",
          "operator": "notEquals",
          "value": "1970-01-01"
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Date notEquals SQL: {}", sql);

        assertAll("Date notEquals validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("deleted_date"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("1970-01-01"), "SQL should contain date value"),
                () -> assertTrue(sql.contains("<>") || sql.contains("!="), "SQL should contain not equals operator")
        );
    }

    @Test
    @DisplayName("apply_givenNegativeValue_shouldReturnNotEqualsCondition")
    void apply_givenNegativeValue_shouldReturnNotEqualsCondition() throws Exception {
        String json = """
        {
          "type": "NumericQuery",
          "column": "balance",
          "operator": "notEquals",
          "value": -1.0
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Negative value notEquals SQL: {}", sql);

        assertAll("Negative value notEquals validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("balance"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("-1") || sql.contains("-1.0"), "SQL should contain negative value"),
                () -> assertTrue(sql.contains("<>") || sql.contains("!="), "SQL should contain not equals operator")
        );
    }

    @Test
    @DisplayName("apply_givenEmptyString_shouldReturnNotEqualsCondition")
    void apply_givenEmptyString_shouldReturnNotEqualsCondition() throws Exception {
        String json = """
        {
          "type": "StringQuery",
          "column": "notes",
          "operator": "notEquals",
          "value": ""
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Empty string notEquals SQL: {}", sql);

        assertAll("Empty string notEquals validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("notes"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("<>") || sql.contains("!="), "SQL should contain not equals operator")
        );
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
              "column": "status",
              "operator": "notEquals",
              "value": "deleted"
            },
            {
              "type": "NumericQuery",
              "column": "error_count",
              "operator": "notEquals",
              "value": 0
            },
            {
              "type": "BoolQuery",
              "column": "is_test",
              "operator": "notEquals",
              "value": true
            }
          ]
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Complex nested notEquals SQL: {}", sql);

        assertAll("Complex nested notEquals validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.toLowerCase().contains("and"), "SQL should contain AND operator"),
                () -> assertTrue(sql.contains("status"), "SQL should contain status column"),
                () -> assertTrue(sql.contains("deleted"), "SQL should contain deleted value"),
                () -> assertTrue(sql.contains("error_count"), "SQL should contain error_count column"),
                () -> assertTrue(sql.contains("0"), "SQL should contain zero value"),
                () -> assertTrue(sql.contains("is_test"), "SQL should contain is_test column")
        );
    }
}
