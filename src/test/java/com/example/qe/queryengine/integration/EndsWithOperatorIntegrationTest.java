package com.example.qe.queryengine.integration;

import com.example.qe.queryengine.exception.InvalidQueryException;
import com.example.qe.util.QueryTestCase;
import org.jooq.Condition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class EndsWithOperatorIntegrationTest extends OperatorIntegrationTest {

    /* ============================
       Positive / Normal Cases
       ============================ */
    static Stream<QueryTestCase> positiveTestCases() {
        return Stream.of(
                new QueryTestCase("endsWith", "StringQuery", "name", "Doe", "STRING"),
                new QueryTestCase("endsWith", "StringQuery", "description", "Test", "STRING"),
                new QueryTestCase("endsWith", "StringQuery", "comments", "xyz", "STRING")
        );
    }

    @ParameterizedTest
    @MethodSource("positiveTestCases")
    @DisplayName("EndsWithOperator Positive Test Cases")
    void parseJsonToCondition_givenEndsWithOperatorWithPositiveCases_shouldReturnConditionSuccessfully(QueryTestCase testCase) {
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
        System.out.println("Generated SQL: " + sql);

        assertTrue(sql.contains(testCase.column()), "SQL should contain column name");
        assertTrue(sql.toLowerCase().contains("like"), "SQL should contain LIKE keyword");
        assertTrue(sql.contains(testCase.value()), "SQL should contain the test value");
        assertTrue(sql.contains("%" + testCase.value()), "SQL should prepend '%' for endsWith");
    }

    /* ============================
       Negative / Invalid Cases
       ============================ */
    static Stream<QueryTestCase> negativeTestCases() {
        return Stream.of(
                // Invalid operator
                new QueryTestCase("invalid", "StringQuery", "name", "Doe", "STRING"),
                // Wrong value type
                new QueryTestCase("endsWith", "StringQuery", "name", "123", "NUMERIC"),
                // Missing column
                new QueryTestCase("endsWith", "StringQuery", "", "Doe", "STRING")
        );
    }

    @ParameterizedTest
    @MethodSource("negativeTestCases")
    @DisplayName("EndsWithOperator Negative Test Cases")
    void parseJsonToCondition_givenEndsWithOperatorWithNegativeCases_shouldThrowException(QueryTestCase testCase) {
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

        assertThrows(Exception.class, () -> conditionParser.parseJsonToCondition(jsonInput));
    }

    /* ============================
       Null Value Case
       ============================ */
    @Test
    @DisplayName("EndsWithOperator should throw InvalidQueryException when value is null")
    void parseJsonToCondition_givenEndsWithOperatorWithNullValue_shouldThrowInvalidQueryException() {
        String jsonInput = """
            {
              "type": "StringQuery",
              "column": "name",
              "operator": "endsWith",
              "value": null,
              "valueType": "STRING"
            }
            """;

        Exception ex = assertThrows(InvalidQueryException.class, () -> {
            conditionParser.parseJsonToCondition(jsonInput);
        });

        assertTrue(ex.getMessage().contains("EndsWithOperator expects a String value"),
                "Exception message should mention that EndsWithOperator requires String value");
    }

    /* ============================
       Edge Cases
       ============================ */
    static Stream<QueryTestCase> edgeTestCases() {
        return Stream.of(
                // Null operator
                new QueryTestCase(null, "StringQuery", "name", "Doe", "STRING"),
                // Empty string value
                new QueryTestCase("endsWith", "StringQuery", "name", "", "STRING")
        );
    }

    @ParameterizedTest
    @MethodSource("edgeTestCases")
    @DisplayName("EndsWithOperator Edge Cases")
    void parseJsonToCondition_givenEndsWithOperatorWithEdgeCases_shouldHandleOrThrowException(QueryTestCase testCase) {
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
                testCase.operator() == null ? "\\null\\" : testCase.operator(),
                testCase.value(),
                testCase.valueType()
        );

        if (testCase.value().isEmpty()) {
            // Empty string should generate LIKE '%' successfully
            Condition condition = assertDoesNotThrow(() -> conditionParser.parseJsonToCondition(jsonInput));
            assertNotNull(condition);
            String sql = condition.toString();
            System.out.println("Generated SQL (empty value): " + sql);
            assertTrue(sql.contains("like"), "SQL should still contain LIKE");
        } else {
            assertThrows(Exception.class, () -> conditionParser.parseJsonToCondition(jsonInput));
        }
    }
}
