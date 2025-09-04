package com.example.qe.queryengine.integration;

import com.example.qe.queryengine.operator.ConditionParser;
import com.example.qe.queryengine.exception.InvalidQueryException;
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

class MonthEqualOperatorIntegrationTest extends OperatorIntegrationTest {

    /* ============================
       Positive / Normal Cases
       ============================ */
    static Stream<QueryTestCase> positiveTestCases() {
        return Stream.of(
                new QueryTestCase("monthEqual", "DateQuery", "createdDate", "1", "NUMERIC"),
                new QueryTestCase("monthEqual", "DateQuery", "createdDate", "6", "NUMERIC"),
                new QueryTestCase("monthEqual", "DateQuery", "createdDate", "12", "NUMERIC")
        );
    }

    @ParameterizedTest
    @MethodSource("positiveTestCases")
    @DisplayName("MonthEqualOperator Positive Test Cases")
    void parseJsonToCondition_givenMonthEqualOperatorWithPositiveCases_shouldReturnConditionSuccessfully(QueryTestCase testCase) throws Exception {
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
        assertTrue(sql.contains("MONTH"), "SQL should use MONTH function");
        assertTrue(sql.contains(testCase.value()), "SQL should contain the expected month value");
    }

    /* ============================
       Negative / Invalid Cases
       ============================ */
    static Stream<QueryTestCase> negativeTestCases() {
        return Stream.of(
                // Null value
                new QueryTestCase("monthEqual", "DateQuery", "createdDate", null, "NUMERIC"),
                // Non-numeric value
                new QueryTestCase("monthEqual", "DateQuery", "createdDate", "abc", "STRING"),
                // Invalid operator
                new QueryTestCase("invalid", "DateQuery", "createdDate", "1", "NUMERIC"),
                // Missing column
                new QueryTestCase("monthEqual", "DateQuery", "", "1", "NUMERIC")
        );
    }

    @ParameterizedTest
    @MethodSource("negativeTestCases")
    @DisplayName("MonthEqualOperator Negative Test Cases")
    void parseJsonToCondition_givenMonthEqualOperatorWithNegativeCases_shouldThrowException(QueryTestCase testCase) {
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

    /* ============================
       Null Value Test
       ============================ */
    @Test
    @DisplayName("MonthEqualOperator should throw InvalidQueryException when value is null")
    void parseJsonToCondition_givenMonthEqualOperatorWithNullValue_shouldThrowInvalidQueryException() {
        String jsonInput = """
            {
              "type": "DateQuery",
              "column": "createdDate",
              "operator": "monthEqual",
              "value": null,
              "valueType": "NUMERIC"
            }
            """;

        Exception ex = assertThrows(InvalidQueryException.class, () -> {
            conditionParser.parseJsonToCondition(jsonInput);
        });

        assertTrue(ex.getMessage().contains("Month value cannot be null"));
    }
}
