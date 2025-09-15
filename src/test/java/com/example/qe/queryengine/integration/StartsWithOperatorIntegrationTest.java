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

class StartsWithOperatorIntegrationTest extends OperatorIntegrationTest {

    /* ============================
       Positive / Normal Cases
       ============================ */
    static Stream<QueryTestCase> positiveTestCases() {
        return Stream.of(
                new QueryTestCase("startsWith", "StringQuery", "name", "John", "STRING"),
                new QueryTestCase("startsWith", "StringQuery", "description", "Test", "STRING"),
                new QueryTestCase("startsWith", "StringQuery", "comments", "abc", "STRING")
        );
    }

    @ParameterizedTest
    @MethodSource("positiveTestCases")
    @DisplayName("StartsWithOperator Positive Test Cases")
    void parseJsonToCondition_givenStartsWithOperatorWithPositiveCases_shouldReturnConditionSuccessfully(QueryTestCase testCase) {
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
        assertTrue(sql.contains(testCase.value() + "%"), "SQL should append '%' for startsWith");
    }

    /* ============================
       Negative / Invalid Cases
       ============================ */
    static Stream<QueryTestCase> negativeTestCases() {
        return Stream.of(
                // Invalid operator
                new QueryTestCase("invalid", "StringQuery", "name", "John", "STRING"),
                // Wrong value type
                new QueryTestCase("startsWith", "StringQuery", "name", "123", "NUMERIC"),
                // Missing column
                new QueryTestCase("startsWith", "StringQuery", "", "John", "STRING")
        );
    }

    @ParameterizedTest
    @MethodSource("negativeTestCases")
    @DisplayName("StartsWithOperator Negative Test Cases")
    void parseJsonToCondition_givenStartsWithOperatorWithNegativeCases_shouldThrowException(QueryTestCase testCase) {
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
    @DisplayName("StartsWithOperator should throw InvalidQueryException when value is null")
    void parseJsonToCondition_givenStartsWithOperatorWithNullValue_shouldThrowInvalidQueryException() {
        String jsonInput = """
            {
              "type": "StringQuery",
              "column": "name",
              "operator": "startsWith",
              "value": null,
              "valueType": "STRING"
            }
            """;

        Exception ex = assertThrows(InvalidQueryException.class, () -> {
            conditionParser.parseJsonToCondition(jsonInput);
        });

        assertTrue(ex.getMessage().contains("StartsWithOperator expects a String value"),
                "Exception message should mention that StartsWithOperator requires String value");
    }

    /* ============================
       Edge Cases
       ============================ */
    static Stream<QueryTestCase> edgeTestCases() {
        return Stream.of(
                // Null operator
                new QueryTestCase(null, "StringQuery", "name", "John", "STRING"),
                // Empty string value
                new QueryTestCase("startsWith", "StringQuery", "name", "", "STRING")
        );
    }

    @ParameterizedTest
    @MethodSource("edgeTestCases")
    @DisplayName("StartsWithOperator Edge Cases")
    void parseJsonToCondition_givenStartsWithOperatorWithEdgeCases_shouldHandleOrThrowException(QueryTestCase testCase) {
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
