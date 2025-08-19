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

@DisplayName("IsNullOperator Tests")
class IsNullOperatorTest {

    private static final Logger logger = LoggerFactory.getLogger(IsNullOperatorTest.class);

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
    @DisplayName("apply_givenValidStringField_shouldReturnIsNullCondition")
    void apply_givenValidStringField_shouldReturnIsNullCondition() throws Exception {
        String json = """
        {
          "type": "StringQuery",
          "column": "description",
          "operator": "isNull",
          "value": null
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("String isNull SQL: {}", sql);

        assertAll("String isNull validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("description"), "SQL should contain column name"),
                () -> assertTrue(sql.toLowerCase().contains("is null"), "SQL should contain IS NULL operator")
        );
    }

    @Test
    @DisplayName("apply_givenValidIntegerField_shouldReturnIsNullCondition")
    void apply_givenValidIntegerField_shouldReturnIsNullCondition() throws Exception {
        String json = """
        {
          "type": "NumericQuery",
          "column": "manager_id",
          "operator": "isNull",
          "value": null
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Integer isNull SQL: {}", sql);

        assertAll("Integer isNull validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("manager_id"), "SQL should contain column name"),
                () -> assertTrue(sql.toLowerCase().contains("is null"), "SQL should contain IS NULL operator")
        );
    }

    @Test
    @DisplayName("apply_givenValidDateField_shouldReturnIsNullCondition")
    void apply_givenValidDateField_shouldReturnIsNullCondition() throws Exception {
        String json = """
        {
          "type": "DateQuery",
          "column": "deleted_at",
          "operator": "isNull",
          "value": null
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Date isNull SQL: {}", sql);

        assertAll("Date isNull validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("deleted_at"), "SQL should contain column name"),
                () -> assertTrue(sql.toLowerCase().contains("is null"), "SQL should contain IS NULL operator")
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
              "column": "middle_name",
              "operator": "isNull",
              "value": null
            },
            {
              "type": "DateQuery",
              "column": "archived_date",
              "operator": "isNull",
              "value": null
            }
          ]
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Complex nested isNull SQL: {}", sql);

        assertAll("Complex nested isNull validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.toLowerCase().contains("and"), "SQL should contain AND operator"),
                () -> assertTrue(sql.contains("middle_name"), "SQL should contain middle_name column"),
                () -> assertTrue(sql.contains("archived_date"), "SQL should contain archived_date column"),
                () -> assertTrue(sql.toLowerCase().contains("is null"), "SQL should contain IS NULL operators")
        );
    }
}
