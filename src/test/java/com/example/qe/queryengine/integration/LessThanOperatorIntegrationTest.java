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

class LessThanOperatorIntegrationTest extends OperatorIntegrationTest {

    /* ============================
       Positive / Normal Cases
       ============================ */
    static Stream<QueryTestCase> positiveTestCases() {
        return Stream.of(
                // Numeric strictly less
                new QueryTestCase("lessThan", "NumericQuery", "amount", "200", "NUMERIC"),
                new QueryTestCase("lessThan", "NumericQuery", "amount", "100.01", "NUMERIC"),
                // Date strictly less
                new QueryTestCase("lessThan", "DateQuery", "createdDate", "2025-08-15", "DATE"),
                new QueryTestCase("lessThan", "DateQuery", "createdDate", "2100-01-01", "DATE")
        );
    }

    @ParameterizedTest
    @MethodSource("positiveTestCases")
    @DisplayName("LessThanOperator Positive Test Cases")
    void parseJsonToCondition_givenLessThanOperatorWithPositiveCases_shouldReturnConditionSuccessfully(QueryTestCase testCase) {
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
        assertTrue(sql.contains("<"), "SQL should contain less than operator");
        assertFalse(sql.contains("<="), "SQL should not contain less than or equal");
        assertTrue(sql.contains(testCase.value()), "SQL should contain the test value");
    }

    /* ============================
       Negative / Invalid Cases
       ============================ */
    static Stream<QueryTestCase> negativeTestCases() {
        return Stream.of(
                // Invalid operator
                new QueryTestCase("invalid", "NumericQuery", "amount", "100", "NUMERIC"),
                // Missing column
                new QueryTestCase("lessThan", "NumericQuery", "", "100", "NUMERIC"),
                // Invalid numeric value
                new QueryTestCase("lessThan", "NumericQuery", "amount", "abc", "NUMERIC"),
                // Invalid date format
                new QueryTestCase("lessThan", "DateQuery", "createdDate", "2023-13-01", "DATE")
        );
    }

    @ParameterizedTest
    @MethodSource("negativeTestCases")
    @DisplayName("LessThanOperator Negative Test Cases")
    void parseJsonToCondition_givenLessThanOperatorWithNegativeCases_shouldThrowException(QueryTestCase testCase) {
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
    @DisplayName("LessThanOperator should throw InvalidQueryException when value is null")
    void parseJsonToCondition_givenLessThanOperatorWithNullValue_shouldThrowInvalidQueryException() {
        String jsonInput = """
            {
              "type": "NumericQuery",
              "column": "amount",
              "operator": "lessThan",
              "value": null,
              "valueType": "NUMERIC"
            }
            """;

        Exception ex = assertThrows(InvalidQueryException.class, () -> {
            conditionParser.parseJsonToCondition(jsonInput);
        });

        assertTrue(ex.getMessage().contains("Value cannot be null"), "Exception message should mention null value");
    }

    /* ============================
       Edge Cases
       ============================ */
    static Stream<QueryTestCase> edgeTestCases() {
        return Stream.of(
                // Wrong valueType
                new QueryTestCase("lessThan", "NumericQuery", "amount", "100", "STRING"),
                // Null operator
                new QueryTestCase(null, "NumericQuery", "amount", "100", "NUMERIC")
        );
    }

    @ParameterizedTest
    @MethodSource("edgeTestCases")
    @DisplayName("LessThanOperator Edge Cases")
    void parseJsonToCondition_givenLessThanOperatorWithEdgeCases_shouldThrowException(QueryTestCase testCase) {
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

        assertThrows(Exception.class, () -> conditionParser.parseJsonToCondition(jsonInput));
    }
}
