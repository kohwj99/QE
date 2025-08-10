package com.example.qe.util;

import com.example.qe.model.query.Query;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.support.DefaultConversionService;

import static org.junit.jupiter.api.Assertions.*;

public class QueryDeserializationTest {

    private static final Logger logger = LoggerFactory.getLogger(QueryDeserializationTest.class);

    private ObjectMapper mapper;
    private DSLContext dsl;
    private QueryExecutionContext context;

    @BeforeEach
    public void setup() {
        // Jackson ObjectMapper (register polymorphic modules for Query if needed)
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        // TODO: register polymorphic config or modules if you use @JsonTypeInfo on Query hierarchy

        // Use H2 dialect for SQL generation
        dsl = DSL.using(SQLDialect.H2);

        // Setup OperatorRegistry and scan operators
        OperatorRegistry registry = new OperatorRegistry();
        OperatorScanner scanner = new OperatorScanner(registry);
        scanner.scanAndRegister("com.example.qe.model.operator");

        // Create factory and context with Spring ConversionService (or default)
        OperatorFactory factory = new OperatorFactory(registry);
        context = new QueryExecutionContext(factory, new DefaultConversionService());

        logger.info("Setup complete: OperatorRegistry size = {}, Total operators = {}",
                registry.getAllOperatorNames().size(),
                registry.getTotalOperatorCount());
    }

    @Test
    public void testDeserializeJsonToCondition() throws Exception {
        String json = """
        {
          "tableName": "sales_data",
          "conditions": {
            "type": "AndQuery",
            "children": [
              {
                "type": "IntQuery",
                "column": "revenue",
                "operator": "greaterThan",
                "value": 10000
              },
              {
                "type": "StringQuery",
                "column": "region",
                "operator": "equals",
                "value": "APAC"
              },
              {
                "type": "OrQuery",
                "children": [
                  {
                    "type": "DateQuery",
                    "column": "order_date",
                    "operator": "lessThan",
                    "value": "2025-01-01"
                  },
                  {
                    "type": "BoolQuery",
                    "operator": "equals",
                    "column": "sold",
                    "value": true
                  }
                ]
              }
            ]
          }
        }
        """;

        JsonNode rootNode = mapper.readTree(json);
        JsonNode conditionsNode = rootNode.get("conditions");

        // Deserialize the conditions subtree to Query
        Query query = mapper.treeToValue(conditionsNode, Query.class);

        // Generate the jOOQ Condition recursively
        Condition condition = query.toCondition(dsl, context);

        logger.info("Generated jOOQ Condition object: {}", condition);
        logger.info("Rendered SQL Condition: {}", dsl.renderInlined(condition));
    }

