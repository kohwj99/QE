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

@DisplayName("StartsWithOperator Tests")
class StartsWithOperatorTest {

    private static final Logger logger = LoggerFactory.getLogger(StartsWithOperatorTest.class);

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
    @DisplayName("apply_givenValidStringFieldWithPrefix_shouldReturnStartsWithCondition")
    void apply_givenValidStringFieldWithPrefix_shouldReturnStartsWithCondition() throws Exception {
        String json = """
        {
          "type": "StringQuery",
          "column": "full_name",
          "operator": "startsWith",
          "value": "John"
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("String startsWith SQL: {}", sql);

        assertAll("String startsWith validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("full_name"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("John") || sql.contains("John%"), "SQL should contain prefix value"),
                () -> assertTrue(sql.toLowerCase().contains("like"), "SQL should contain LIKE operator for prefix matching")
        );
    }

    @Test
    @DisplayName("apply_givenValidStringFieldWithEmptyPrefix_shouldReturnStartsWithCondition")
    void apply_givenValidStringFieldWithEmptyPrefix_shouldReturnStartsWithCondition() throws Exception {
        String json = """
        {
          "type": "StringQuery",
          "column": "title",
          "operator": "startsWith",
          "value": ""
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("String startsWith empty prefix SQL: {}", sql);

        assertAll("String startsWith empty prefix validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("title"), "SQL should contain column name")
        );
    }

    @Test
    @DisplayName("apply_givenValidStringFieldWithSpecialCharacters_shouldReturnStartsWithCondition")
    void apply_givenValidStringFieldWithSpecialCharacters_shouldReturnStartsWithCondition() throws Exception {
        String json = """
        {
          "type": "StringQuery",
          "column": "product_code",
          "operator": "startsWith",
          "value": "ABC-123"
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("String startsWith with special characters SQL: {}", sql);

        assertAll("String startsWith with special characters validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("product_code"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("ABC-123"), "SQL should contain special characters in prefix")
        );
    }

    @Test
    @DisplayName("apply_givenValidStringFieldWithSingleCharacter_shouldReturnStartsWithCondition")
    void apply_givenValidStringFieldWithSingleCharacter_shouldReturnStartsWithCondition() throws Exception {
        String json = """
        {
          "type": "StringQuery",
          "column": "category",
          "operator": "startsWith",
          "value": "A"
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("String startsWith single character SQL: {}", sql);

        assertAll("String startsWith single character validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("category"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("A"), "SQL should contain single character prefix")
        );
    }

    @Test
    @DisplayName("apply_givenValidStringFieldWithNumbers_shouldReturnStartsWithCondition")
    void apply_givenValidStringFieldWithNumbers_shouldReturnStartsWithCondition() throws Exception {
        String json = """
        {
          "type": "StringQuery",
          "column": "order_id",
          "operator": "startsWith",
          "value": "2025"
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("String startsWith with numbers SQL: {}", sql);

        assertAll("String startsWith with numbers validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("order_id"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("2025"), "SQL should contain numeric prefix")
        );
    }

    @Test
    @DisplayName("apply_givenUnsupportedIntegerType_shouldThrowException")
    void apply_givenUnsupportedIntegerType_shouldThrowException() {
        String json = """
        {
          "type": "NumericQuery",
          "column": "age",
          "operator": "startsWith",
          "value": 25
        }
        """;

        assertThrows(Exception.class, () -> {
            Query query = mapper.readValue(json, Query.class);
            query.toCondition(dsl, context);
        }, "Should throw exception for startsWith on Integer type");
    }

    @Test
    @DisplayName("apply_givenUnsupportedDateType_shouldThrowException")
    void apply_givenUnsupportedDateType_shouldThrowException() {
        String json = """
        {
          "type": "DateQuery",
          "column": "created_date",
          "operator": "startsWith",
          "value": "2025-01-01"
        }
        """;

        assertThrows(Exception.class, () -> {
            Query query = mapper.readValue(json, Query.class);
            query.toCondition(dsl, context);
        }, "Should throw exception for startsWith on Date type");
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
              "column": "first_name",
              "operator": "startsWith",
              "value": "John"
            },
            {
              "type": "StringQuery",
              "column": "last_name",
              "operator": "startsWith",
              "value": "Smith"
            },
            {
              "type": "StringQuery",
              "column": "email",
              "operator": "startsWith",
              "value": "admin"
            }
          ]
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Complex nested startsWith SQL: {}", sql);

        assertAll("Complex nested startsWith validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.toLowerCase().contains("or"), "SQL should contain OR operator"),
                () -> assertTrue(sql.contains("first_name"), "SQL should contain first_name column"),
                () -> assertTrue(sql.contains("John"), "SQL should contain John prefix"),
                () -> assertTrue(sql.contains("last_name"), "SQL should contain last_name column"),
                () -> assertTrue(sql.contains("Smith"), "SQL should contain Smith prefix"),
                () -> assertTrue(sql.contains("email"), "SQL should contain email column"),
                () -> assertTrue(sql.contains("admin"), "SQL should contain admin prefix")
        );
    }

    @Test
    @DisplayName("apply_givenCaseSensitivePrefix_shouldReturnStartsWithCondition")
    void apply_givenCaseSensitivePrefix_shouldReturnStartsWithCondition() throws Exception {
        String json = """
        {
          "type": "StringQuery",
          "column": "status_code",
          "operator": "startsWith",
          "value": "HTTP"
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Case sensitive startsWith SQL: {}", sql);

        assertAll("Case sensitive startsWith validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("status_code"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("HTTP"), "SQL should preserve case in prefix")
        );
    }
}
