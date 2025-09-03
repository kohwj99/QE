package com.example.qe.integration;

import com.example.qe.queryengine.QueryExecutionService;
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

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class DaysAfterOperatorIntegrationTest {

    private static QueryExecutionService queryExecutionService;

    @BeforeAll
    static void setup() {
        OperatorRegistry registry = new OperatorRegistry();
        OperatorScanner scanner = new OperatorScanner(registry);
        scanner.scanAndRegister("com.example.qe.queryengine.operator.impl");
        OperatorFactory factory = new OperatorFactory(registry);

        DSLContext dsl = DSL.using(SQLDialect.DEFAULT);
        queryExecutionService = new QueryExecutionService(factory, dsl);
    }

    /* ============================
       Positive / Normal Cases
       ============================ */
    static Stream<QueryTestCase> positiveTestCases() {
        return Stream.of(
                new QueryTestCase("daysAfter", "DateQuery", "createdDate", "0", "NUMERIC"),
                new QueryTestCase("daysAfter", "DateQuery", "createdDate", "1", "NUMERIC"),
                new QueryTestCase("daysAfter", "DateQuery", "createdDate", "30", "NUMERIC")
        );
    }

    @ParameterizedTest
    @MethodSource("positiveTestCases")
    @DisplayName("DaysAfterOperator Positive Test Cases")
    void parseJsonToCondition_givenDaysAfterOperatorWithPositiveCases_shouldReturnConditionSuccessfully(QueryTestCase testCase) throws Exception {
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

        Condition condition = queryExecutionService.parseJsonToCondition(jsonInput);
        assertNotNull(condition, "Condition should not be null");

        String sql = condition.toString();
        System.out.println("Generated SQL: " + sql);

        assertTrue(sql.contains(testCase.column()), "SQL should contain column name");

        LocalDate expectedDate = LocalDate.now().minusDays(Long.parseLong(testCase.value()));
        assertTrue(sql.contains(expectedDate.toString()), "SQL should contain the computed target date");
    }

    /* ============================
       Negative / Invalid Cases
       ============================ */
    static Stream<QueryTestCase> negativeTestCases() {
        return Stream.of(
                // Null value
                new QueryTestCase("daysAfter", "DateQuery", "createdDate", null, "NUMERIC"),
                // Non-numeric value
                new QueryTestCase("daysAfter", "DateQuery", "createdDate", "abc", "STRING"),
                // Invalid operator
                new QueryTestCase("invalid", "DateQuery", "createdDate", "1", "NUMERIC"),
                // Missing column
                new QueryTestCase("daysAfter", "DateQuery", "", "1", "NUMERIC")
        );
    }

    @ParameterizedTest
    @MethodSource("negativeTestCases")
    @DisplayName("DaysAfterOperator Negative Test Cases")
    void parseJsonToCondition_givenDaysAfterOperatorWithNegativeCases_shouldThrowException(QueryTestCase testCase) {
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

        assertThrows(Exception.class, () -> queryExecutionService.parseJsonToCondition(jsonInput));
    }

    /* ============================
       Null Value Test
       ============================ */
    @Test
    @DisplayName("DaysAfterOperator should throw InvalidQueryException when value is null")
    void parseJsonToCondition_givenDaysAfterOperatorWithNullValue_shouldThrowInvalidQueryException() {
        String jsonInput = """
            {
              "type": "DateQuery",
              "column": "createdDate",
              "operator": "daysAfter",
              "value": null,
              "valueType": "NUMERIC"
            }
            """;

        Exception ex = assertThrows(InvalidQueryException.class, () -> {
            queryExecutionService.parseJsonToCondition(jsonInput);
        });

        assertTrue(ex.getMessage().contains("Day value cannot be null"));
    }
}
