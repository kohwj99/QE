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
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class YearsBeforeOperatorIntegrationTest extends OperatorIntegrationTest {

    /* ============================
       Positive / Normal Cases
       ============================ */
    static Stream<QueryTestCase> positiveTestCases() {
        return Stream.of(
                new QueryTestCase("yearsBefore", "DateQuery", "createdDate", "0", "NUMERIC"),
                new QueryTestCase("yearsBefore", "DateQuery", "createdDate", "1", "NUMERIC"),
                new QueryTestCase("yearsBefore", "DateQuery", "createdDate", "5", "NUMERIC")
        );
    }

    @ParameterizedTest
    @MethodSource("positiveTestCases")
    @DisplayName("YearsBeforeOperator Positive Test Cases")
    void parseJsonToCondition_givenYearsBeforeOperatorWithPositiveCases_shouldReturnConditionSuccessfully(QueryTestCase testCase) throws Exception {
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
        assertTrue(sql.contains("CAST"), "SQL should cast field to DATE");
        assertTrue(sql.contains(LocalDate.now().plusYears(Long.parseLong(testCase.value())).toString()),
                "SQL should contain correct target date");
    }

    /* ============================
       Negative / Invalid Cases
       ============================ */
    static Stream<QueryTestCase> negativeTestCases() {
        return Stream.of(
                new QueryTestCase("yearsBefore", "DateQuery", "createdDate", null, "NUMERIC"),
                new QueryTestCase("yearsBefore", "DateQuery", "createdDate", "abc", "STRING"),
                new QueryTestCase("invalid", "DateQuery", "createdDate", "2023", "NUMERIC"),
                new QueryTestCase("yearsBefore", "DateQuery", "", "2023", "NUMERIC")
        );
    }

    @ParameterizedTest
    @MethodSource("negativeTestCases")
    @DisplayName("YearsBeforeOperator Negative Test Cases")
    void parseJsonToCondition_givenYearsBeforeOperatorWithNegativeCases_shouldThrowException(QueryTestCase testCase) {
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
    @DisplayName("YearsBeforeOperator should throw InvalidQueryException when value is null")
    void parseJsonToCondition_givenYearsBeforeOperatorWithNullValue_shouldThrowInvalidQueryException() {
        String jsonInput = """
            {
              "type": "DateQuery",
              "column": "createdDate",
              "operator": "yearsBefore",
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