    @Test
    public void testDeserializeJsonToCondition2() throws Exception {
        String json = """
    {
      "tableName": "sales_data",
      "conditions": {
        "type": "AndQuery",
        "children": [
          {
            "type": "IntQuery",
            "column": "revenue",
            "operator": "greaterThan",
            "value": 10000
          },
          {
            "type": "StringQuery",
            "column": "region",
            "operator": "equals",
            "value": "APAC"
          },
          {
            "type": "OrQuery",
            "children": [
              {
                "type": "DateQuery",
                "column": "order_date",
                "operator": "lessThan",
                "value": "2025-01-01"
              },
              {
                "type": "BoolQuery",
                "operator": "equals",
                "column": "sold",
                "value": true
              }
            ]
          }
        ]
      }
    }
    """;

        JsonNode rootNode = mapper.readTree(json);
        JsonNode conditionsNode = rootNode.get("conditions");

        // Deserialize the conditions subtree to Query
        Query query = mapper.treeToValue(conditionsNode, Query.class);
        logger.info("Deserialized Query type: {}", query.getClass().getSimpleName());

        // Generate the jOOQ Condition recursively
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        // Comprehensive logging
        logger.info("Generated jOOQ Condition object: {}", condition);
        logger.info("Rendered SQL Condition: {}", sql);
        logger.info("SQL Length: {} characters", sql.length());
        logger.info("SQL Structure Analysis:");
        logger.info("  - Contains AND: {}", sql.toLowerCase().contains("and"));
        logger.info("  - Contains OR: {}", sql.toLowerCase().contains("or"));
        logger.info("  - Parentheses count - Open: {}, Close: {}",
                sql.chars().filter(ch -> ch == '(').count(),
                sql.chars().filter(ch -> ch == ')').count());

        // Comprehensive assertions to prove correctness
        assertAll("Complex query structure validation",
                // Basic validation
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertNotNull(sql, "SQL should not be null"),
                () -> assertFalse(sql.trim().isEmpty(), "SQL should not be empty"),

                // Logical structure validation
                () -> assertTrue(sql.toLowerCase().contains("and"),
                        "SQL should contain AND for top-level conjunction"),
                () -> assertTrue(sql.toLowerCase().contains("or"),
                        "SQL should contain OR for nested disjunction"),

                // Column presence validation
                () -> assertTrue(sql.contains("revenue"), "SQL should contain 'revenue' column"),
                () -> assertTrue(sql.contains("region"), "SQL should contain 'region' column"),
                () -> assertTrue(sql.contains("order_date"), "SQL should contain 'order_date' column"),
                () -> assertTrue(sql.contains("sold"), "SQL should contain 'sold' column"),

                // Value validation
                () -> assertTrue(sql.contains("10000"), "SQL should contain revenue value '10000'"),
                () -> assertTrue(sql.contains("'APAC'"), "SQL should contain quoted region value 'APAC'"),
                () -> assertTrue(sql.contains("2025-01-01"), "SQL should contain date '2025-01-01'"),
                () -> assertTrue(sql.toLowerCase().contains("true") || sql.contains("1"),
                        "SQL should contain boolean true value"),

                // Operator validation
                () -> assertTrue(sql.contains(">") || sql.toLowerCase().contains("greater"),
                        "SQL should contain greater than operator"),
                () -> assertTrue(sql.contains("="), "SQL should contain equals operator"),
                () -> assertTrue(sql.contains("<") || sql.toLowerCase().contains("less"),
                        "SQL should contain less than operator"),

                // Structure integrity validation
                () -> {
                    long openParens = sql.chars().filter(ch -> ch == '(').count();
                    long closeParens = sql.chars().filter(ch -> ch == ')').count();
                    assertEquals(openParens, closeParens, "Parentheses should be balanced");
                },
                () -> assertTrue(sql.chars().filter(ch -> ch == '(').count() >= 2,
                        "Should have proper nesting with at least 2 levels"),

                // SQL syntax validation
                () -> assertFalse(sql.contains("null"), "SQL should not contain null values"),
                () -> assertFalse(sql.matches(".*\\s{2,}.*"), "SQL should not have excessive whitespace"),
                () -> assertTrue(sql.matches(".*[a-zA-Z].*"), "SQL should contain alphabetic characters")
        );

        // Additional validation - verify the condition can be used in a query
        String selectSql = dsl.select().from("sales_data").where(condition).getSQL();
        logger.info("Complete SELECT query: {}", selectSql);

        assertAll("Complete query validation",
                () -> assertTrue(selectSql.toLowerCase().contains("select"),
                        "Should generate valid SELECT statement"),
                () -> assertTrue(selectSql.toLowerCase().contains("from sales_data"),
                        "Should reference correct table"),
                () -> assertTrue(selectSql.toLowerCase().contains("where"),
                        "Should include WHERE clause")
        );

        // Performance validation
        long startTime = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            query.toCondition(dsl, context);
        }
        long endTime = System.nanoTime();
        long avgTimeNs = (endTime - startTime) / 1000;
        logger.info("Performance: Average condition generation time: {} ns", avgTimeNs);

