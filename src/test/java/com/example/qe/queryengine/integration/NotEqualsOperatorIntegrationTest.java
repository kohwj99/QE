package com.example.qe.queryengine.integration;

import com.example.qe.util.QueryTestCase;
import org.jooq.Condition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class NotEqualsOperatorIntegrationTest extends OperatorIntegrationTest {

    /* ============================
       Positive / Normal Cases
       ============================ */
    static Stream<QueryTestCase> positiveTestCases() {
        return Stream.of(
                // String not equals
                new QueryTestCase("notEquals", "StringQuery", "name", "John Doe", "STRING"),
                // Empty value
                new QueryTestCase("notEquals", "StringQuery", "name", "", "STRING"),
                // Boolean not equals
                new QueryTestCase("notEquals", "BoolQuery", "active", "true", "BOOLEAN"),
                new QueryTestCase("notEquals", "BoolQuery", "active", "false", "BOOLEAN"),
                // LocalDate not equals
                new QueryTestCase("notEquals", "DateQuery", "createdDate", "2023-08-15", "DATE"),
                // Numeric not equals
                new QueryTestCase("notEquals", "NumericQuery", "amount", "100.50", "NUMERIC"),
                new QueryTestCase("notEquals", "NumericQuery", "amount", "100", "NUMERIC")
        );
    }

    @ParameterizedTest
    @MethodSource("positiveTestCases")
    @DisplayName("NotEqualsOperator Positive Test Cases")
    void parseJsonToCondition_givenNotEqualsOperatorWithPositiveCases_shouldReturnConditionSuccessfully(QueryTestCase testCase) {
        String jsonInput = String.format("""
                {
                  "type": "%s",
                  "column": "%s",
                  "operator": "%s",
                  "value": "%s",
                  "valueType": "%s"
                }
                """, testCase.queryType(), testCase.column(), testCase.operator(),
                testCase.value(), testCase.valueType());

        Condition condition = conditionParser.parseJsonToCondition(jsonInput);
        assertNotNull(condition, "Condition should not be null");

        String sql = condition.toString();

        assertTrue(sql.contains(testCase.column()), "SQL should contain column name");

        if ("BOOLEAN".equals(testCase.valueType())) {
            assertTrue(sql.contains(testCase.value()), "SQL should contain boolean value");
            assertTrue(sql.contains("<>") || sql.toLowerCase().contains("not"), "SQL should contain NOT EQUALS operator");
        } else if ("NUMERIC".equals(testCase.valueType())) {
            assertTrue(sql.contains(testCase.value()), "SQL should contain numeric value");
            assertTrue(sql.contains("<>"), "SQL should contain NOT EQUALS operator");
        } else if ("DATE".equals(testCase.valueType())) {
            assertTrue(sql.contains(testCase.value()), "SQL should contain date value");
            assertTrue(sql.contains("<>"), "SQL should contain NOT EQUALS operator");
        } else {
            assertTrue(sql.contains(testCase.value()), "SQL should contain string value");
            assertTrue(sql.contains("<>"), "SQL should contain NOT EQUALS operator");
        }
    }

    /* ============================
       Negative / Invalid Cases
       ============================ */
    static Stream<QueryTestCase> negativeTestCases() {
        return Stream.of(
                new QueryTestCase("invalid", "StringQuery", "name", "John", "STRING"),
                new QueryTestCase("notEquals", "StringQuery", "", "John", "STRING"),
                new QueryTestCase("notEquals", "BoolQuery", "active", "notABoolean", "BOOLEAN"),
                new QueryTestCase("notEquals", "NumericQuery", "amount", "abc", "NUMERIC"),
                new QueryTestCase("notEquals", "DateQuery", "createdDate", "2023-13-01", "DATE")
        );
    }

    @ParameterizedTest
    @MethodSource("negativeTestCases")
    @DisplayName("NotEqualsOperator Negative Test Cases")
    void parseJsonToCondition_givenNotEqualsOperatorWithNegativeCases_shouldThrowException(QueryTestCase testCase) {
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
                new QueryTestCase("notEquals", "StringQuery", "\\null\\", "John", "STRING"),
                new QueryTestCase("\\null\\", "StringQuery", "name", "John", "STRING"),
                new QueryTestCase("notEquals", "StringQuery", "name", "John", "NUMERIC")
        );
    }

    @ParameterizedTest
    @MethodSource("edgeTestCases")
    @DisplayName("NotEqualsOperator Edge Cases")
    void parseJsonToCondition_givenNotEqualsOperatorWithEdgeCases_shouldThrowException(QueryTestCase testCase) {
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
    @DisplayName("NotEqualsOperator should handle null value correctly")
    void parseJsonToCondition_givenNotEqualsOperatorWithNullValue_shouldReturnConditionSuccessfully() {
        String stringJsonInput = """
            {
              "type": "StringQuery",
              "column": "name",
              "operator": "notEquals",
              "value": null,
              "valueType": "STRING"
            }
            """;

        Condition condition = conditionParser.parseJsonToCondition(stringJsonInput);
        assertNotNull(condition, "Condition should not be null");

        String sql = condition.toString();
        System.out.println("Generated SQL: " + sql);

        assertTrue(sql.contains("name"), "SQL should contain column name");
        assertTrue(sql.toLowerCase().contains("is not null"), "SQL should contain IS NOT NULL for null value");
    }
}
