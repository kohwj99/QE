package com.example.qe.queryengine.integration;

import com.example.qe.queryengine.operator.ConditionParser;
import com.example.qe.queryengine.operator.OperatorFactory;
import com.example.qe.queryengine.operator.OperatorRegistry;
import com.example.qe.queryengine.operator.OperatorScanner;
import com.example.qe.util.QueryTestCase;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class EqualsOperatorIntegrationTest extends OperatorIntegrationTest {

    /* ============================
       Positive / Normal Cases
       ============================ */
    static Stream<QueryTestCase> positiveTestCases() {
        return Stream.of(
                // String equals
                new QueryTestCase("equals", "StringQuery", "name", "John Doe", "STRING"),
                // Empty value
                new QueryTestCase("equals", "StringQuery", "name", "", "STRING"),
                // Boolean equals
                new QueryTestCase("equals", "BoolQuery", "active", "true", "BOOLEAN"),
                new QueryTestCase("equals", "BoolQuery", "active", "false", "BOOLEAN"),
                new QueryTestCase("equals", "BoolQuery", "active", "null", "BOOLEAN"),
                // LocalDate equals
                new QueryTestCase("equals", "DateQuery", "createdDate", "2023-08-15", "DATE"),
                // Numeric equals
                new QueryTestCase("equals", "NumericQuery", "amount", "100.50", "NUMERIC"),
                new QueryTestCase("equals", "NumericQuery", "amount", "100", "NUMERIC")
        );
    }

    @ParameterizedTest
    @MethodSource("positiveTestCases")
    @DisplayName("EqualsOperator Positive Test Cases")
    void parseJsonToCondition_givenEqualsOperatorWithPositiveCases_shouldReturnConditionSuccessfully(QueryTestCase testCase) throws Exception {
        String jsonInput = String.format("""
                {
                  "type": "%s",
                  "column": "%s",
                  "operator": "%s",
                  "value": "%s",
                  "valueType": "%s"
                }
                """, testCase.queryType(), testCase.column(), testCase.operator(),
                testCase.value(),
                testCase.valueType());

        Condition condition = conditionParser.parseJsonToCondition(jsonInput);
        assertNotNull(condition, "Condition should not be null");

        String sql = condition.toString();

        assertTrue(sql.contains(testCase.column()), "SQL should contain column name");

        if ("BOOLEAN".equals(testCase.valueType())) {
            // Boolean values rendered as true/false
            assertTrue(sql.contains(testCase.value()), "SQL should contain boolean value");
        } else if ("NUMERIC".equals(testCase.valueType())) {
            assertTrue(sql.contains(testCase.value()), "SQL should contain numeric value");
        } else if ("DATE".equals(testCase.valueType())) {
            assertTrue(sql.contains(testCase.value()), "SQL should contain date value");
        } else {
            // Default string comparison
            assertTrue(sql.contains(testCase.value()), "SQL should contain string value");
        }
    }

    /* ============================
       Negative / Invalid Cases
       ============================ */
    static Stream<QueryTestCase> negativeTestCases() {
        return Stream.of(
                // Invalid operator
                new QueryTestCase("invalid", "StringQuery", "name", "John", "STRING"),
                // Missing column
                new QueryTestCase("equals", "StringQuery", "", "John", "STRING"),
                // Invalid boolean value
                new QueryTestCase("equals", "BoolQuery", "active", "notABoolean", "BOOLEAN"),
                // Invalid numeric value
                new QueryTestCase("equals", "NumericQuery", "amount", "abc", "NUMERIC"),
                // Invalid date
                new QueryTestCase("equals", "DateQuery", "createdDate", "2023-13-01", "DATE")
        );
    }

    @ParameterizedTest
    @MethodSource("negativeTestCases")
    @DisplayName("EqualsOperator Negative Test Cases")
    void parseJsonToCondition_givenEqualsOperatorWithNegativeCases_shouldThrowException(QueryTestCase testCase) {
        String jsonInput = String.format("""
                {
                  "type": "%s",
                  "column": "%s",
                  "operator": "%s",
                  "value": "%s",
                  "valueType": "%s"
                }
                """, testCase.queryType(), testCase.column(), testCase.operator(),
                testCase.value() == null ? "null" : testCase.value(),
                testCase.valueType());

        Exception ex = assertThrows(Exception.class, () -> {
            conditionParser.parseJsonToCondition(jsonInput);
        });

        System.out.println("Negative Test Error: " + ex.getMessage());
    }

    /* ============================
       Edge Cases
       ============================ */
    static Stream<QueryTestCase> edgeTestCases() {
        return Stream.of(
                // Null column name
                new QueryTestCase("equals", "StringQuery", "\\null\\", "John", "STRING"),

                // Null operator
                new QueryTestCase("\\null\\", "StringQuery", "name", "John", "STRING"),

                // Mismatch in Field and Value Type
                new QueryTestCase("equals", "StringQuery", "name", "John", "NUMERIC")
        );
    }

    @ParameterizedTest
    @MethodSource("edgeTestCases")
    @DisplayName("EqualsOperator Edge Cases")
    void parseJsonToCondition_givenEqualsOperatorWithEdgeCases_shouldThrowException(QueryTestCase testCase) {
        String jsonInput = String.format("""
                {
                  "type": "%s",
                  "column": "%s",
                  "operator": "%s",
                  "value": "%s",
                  "valueType": "%s"
                }
                """,
                testCase.queryType(),
                testCase.column(),
                testCase.operator(),
                testCase.value(),
                testCase.valueType()
        );

        Exception ex = assertThrows(Exception.class, () -> {
            conditionParser.parseJsonToCondition(jsonInput);
        });

        System.out.println("Edge Case Error: " + ex.getMessage());
    }

    @Test
    @DisplayName("EqualsOperator should handle null value correctly")
    void parseJsonToCondition_givenEqualsOperatorWithNullValue_shouldReturnConditionSuccessfully() throws Exception {
        String stringJsonInput = """
            {
              "type": "StringQuery",
              "column": "name",
              "operator": "equals",
              "value": null,
              "valueType": "STRING"
            }
            """;

        String boolJsonInput = """
            {
              "type": "BoolQuery",
              "column": "name",
              "operator": "equals",
              "value": null,
              "valueType": "BOOLEAN"
            }
            """;

        String dateJsonInput = """
            {
              "type": "DateQuery",
              "column": "name",
              "operator": "equals",
              "value": null,
              "valueType": "DATE"
            }
            """;

        String numericJsonInput = """
            {
              "type": "NumericQuery",
              "column": "name",
              "operator": "equals",
              "value": null,
              "valueType": "NUMERIC"
            }
            """;

        // test with string json input
        Condition strCondition = conditionParser.parseJsonToCondition(stringJsonInput);
        assertNotNull(strCondition, "Condition should not be null");

        String strSql = strCondition.toString();
        System.out.println("Generated SQL: " + strSql);

        assertTrue(strSql.contains("name"), "SQL should contain column name");
        assertTrue(strSql.toLowerCase().contains("is null"), "SQL should contain IS NULL for null value");

        // test with bool json input
        Condition boolCondition = conditionParser.parseJsonToCondition(boolJsonInput);
        assertNotNull(boolCondition, "Condition should not be null");

        String boolSql = boolCondition.toString();
        System.out.println("Generated SQL: " + boolSql);

        assertTrue(boolSql.contains("name"), "SQL should contain column name");
        assertTrue(boolSql.toLowerCase().contains("is null"), "SQL should contain IS NULL for null value");

        // test with Date json input
        Condition dateCondition = conditionParser.parseJsonToCondition(dateJsonInput);
        assertNotNull(dateCondition, "Condition should not be null");

        String dateSql = dateCondition.toString();
        System.out.println("Generated SQL: " + dateSql);

        assertTrue(dateSql.contains("name"), "SQL should contain column name");
        assertTrue(dateSql.toLowerCase().contains("is null"), "SQL should contain IS NULL for null value");

        // test with Numeric json input
        Condition numericCondition = conditionParser.parseJsonToCondition(numericJsonInput);
        assertNotNull(numericCondition, "Condition should not be null");

        String numericSql = numericCondition.toString();
        System.out.println("Generated SQL: " + numericSql);

        assertTrue(numericSql.contains("name"), "SQL should contain column name");
        assertTrue(numericSql.toLowerCase().contains("is null"), "SQL should contain IS NULL for null value");
    }
}
