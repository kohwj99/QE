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

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class GreaterThanEqualOperatorIntegrationTest {

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
                // Numeric greater than or equal
                new QueryTestCase("greaterThanEqual", "NumericQuery", "amount", "100", "NUMERIC"),
                new QueryTestCase("greaterThanEqual", "NumericQuery", "amount", "99.99", "NUMERIC"),
                // Date greater than or equal
                new QueryTestCase("greaterThanEqual", "DateQuery", "createdDate", "2023-08-15", "DATE"),
                new QueryTestCase("greaterThanEqual", "DateQuery", "createdDate", "2000-01-01", "DATE")
        );
    }

    @ParameterizedTest
    @MethodSource("positiveTestCases")
    @DisplayName("GreaterThanEqualOperator Positive Test Cases")
    void parseJsonToCondition_givenGreaterThanEqualOperatorWithPositiveCases_shouldReturnConditionSuccessfully(QueryTestCase testCase) throws Exception {
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
        assertTrue(sql.contains(">="), "SQL should contain greater than or equal operator");
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
                new QueryTestCase("greaterThanEqual", "NumericQuery", "", "100", "NUMERIC"),
                // Invalid numeric value
                new QueryTestCase("greaterThanEqual", "NumericQuery", "amount", "abc", "NUMERIC"),
                // Invalid date format
                new QueryTestCase("greaterThanEqual", "DateQuery", "createdDate", "2023-13-01", "DATE")
        );
    }

    @ParameterizedTest
    @MethodSource("negativeTestCases")
    @DisplayName("GreaterThanEqualOperator Negative Test Cases")
    void parseJsonToCondition_givenGreaterThanEqualOperatorWithNegativeCases_shouldThrowException(QueryTestCase testCase) {
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

        assertThrows(Exception.class, () -> queryExecutionService.parseJsonToCondition(jsonInput));
    }

    /* ============================
       Null Value Case
       ============================ */
    @Test
    @DisplayName("GreaterThanEqualOperator should throw InvalidQueryException when value is null")
    void parseJsonToCondition_givenGreaterThanEqualOperatorWithNullValue_shouldThrowInvalidQueryException() {
        String jsonInput = """
            {
              "type": "NumericQuery",
              "column": "amount",
              "operator": "greaterThanEqual",
              "value": null,
              "valueType": "NUMERIC"
            }
            """;

        Exception ex = assertThrows(InvalidQueryException.class, () -> {
            queryExecutionService.parseJsonToCondition(jsonInput);
        });

        assertTrue(ex.getMessage().contains("Value cannot be null"), "Exception message should mention null value");
    }

    /* ============================
       Edge Cases
       ============================ */
    static Stream<QueryTestCase> edgeTestCases() {
        return Stream.of(
                // Wrong valueType
                new QueryTestCase("greaterThanEqual", "NumericQuery", "amount", "100", "STRING"),
                // Null operator
                new QueryTestCase(null, "NumericQuery", "amount", "100", "NUMERIC")
        );
    }

    @ParameterizedTest
    @MethodSource("edgeTestCases")
    @DisplayName("GreaterThanEqualOperator Edge Cases")
    void parseJsonToCondition_givenGreaterThanEqualOperatorWithEdgeCases_shouldThrowException(QueryTestCase testCase) {
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
}
