package com.example.qe.integration;

import com.example.qe.queryengine.QueryExecutionService;
import com.example.qe.queryengine.exception.InvalidQueryException;
import org.jooq.Condition;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ComplexQueryIntegrationTest {

    @Autowired
    private QueryExecutionService queryExecutionService;

    @Test
    @Order(1)
    void parseJsonToCondition_givenAndQuery_shouldGenerateCorrectSql() throws Exception {
        String json = """
        {
          "type": "AndQuery",
          "children": [
            {
              "type": "BoolQuery",
              "column": "is_active",
              "operator": "equals",
              "value": true,
              "valueType": "BOOLEAN"
            },
            {
              "type": "StringQuery",
              "column": "department",
              "operator": "equals",
              "value": "Engineering",
              "valueType": "STRING"
            }
          ]
        }
        """;

        Condition condition = queryExecutionService.parseJsonToCondition(json);
        assertNotNull(condition);

        String sql = condition.toString().toLowerCase();
        assertTrue(sql.contains("is_active = true"));
        assertTrue(sql.contains("department = 'engineering'"));
        assertTrue(sql.contains("and"));
    }

    @Test
    @Order(2)
    void parseJsonToCondition_givenNestedOrQuery_shouldGenerateCorrectSql() throws Exception {
        String json = """
        {
          "type": "OrQuery",
          "children": [
            {
              "type": "NumericQuery",
              "column": "salary",
              "operator": "greaterThan",
              "value": 50000,
              "valueType": "NUMERIC"
            },
            {
              "type": "AndQuery",
              "children": [
                {
                  "type": "BoolQuery",
                  "column": "is_active",
                  "operator": "equals",
                  "value": true,
                  "valueType": "BOOLEAN"
                },
                {
                  "type": "StringQuery",
                  "column": "department",
                  "operator": "equals",
                  "value": "Engineering",
                  "valueType": "STRING"
                }
              ]
            }
          ]
        }
        """;

        Condition condition = queryExecutionService.parseJsonToCondition(json);
        assertNotNull(condition);

        String sql = condition.toString().toLowerCase();
        assertTrue(sql.contains("salary > 50000"));
        assertTrue(sql.contains("is_active = true"));
        assertTrue(sql.contains("department = 'engineering'"));
        assertTrue(sql.contains("or"));
        assertTrue(sql.contains("and"));
    }

    @Test
    @Order(3)
    void parseJsonToCondition_givenMultiLayeredQuery_shouldGenerateCorrectSql() throws Exception {
        String json = """
        {
          "type": "AndQuery",
          "children": [
            {
              "type": "OrQuery",
              "children": [
                {
                  "type": "NumericQuery",
                  "column": "salary",
                  "operator": "greaterThanEqual",
                  "value": 70000,
                  "valueType": "NUMERIC"
                },
                {
                  "type": "NumericQuery",
                  "column": "bonus",
                  "operator": "lessThan",
                  "value": 10000,
                  "valueType": "NUMERIC"
                }
              ]
            },
            {
              "type": "AndQuery",
              "children": [
                {
                  "type": "BoolQuery",
                  "column": "is_active",
                  "operator": "equals",
                  "value": true,
                  "valueType": "BOOLEAN"
                },
                {
                  "type": "DateQuery",
                  "column": "created_date",
                  "operator": "yearEqual",
                  "value": 2023,
                  "valueType": "NUMERIC"
                }
              ]
            }
          ]
        }
        """;

        Condition condition = queryExecutionService.parseJsonToCondition(json);
        assertNotNull(condition);

        String sql = condition.toString().toLowerCase();
        assertTrue(sql.contains("salary >= 70000") || sql.contains("bonus < 10000"));
        assertTrue(sql.contains("is_active = true"));
        assertTrue(sql.contains("year"));
        assertTrue(sql.contains("and") && sql.contains("or"));
    }

    @Test
    @Order(4)
    void parseJsonToCondition_givenEmptyAndQuery_shouldThrowException() {
        String json = """
        {
          "type": "AndQuery",
          "children": []
        }
        """;

        assertThrows(Exception.class, () -> queryExecutionService.parseJsonToCondition(json));
    }

    @Test
    @Order(5)
    void parseJsonToCondition_givenNestedQueryWithInvalidLeaf_shouldThrowException() {
        String json = """
    {
      "type": "AndQuery",
      "children": [
        {
          "type": "OrQuery",
          "children": [
            {
              "type": "NumericQuery",
              "column": "salary",
              "operator": "greaterThan",
              "value": 70000,
              "valueType": "NUMERIC"
            },
            {
              "type": "NumericQuery",
              "column": "",
              "operator": "lessThan",
              "value": 10000,
              "valueType": "NUMERIC"
            }
          ]
        },
        {
          "type": "BoolQuery",
          "column": "is_active",
          "operator": "equals",
          "value": true,
          "valueType": "BOOLEAN"
        }
      ]
    }
    """;

        InvalidQueryException ex = assertThrows(
                InvalidQueryException.class,
                () -> queryExecutionService.parseJsonToCondition(json)
        );

        String expectedMessage = "Column cannot be null or empty"; // adjust to match your operator validation
        assertTrue(ex.getMessage().contains(expectedMessage),
                "Exception message should indicate the invalid column");
    }

}