        assertTrue(avgTimeNs < 1_000_000, "Condition generation should be efficient (< 1ms avg)");
    }

    @Test
    public void testSimpleIntQuery() throws Exception {
        String json = """
    {
      "type": "IntQuery",
      "column": "age",
      "operator": "equals",
      "value": 25
    }
    """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Simple IntQuery SQL: {}", sql);

        // Assert the SQL contains expected elements
        assertTrue(sql.contains("age"), "SQL should contain column name 'age'");
        assertTrue(sql.contains("25"), "SQL should contain value '25'");
        assertTrue(sql.contains("=") || sql.toLowerCase().contains("equals"),
                "SQL should contain equals operator");
    }

    @Test
    public void testSimpleStringQuery() throws Exception {
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

        logger.info("Simple StringQuery SQL: {}", sql);

        assertAll("String query validation",
                () -> assertTrue(sql.contains("name"), "SQL should contain column 'name'"),
                () -> assertTrue(sql.toLowerCase().contains("like"), "SQL should contain LIKE operator"),
                () -> assertTrue(sql.contains("John%"), "SQL should contain pattern 'John%'")
        );
    }

    @Test
    public void testNestedAndOrQuery() throws Exception {
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
          "type": "OrQuery",
          "children": [
            {
              "type": "IntQuery",
              "column": "price",
              "operator": "lessThan",
              "value": 100
            },
            {
              "type": "BoolQuery",
              "column": "on_sale",
              "operator": "equals",
              "value": true
            }
          ]
        }
      ]
    }
    """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Nested AndOr SQL: {}", sql);

        assertAll("Nested AND/OR query validation",
                () -> assertTrue(sql.toLowerCase().contains("and"), "SQL should contain AND operator"),
                () -> assertTrue(sql.toLowerCase().contains("or"), "SQL should contain OR operator"),
                () -> assertTrue(sql.contains("category"), "SQL should contain 'category' column"),
                () -> assertTrue(sql.contains("Electronics"), "SQL should contain 'Electronics' value"),
                () -> assertTrue(sql.contains("price"), "SQL should contain 'price' column"),
                () -> assertTrue(sql.contains("100"), "SQL should contain price value '100'"),
                () -> assertTrue(sql.contains("on_sale"), "SQL should contain 'on_sale' column"),
                () -> assertTrue(sql.toLowerCase().contains("true") || sql.contains("1"),
                        "SQL should contain boolean true value")
        );
    }

    @Test
    public void testComplexDateQuery() throws Exception {
        String json = """
    {
      "type": "OrQuery",
      "children": [
        {
          "type": "DateQuery",
          "column": "created_at",
          "operator": "greaterThan",
          "value": "2024-01-01"
        },
        {
          "type": "AndQuery",
          "children": [
            {
              "type": "DateQuery",
              "column": "updated_at",
              "operator": "lessThan",
              "value": "2024-12-31"
            },
            {
              "type": "BoolQuery",
              "column": "active",
              "operator": "equals",
              "value": true
            }
          ]
        }
      ]
    }
    """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Complex Date Query SQL: {}", sql);

        assertAll("Complex date query validation",
                () -> assertTrue(sql.toLowerCase().contains("or"), "SQL should contain OR operator"),
                () -> assertTrue(sql.toLowerCase().contains("and"), "SQL should contain AND operator"),
                () -> assertTrue(sql.contains("created_at"), "SQL should contain 'created_at' column"),
                () -> assertTrue(sql.contains("updated_at"), "SQL should contain 'updated_at' column"),
                () -> assertTrue(sql.contains("2024-01-01"), "SQL should contain start date"),
                () -> assertTrue(sql.contains("2024-12-31"), "SQL should contain end date"),
                () -> assertTrue(sql.contains("active"), "SQL should contain 'active' column")
        );
    }

    @Test
    public void testDeepNesting() throws Exception {
        String json = """
    {
      "type": "AndQuery",
      "children": [
        {
          "type": "StringQuery",
          "column": "department",
          "operator": "equals",
          "value": "Sales"
        },
        {
          "type": "OrQuery",
          "children": [
            {
              "type": "AndQuery",
              "children": [
                {
                  "type": "IntQuery",
                  "column": "experience",
                  "operator": "greaterThan",
                  "value": 2
                },
                {
                  "type": "BoolQuery",
                  "column": "full_time",
                  "operator": "equals",
                  "value": true
                }
              ]
            },
            {
              "type": "AndQuery",
              "children": [
                {
                  "type": "IntQuery",
                  "column": "salary",
                  "operator": "greaterThan",
                  "value": 50000
                },
                {
                  "type": "BoolQuery",
                  "column": "remote",
                  "operator": "equals",
                  "value": true
                }
              ]
            }
          ]
        }
      ]
    }
    """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Deep Nesting SQL: {}", sql);

        // Validate proper nesting with parentheses
        long openParens = sql.chars().filter(ch -> ch == '(').count();
        long closeParens = sql.chars().filter(ch -> ch == ')').count();

        assertAll("Deep nesting validation",
                () -> assertEquals(openParens, closeParens, "Parentheses should be balanced"),
                () -> assertTrue(openParens >= 2, "Should have at least 2 levels of nesting"),
                () -> assertTrue(sql.contains("department"), "SQL should contain 'department' column"),
                () -> assertTrue(sql.contains("Sales"), "SQL should contain 'Sales' value"),
                () -> assertTrue(sql.contains("experience"), "SQL should contain 'experience' column"),
                () -> assertTrue(sql.contains("salary"), "SQL should contain 'salary' column"),
                () -> assertTrue(sql.contains("50000"), "SQL should contain salary value")
        );
    }

    @Test
    public void testSingleChildAndQuery() throws Exception {
        String json = """
    {
      "type": "AndQuery",
      "children": [
        {
          "type": "StringQuery",
          "column": "status",
          "operator": "equals",
          "value": "active"
        }
      ]
    }
    """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Single Child AndQuery SQL: {}", sql);

        assertAll("Single child AND query validation",
                () -> assertTrue(sql.contains("status"), "SQL should contain 'status' column"),
                () -> assertTrue(sql.contains("active"), "SQL should contain 'active' value"),
                () -> assertFalse(sql.toLowerCase().contains(" and "),
                        "Single child AND should not contain explicit AND operator")
        );
    }

    @Test
    public void testAllBooleanOperators() throws Exception {
        String json = """
    {
      "type": "OrQuery",
      "children": [
        {
          "type": "BoolQuery",
          "column": "is_verified",
          "operator": "equals",
          "value": true
        },
        {
          "type": "BoolQuery",
          "column": "is_premium",
          "operator": "notEquals",
          "value": false
        }
      ]
    }
    """;

        Query query = mapper.readValue(json, Query.class);
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(condition);

        logger.info("Boolean Operators SQL: {}", sql);

        assertAll("Boolean operators validation",
                () -> assertTrue(sql.toLowerCase().contains("or"), "SQL should contain OR operator"),
                () -> assertTrue(sql.contains("is_verified"), "SQL should contain 'is_verified' column"),
                () -> assertTrue(sql.contains("is_premium"), "SQL should contain 'is_premium' column"),
                () -> assertTrue(sql.toLowerCase().contains("true") || sql.contains("1"),
                        "SQL should contain true value"),
                () -> assertTrue(sql.toLowerCase().contains("false") || sql.contains("0") ||
                                sql.contains("<>") || sql.contains("!="),
                        "SQL should contain false value or not equals operator")
        );
    }

    @Test
    public void testEmptyAndQuery() {
        String json = """
    {
      "type": "AndQuery",
      "children": []
    }
    """;

        Query query = assertDoesNotThrow(() -> mapper.readValue(json, Query.class));

        Exception exception = assertThrows(Exception.class, () -> {
            query.toCondition(dsl, context);
        }, "Should throw exception for empty AndQuery");

        assertNotNull(exception.getMessage(), "Exception should have a descriptive message");
    }

    @Test
    public void testInvalidOperatorHandling() {
        String json = """
    {
      "type": "IntQuery",
      "column": "age",
      "operator": "invalidOperator",
      "value": 25
    }
    """;

        Query query = assertDoesNotThrow(() -> mapper.readValue(json, Query.class));

        Exception exception = assertThrows(Exception.class, () -> {
            query.toCondition(dsl, context);
        }, "Should throw exception for invalid operator");

        assertTrue(exception.getMessage().toLowerCase().contains("operator") ||
                        exception.getMessage().toLowerCase().contains("invalid"),
                "Exception message should mention invalid operator");
    }
}
