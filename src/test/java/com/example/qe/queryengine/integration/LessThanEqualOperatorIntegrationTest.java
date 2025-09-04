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

class LessThanEqualOperatorIntegrationTest extends OperatorIntegrationTest {

    /* ============================
       Positive / Normal Cases
       ============================ */
    static Stream<QueryTestCase> positiveTestCases() {
        return Stream.of(
                // Numeric lessThanEqual
                new QueryTestCase("lessThanEqual", "NumericQuery", "amount", "200", "NUMERIC"),
                new QueryTestCase("lessThanEqual", "NumericQuery", "amount", "100.00", "NUMERIC"),

                // Date lessThanEqual
                new QueryTestCase("lessThanEqual", "DateQuery", "createdDate", "2023-08-15", "DATE"),
                new QueryTestCase("lessThanEqual", "DateQuery", "createdDate", "2100-01-01", "DATE")
        );
    }

    @ParameterizedTest
    @MethodSource("positiveTestCases")
    @DisplayName("LessThanEqualOperator Positive Test Cases")
    void parseJsonToCondition_givenLessThanEqualOperatorWithPositiveCases_shouldReturnConditionSuccessfully(
            QueryTestCase testCase) throws Exception {

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
        assertTrue(sql.contains("<="), "SQL should contain <=");

        if ("NUMERIC".equals(testCase.valueType())) {
            assertTrue(sql.contains(testCase.value()), "SQL should contain numeric value");
        } else if ("DATE".equals(testCase.valueType())) {
            assertTrue(sql.contains(testCase.value()), "SQL should contain date value");
        }
    }

    /* ============================
       Negative / Invalid Cases
       ============================ */
    static Stream<QueryTestCase> negativeTestCases() {
        return Stream.of(
                // Invalid operator
                new QueryTestCase("invalid", "NumericQuery", "amount", "100", "NUMERIC"),
                // Missing column
                new QueryTestCase("lessThanEqual", "NumericQuery", "", "100", "NUMERIC"),
                // Invalid numeric
                new QueryTestCase("lessThanEqual", "NumericQuery", "amount", "abc", "NUMERIC"),
                // Invalid date
                new QueryTestCase("lessThanEqual", "DateQuery", "createdDate", "2023-13-01", "DATE"),
                // Null value
                new QueryTestCase("lessThanEqual", "NumericQuery", "amount", null, "NUMERIC")
        );
    }

    @ParameterizedTest
    @MethodSource("negativeTestCases")
    @DisplayName("LessThanEqualOperator Negative Test Cases")
    void parseJsonToCondition_givenLessThanEqualOperatorWithNegativeCases_shouldThrowException(
            QueryTestCase testCase) {

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

        Exception ex = assertThrows(Exception.class, () -> {
            conditionParser.parseJsonToCondition(jsonInput);
        });

        System.out.println("Negative Test Error: " + ex.getMessage());
    }

    /* ============================
       Null Value Case
       ============================ */
    @Test
    @DisplayName("LessThanEqualOperator should throw exception for null value")
    void parseJsonToCondition_givenLessThanEqualOperatorWithNullValue_shouldThrowException() {
        String jsonInput = """
            {
              "type": "NumericQuery",
              "column": "amount",
              "operator": "lessThanEqual",
              "value": null,
              "valueType": "NUMERIC"
            }
            """;

        Exception ex = assertThrows(Exception.class, () -> {
            conditionParser.parseJsonToCondition(jsonInput);
        });

        assertTrue(ex.getMessage().toLowerCase().contains("null"),
                "Exception message should mention null");
    }
}
