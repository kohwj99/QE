package com.example.qe.queryengine.integration;

import com.example.qe.queryengine.operator.ConditionParser;
import com.example.qe.queryengine.exception.QueryDeserializationException;
import com.example.qe.queryengine.operator.OperatorFactory;
import com.example.qe.queryengine.operator.OperatorRegistry;
import com.example.qe.queryengine.operator.OperatorScanner;
import com.example.qe.util.QueryTestCase;
import com.fasterxml.jackson.databind.JsonMappingException;
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

class DayOfWeekOperatorIntegrationTest extends OperatorIntegrationTest {

    /* ============================
       Positive / Normal Cases
       ============================ */
    static Stream<QueryTestCase> positiveTestCases() {
        return Stream.of(
                new QueryTestCase("dayOfWeek", "DateQuery", "createdDate", "1", "NUMERIC"), // Monday
                new QueryTestCase("dayOfWeek", "DateQuery", "createdDate", "4", "NUMERIC"), // Thursday
                new QueryTestCase("dayOfWeek", "DateQuery", "createdDate", "7", "NUMERIC")  // Sunday
        );
    }

    @ParameterizedTest
    @MethodSource("positiveTestCases")
    @DisplayName("DayOfWeekOperator Positive Test Cases")
    void parseJsonToCondition_givenDayOfWeekOperatorWithPositiveCases_shouldReturnConditionSuccessfully(QueryTestCase testCase) throws Exception {
        String jsonInput = String.format("""
                {
                  "type": "%s",
                  "column": "%s",
                  "operator": "%s",
                  "value": "%s",
                  "valueType": "%s"
                }
                """, testCase.queryType(), testCase.column(), testCase.operator(),
                testCase.value(),
                testCase.valueType());

        Condition condition = conditionParser.parseJsonToCondition(jsonInput);
        assertNotNull(condition, "Condition should not be null");

        String sql = condition.toString();
        System.out.println("Generated SQL: " + sql);

        assertTrue(sql.contains(testCase.column()), "SQL should contain column name");
        assertTrue(sql.contains(testCase.value()), "SQL should contain expected day value");
    }

    /* ============================
       Negative / Invalid Cases
       ============================ */
    static Stream<QueryTestCase> negativeTestCases() {
        return Stream.of(
                // Null value
                new QueryTestCase("dayOfWeek", "DateQuery", "createdDate", null, "DATE"),
                // Missing column
                new QueryTestCase("dayOfWeek", "DateQuery", "", "1", "DATE"),
                // Invalid operator
                new QueryTestCase("invalid", "DateQuery", "createdDate", "3", "DATE"),
                // Invalid value type
                new QueryTestCase("dayOfWeek", "DateQuery", "createdDate", "abc", "STRING")
        );
    }

    @ParameterizedTest
    @MethodSource("negativeTestCases")
    @DisplayName("DayOfWeekOperator Negative Test Cases")
    void parseJsonToCondition_givenDayOfWeekOperatorWithNegativeCases_shouldThrowException(QueryTestCase testCase) {
        String jsonInput = String.format("""
                {
                  "type": "%s",
                  "column": "%s",
                  "operator": "%s",
                  "value": %s,
                  "valueType": "%s"
                }
                """,
                testCase.queryType(),
                testCase.column(),
                testCase.operator() == null ? "\\null\\" : testCase.operator(),
                testCase.value() == null ? "null" : "\"" + testCase.value() + "\"",
                testCase.valueType()
        );

        assertThrows(Exception.class, () -> conditionParser.parseJsonToCondition(jsonInput));
    }

    /* ============================
       Edge Case: value not numeric
       ============================ */
    @Test
    @DisplayName("DayOfWeekOperator should throw InvalidQueryException when value is non-numeric")
    void parseJsonToCondition_givenDayOfWeekOperatorWithNonNumericValue_shouldThrowInvalidQueryException() {
        String jsonInput = """
            {
              "type": "DateQuery",
              "column": "createdDate",
              "operator": "dayOfWeek",
              "value": "notANumber",
              "valueType": "STRING"
            }
            """;

        Exception ex = assertThrows(JsonMappingException.class, () -> {
            conditionParser.parseJsonToCondition(jsonInput);
        });

        assertTrue(ex.getCause() instanceof QueryDeserializationException);
    }
}
