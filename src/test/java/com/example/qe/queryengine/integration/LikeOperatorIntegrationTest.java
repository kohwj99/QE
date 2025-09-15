package com.example.qe.queryengine.integration;

import com.example.qe.util.QueryTestCase;
import org.jooq.Condition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class LikeOperatorIntegrationTest extends OperatorIntegrationTest {

    /* ============================
       Positive / Normal Cases
       ============================ */
    static Stream<QueryTestCase> positiveTestCases() {
        return Stream.of(
                new QueryTestCase("like", "StringQuery", "name", "John", "STRING"),
                new QueryTestCase("like", "StringQuery", "description", "test", "STRING"),
                new QueryTestCase("like", "StringQuery", "comments", "abc123", "STRING")
        );
    }

    @ParameterizedTest
    @MethodSource("positiveTestCases")
    @DisplayName("LikeOperator Positive Test Cases")
    void parseJsonToCondition_givenLikeOperatorWithPositiveCases_shouldReturnConditionSuccessfully(QueryTestCase testCase) {
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
        assertTrue(sql.contains(testCase.value()), "SQL should contain test value inside LIKE pattern");
        assertTrue(sql.contains("%"), "SQL should wrap the value with wildcards");
    }

    /* ============================
       Negative / Invalid Cases
       ============================ */
    static Stream<QueryTestCase> negativeTestCases() {
        return Stream.of(
                // Invalid operator
                new QueryTestCase("invalid", "StringQuery", "name", "John", "STRING"),
                // Wrong value type
                new QueryTestCase("like", "StringQuery", "name", "100", "NUMERIC"),
                // Missing column
                new QueryTestCase("like", "StringQuery", "", "John", "STRING")
        );
    }

    @ParameterizedTest
    @MethodSource("negativeTestCases")
    @DisplayName("LikeOperator Negative Test Cases")
    void parseJsonToCondition_givenLikeOperatorWithNegativeCases_shouldThrowException(QueryTestCase testCase) {
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
    @DisplayName("LikeOperator should throw IllegalArgumentException when value is null")
    void parseJsonToCondition_givenLikeOperatorWithNullValue_shouldThrowIllegalArgumentException() {
        String jsonInput = """
            {
              "type": "StringQuery",
              "column": "name",
              "operator": "like",
              "value": null,
              "valueType": "STRING"
            }
            """;

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            conditionParser.parseJsonToCondition(jsonInput);
        });

        assertTrue(ex.getMessage().contains("LikeOperator expects a String value"),
                "Exception message should mention that LikeOperator requires String value");
    }

    /* ============================
       Edge Cases
       ============================ */
    static Stream<QueryTestCase> edgeTestCases() {
        return Stream.of(
                // Null operator
                new QueryTestCase(null, "StringQuery", "name", "John", "STRING"),
                // Empty string as value (should still generate LIKE '%%')
                new QueryTestCase("like", "StringQuery", "name", "", "STRING")
        );
    }

    @ParameterizedTest
    @MethodSource("edgeTestCases")
    @DisplayName("LikeOperator Edge Cases")
    void parseJsonToCondition_givenLikeOperatorWithEdgeCases_shouldHandleOrThrowException(QueryTestCase testCase) {
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
            // For empty string, LIKE '%%' is valid
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
