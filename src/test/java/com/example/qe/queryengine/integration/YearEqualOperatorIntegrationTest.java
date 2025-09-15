package com.example.qe.queryengine.integration;

import com.example.qe.queryengine.exception.InvalidQueryException;
import com.example.qe.util.QueryTestCase;
import org.jooq.Condition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class YearEqualOperatorIntegrationTest extends OperatorIntegrationTest {


    /* ============================
       Positive / Normal Cases
       ============================ */
    static Stream<QueryTestCase> positiveTestCases() {
        return Stream.of(
                new QueryTestCase("yearEqual", "DateQuery", "createdDate", String.valueOf(LocalDate.now().getYear()), "NUMERIC"),
                new QueryTestCase("yearEqual", "DateQuery", "createdDate", "2000", "NUMERIC"),
                new QueryTestCase("yearEqual", "DateQuery", "createdDate", "1999", "NUMERIC")
        );
    }

    @ParameterizedTest
    @MethodSource("positiveTestCases")
    @DisplayName("YearEqualOperator Positive Test Cases")
    void parseJsonToCondition_givenYearEqualOperatorWithPositiveCases_shouldReturnConditionSuccessfully(QueryTestCase testCase) {
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
        assertTrue(sql.contains("YEAR"), "SQL should extract YEAR");
        assertTrue(sql.contains(testCase.value()), "SQL should contain the correct year value");
    }

    /* ============================
       Negative / Invalid Cases
       ============================ */
    static Stream<QueryTestCase> negativeTestCases() {
        return Stream.of(
                new QueryTestCase("yearEqual", "DateQuery", "createdDate", null, "NUMERIC"),
                new QueryTestCase("yearEqual", "DateQuery", "createdDate", "abc", "STRING"),
                new QueryTestCase("invalid", "DateQuery", "createdDate", "2023", "NUMERIC"),
                new QueryTestCase("yearEqual", "DateQuery", "", "2023", "NUMERIC")
        );
    }

    @ParameterizedTest
    @MethodSource("negativeTestCases")
    @DisplayName("YearEqualOperator Negative Test Cases")
    void parseJsonToCondition_givenYearEqualOperatorWithNegativeCases_shouldThrowException(QueryTestCase testCase) {
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
    @DisplayName("YearEqualOperator should throw InvalidQueryException when value is null")
    void parseJsonToCondition_givenYearEqualOperatorWithNullValue_shouldThrowInvalidQueryException() {
        String jsonInput = """
            {
              "type": "DateQuery",
              "column": "createdDate",
              "operator": "yearEqual",
              "value": null,
              "valueType": "NUMERIC"
            }
            """;

        Exception ex = assertThrows(InvalidQueryException.class, () -> {
            conditionParser.parseJsonToCondition(jsonInput);
        });

        assertTrue(ex.getMessage().contains("Year value cannot be null"));
    }
}
