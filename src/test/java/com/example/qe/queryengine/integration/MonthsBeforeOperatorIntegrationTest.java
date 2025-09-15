package com.example.qe.queryengine.integration;

import com.example.qe.queryengine.exception.InvalidQueryException;
import com.example.qe.util.QueryTestCase;
import org.jooq.Condition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MonthsBeforeOperatorIntegrationTest extends OperatorIntegrationTest {

    /* ============================
       Positive / Normal Cases
       ============================ */
    static Stream<QueryTestCase> positiveTestCases() {
        return Stream.of(
                new QueryTestCase("monthsBefore", "DateQuery", "createdDate", "0", "NUMERIC"),
                new QueryTestCase("monthsBefore", "DateQuery", "createdDate", "1", "NUMERIC"),
                new QueryTestCase("monthsBefore", "DateQuery", "createdDate", "12", "NUMERIC")
        );
    }

    @ParameterizedTest
    @MethodSource("positiveTestCases")
    @DisplayName("MonthsBeforeOperator Positive Test Cases")
    void parseJsonToCondition_givenMonthsBeforeOperatorWithPositiveCases_shouldReturnConditionSuccessfully(QueryTestCase testCase) throws Exception {
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
        assertTrue(sql.contains("CAST"), "SQL should use CAST to DATE");
        assertTrue(sql.contains(LocalDate.now().plusMonths(Integer.parseInt(testCase.value())).toString()), "SQL should contain the correct target date");
    }

    /* ============================
       Negative / Invalid Cases
       ============================ */
    static Stream<QueryTestCase> negativeTestCases() {
        return Stream.of(
                new QueryTestCase("monthsBefore", "DateQuery", "createdDate", null, "NUMERIC"),
                new QueryTestCase("monthsBefore", "DateQuery", "createdDate", "abc", "STRING"),
                new QueryTestCase("invalid", "DateQuery", "createdDate", "1", "NUMERIC"),
                new QueryTestCase("monthsBefore", "DateQuery", "", "1", "NUMERIC")
        );
    }

    @ParameterizedTest
    @MethodSource("negativeTestCases")
    @DisplayName("MonthsBeforeOperator Negative Test Cases")
    void parseJsonToCondition_givenMonthsBeforeOperatorWithNegativeCases_shouldThrowException(QueryTestCase testCase) {
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
    @DisplayName("MonthsBeforeOperator should throw InvalidQueryException when value is null")
    void parseJsonToCondition_givenMonthsBeforeOperatorWithNullValue_shouldThrowInvalidQueryException() {
        String jsonInput = """
            {
              "type": "DateQuery",
              "column": "createdDate",
              "operator": "monthsBefore",
              "value": null,
              "valueType": "NUMERIC"
            }
            """;

        Exception ex = assertThrows(InvalidQueryException.class, () -> {
            conditionParser.parseJsonToCondition(jsonInput);
        });

        assertTrue(ex.getMessage().contains("Day value cannot be null"));
    }
}
