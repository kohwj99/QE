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

@DisplayName("LikeOperator Tests")
class LikeOperatorTest {

    private static final Logger logger = LoggerFactory.getLogger(LikeOperatorTest.class);

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
    @DisplayName("apply_givenValidStringFieldWithWildcard_shouldReturnLikeCondition")
    void apply_givenValidStringFieldWithWildcard_shouldReturnLikeCondition() throws Exception {
        String json = """
        {
          "type": "StringQuery",
          "column": "name",
          "operator": "like",
          "value": "John%"
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("String like with wildcard SQL: {}", sql);

        assertAll("String like with wildcard validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("name"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("John%"), "SQL should contain wildcard pattern"),
                () -> assertTrue(sql.toLowerCase().contains("like"), "SQL should contain LIKE operator")
        );
    }

    @Test
    @DisplayName("apply_givenValidStringFieldWithPrefixWildcard_shouldReturnLikeCondition")
    void apply_givenValidStringFieldWithPrefixWildcard_shouldReturnLikeCondition() throws Exception {
        String json = """
        {
          "type": "StringQuery",
          "column": "email",
          "operator": "like",
          "value": "%@gmail.com"
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("String like with prefix wildcard SQL: {}", sql);

        assertAll("String like with prefix wildcard validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("email"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("%@gmail.com"), "SQL should contain prefix wildcard pattern"),
                () -> assertTrue(sql.toLowerCase().contains("like"), "SQL should contain LIKE operator")
        );
    }

    @Test
    @DisplayName("apply_givenValidStringFieldWithBothWildcards_shouldReturnLikeCondition")
    void apply_givenValidStringFieldWithBothWildcards_shouldReturnLikeCondition() throws Exception {
        String json = """
        {
          "type": "StringQuery",
          "column": "description",
          "operator": "like",
          "value": "%product%"
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("String like with both wildcards SQL: {}", sql);

        assertAll("String like with both wildcards validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("description"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("%product%"), "SQL should contain both wildcards pattern"),
                () -> assertTrue(sql.toLowerCase().contains("like"), "SQL should contain LIKE operator")
        );
    }

    @Test
    @DisplayName("apply_givenValidStringFieldWithUnderscoreWildcard_shouldReturnLikeCondition")
    void apply_givenValidStringFieldWithUnderscoreWildcard_shouldReturnLikeCondition() throws Exception {
        String json = """
        {
          "type": "StringQuery",
          "column": "code",
          "operator": "like",
          "value": "A_B"
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("String like with underscore wildcard SQL: {}", sql);

        assertAll("String like with underscore wildcard validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("code"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("A_B"), "SQL should contain underscore wildcard pattern"),
                () -> assertTrue(sql.toLowerCase().contains("like"), "SQL should contain LIKE operator")
        );
    }

    @Test
    @DisplayName("apply_givenValidStringFieldWithoutWildcard_shouldReturnLikeCondition")
    void apply_givenValidStringFieldWithoutWildcard_shouldReturnLikeCondition() throws Exception {
        String json = """
        {
          "type": "StringQuery",
          "column": "status",
          "operator": "like",
          "value": "active"
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("String like without wildcard SQL: {}", sql);

        assertAll("String like without wildcard validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("status"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("active"), "SQL should contain literal value"),
                () -> assertTrue(sql.toLowerCase().contains("like"), "SQL should contain LIKE operator")
        );
    }

    @Test
    @DisplayName("apply_givenValidStringFieldWithSpecialCharacters_shouldReturnLikeCondition")
    void apply_givenValidStringFieldWithSpecialCharacters_shouldReturnLikeCondition() throws Exception {
        String json = """
        {
          "type": "StringQuery",
          "column": "title",
          "operator": "like",
          "value": "%SQL & Database%"
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("String like with special characters SQL: {}", sql);

        assertAll("String like with special characters validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("title"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("SQL & Database"), "SQL should contain special characters"),
                () -> assertTrue(sql.toLowerCase().contains("like"), "SQL should contain LIKE operator")
        );
    }

    @Test
    @DisplayName("apply_givenEmptyStringValue_shouldReturnLikeCondition")
    void apply_givenEmptyStringValue_shouldReturnLikeCondition() throws Exception {
        String json = """
        {
          "type": "StringQuery",
          "column": "notes",
          "operator": "like",
          "value": ""
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("String like with empty value SQL: {}", sql);

        assertAll("String like with empty value validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("notes"), "SQL should contain column name"),
                () -> assertTrue(sql.toLowerCase().contains("like"), "SQL should contain LIKE operator")
        );
    }

    @Test
    @DisplayName("apply_givenUnsupportedIntegerType_shouldThrowException")
    void apply_givenUnsupportedIntegerType_shouldThrowException() {
        String json = """
        {
          "type": "NumericQuery",
          "column": "age",
          "operator": "like",
          "value": 25
        }
        """;

        assertThrows(Exception.class, () -> {
            Query query = mapper.readValue(json, Query.class);
            query.toCondition(dsl, context);
        }, "Should throw exception for like on Integer type");
    }

    @Test
    @DisplayName("apply_givenUnsupportedDateType_shouldThrowException")
    void apply_givenUnsupportedDateType_shouldThrowException() {
        String json = """
        {
          "type": "DateQuery",
          "column": "created_date",
          "operator": "like",
          "value": "2025-01-01"
        }
        """;

        assertThrows(Exception.class, () -> {
            Query query = mapper.readValue(json, Query.class);
            query.toCondition(dsl, context);
        }, "Should throw exception for like on Date type");
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
              "operator": "like",
              "value": "John%"
            },
            {
              "type": "StringQuery",
              "column": "last_name",
              "operator": "like",
              "value": "%Smith"
            },
            {
              "type": "StringQuery",
              "column": "email",
              "operator": "like",
              "value": "%@company.com"
            }
          ]
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Complex nested like SQL: {}", sql);

        assertAll("Complex nested like validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.toLowerCase().contains("or"), "SQL should contain OR operator"),
                () -> assertTrue(sql.contains("first_name"), "SQL should contain first_name column"),
                () -> assertTrue(sql.contains("John%"), "SQL should contain first name pattern"),
                () -> assertTrue(sql.contains("last_name"), "SQL should contain last_name column"),
                () -> assertTrue(sql.contains("%Smith"), "SQL should contain last name pattern"),
                () -> assertTrue(sql.contains("email"), "SQL should contain email column"),
                () -> assertTrue(sql.contains("%@company.com"), "SQL should contain email pattern"),
                () -> assertTrue(sql.toLowerCase().contains("like"), "SQL should contain LIKE operators")
        );
    }

    @Test
    @DisplayName("apply_givenCaseSensitivePattern_shouldReturnLikeCondition")
    void apply_givenCaseSensitivePattern_shouldReturnLikeCondition() throws Exception {
        String json = """
        {
          "type": "StringQuery",
          "column": "product_code",
          "operator": "like",
          "value": "ABC%"
        }
        """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Case sensitive like SQL: {}", sql);

        assertAll("Case sensitive like validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("product_code"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("ABC%"), "SQL should preserve case in pattern"),
                () -> assertTrue(sql.toLowerCase().contains("like"), "SQL should contain LIKE operator")
        );
    }
}
