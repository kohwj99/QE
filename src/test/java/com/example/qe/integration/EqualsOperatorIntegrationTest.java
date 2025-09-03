package com.example.qe.integration;

import com.example.qe.queryengine.QueryExecutionService;
import com.example.qe.queryengine.operator.OperatorFactory;
import com.example.qe.queryengine.operator.OperatorRegistry;
import com.example.qe.queryengine.operator.OperatorScanner;
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

class EqualsOperatorIntegrationTest {

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
    static Stream<TestCase> positiveTestCases() {
        return Stream.of(
                // String equals
                new TestCase("equals", "StringQuery", "name", "John Doe", "STRING"),
                // Empty value
                new TestCase("equals", "StringQuery", "name", "", "STRING"),
                // Boolean equals
                new TestCase("equals", "BoolQuery", "active", "true", "BOOLEAN"),
                new TestCase("equals", "BoolQuery", "active", "false", "BOOLEAN"),
                new TestCase("equals", "BoolQuery", "active", "null", "BOOLEAN"),
                // LocalDate equals
                new TestCase("equals", "DateQuery", "createdDate", "2023-08-15", "DATE"),
                // Numeric equals
                new TestCase("equals", "NumericQuery", "amount", "100.50", "NUMERIC"),
                new TestCase("equals", "NumericQuery", "amount", "100", "NUMERIC")
        );
    }

    @ParameterizedTest
    @MethodSource("positiveTestCases")
    @DisplayName("EqualsOperator Positive Test Cases")
    void parseJsonToCondition_givenEqualsOperatorWithPositiveCases_shouldReturnConditionSuccessfully(TestCase testCase) throws Exception {
        String jsonInput = String.format("""
                {
                  "type": "%s",
                  "column": "%s",
                  "operator": "%s",
                  "value": "%s",
                  "valueType": "%s"
                }
                """, testCase.queryType, testCase.column, testCase.operator,
                testCase.value,
                testCase.valueType);

        Condition condition = queryExecutionService.parseJsonToCondition(jsonInput);
        assertNotNull(condition, "Condition should not be null");

        String sql = condition.toString();

        assertTrue(sql.contains(testCase.column), "SQL should contain column name");

        if ("BOOLEAN".equals(testCase.valueType)) {
            // Boolean values rendered as true/false
            assertTrue(sql.contains(testCase.value), "SQL should contain boolean value");
        } else if ("NUMERIC".equals(testCase.valueType)) {
            assertTrue(sql.contains(testCase.value), "SQL should contain numeric value");
        } else if ("DATE".equals(testCase.valueType)) {
            assertTrue(sql.contains(testCase.value), "SQL should contain date value");
        } else {
            // Default string comparison
            assertTrue(sql.contains(testCase.value), "SQL should contain string value");
        }
    }

    /* ============================
       Negative / Invalid Cases
       ============================ */
    static Stream<TestCase> negativeTestCases() {
        return Stream.of(
                // Invalid operator
                new TestCase("invalid", "StringQuery", "name", "John", "STRING"),
                // Missing column
                new TestCase("equals", "StringQuery", "", "John", "STRING"),
                // Invalid boolean value
                new TestCase("equals", "BoolQuery", "active", "notABoolean", "BOOLEAN"),
                // Invalid numeric value
                new TestCase("equals", "NumericQuery", "amount", "abc", "NUMERIC"),
                // Invalid date
                new TestCase("equals", "DateQuery", "createdDate", "2023-13-01", "DATE")
        );
    }

    @ParameterizedTest
    @MethodSource("negativeTestCases")
    @DisplayName("EqualsOperator Negative Test Cases")
    void parseJsonToCondition_givenEqualsOperatorWithNegativeCases_shouldThrowException(TestCase testCase) {
        String jsonInput = String.format("""
                {
                  "type": "%s",
                  "column": "%s",
                  "operator": "%s",
                  "value": "%s",
                  "valueType": "%s"
                }
                """, testCase.queryType, testCase.column, testCase.operator,
                testCase.value == null ? "null" : testCase.value,
                testCase.valueType);

        Exception ex = assertThrows(Exception.class, () -> {
            queryExecutionService.parseJsonToCondition(jsonInput);
        });

        System.out.println("Negative Test Error: " + ex.getMessage());
    }

    /* ============================
       Edge Cases
       ============================ */
    static Stream<TestCase> edgeTestCases() {
        return Stream.of(
                // Null column name
                new TestCase("equals", "StringQuery", "\\null\\", "John", "STRING"),

                // Null operator
                new TestCase("\\null\\", "StringQuery", "name", "John", "STRING"),

                // Mismatch in Field and Value Type
                new TestCase("equals", "StringQuery", "name", "John", "NUMERIC")
        );
    }

    @ParameterizedTest
    @MethodSource("edgeTestCases")
    @DisplayName("EqualsOperator Edge Cases")
    void parseJsonToCondition_givenEqualsOperatorWithEdgeCases_shouldThrowException(TestCase testCase) {
        String jsonInput = String.format("""
                {
                  "type": "%s",
                  "column": "%s",
                  "operator": "%s",
                  "value": "%s",
                  "valueType": "%s"
                }
                """,
                testCase.queryType,
                testCase.column,
                testCase.operator,
                testCase.value,
                testCase.valueType
        );

        Exception ex = assertThrows(Exception.class, () -> {
            queryExecutionService.parseJsonToCondition(jsonInput);
        });

        System.out.println("Edge Case Error: " + ex.getMessage());
    }

    @Test
    @DisplayName("EqualsOperator should handle null value correctly")
    void parseJsonToCondition_givenEqualsOperatorWithNullValue_shouldReturnConditionSuccessfully() throws Exception {
        String stringJsonInput = """
            {
              "type": "StringQuery",
              "column": "name",
              "operator": "equals",
              "value": null,
              "valueType": "STRING"
            }
            """;

        String boolJsonInput = """
            {
              "type": "BoolQuery",
              "column": "name",
              "operator": "equals",
              "value": null,
              "valueType": "BOOLEAN"
            }
            """;

        String dateJsonInput = """
            {
              "type": "DateQuery",
              "column": "name",
              "operator": "equals",
              "value": null,
              "valueType": "DATE"
            }
            """;

        String numericJsonInput = """
            {
              "type": "NumericQuery",
              "column": "name",
              "operator": "equals",
              "value": null,
              "valueType": "NUMERIC"
            }
            """;

        // test with string json input
        Condition strCondition = queryExecutionService.parseJsonToCondition(stringJsonInput);
        assertNotNull(strCondition, "Condition should not be null");

        String strSql = strCondition.toString();
        System.out.println("Generated SQL: " + strSql);

        assertTrue(strSql.contains("name"), "SQL should contain column name");
        assertTrue(strSql.toLowerCase().contains("is null"), "SQL should contain IS NULL for null value");

        // test with bool json input
        Condition boolCondition = queryExecutionService.parseJsonToCondition(boolJsonInput);
        assertNotNull(boolCondition, "Condition should not be null");

        String boolSql = boolCondition.toString();
        System.out.println("Generated SQL: " + boolSql);

        assertTrue(boolSql.contains("name"), "SQL should contain column name");
        assertTrue(boolSql.toLowerCase().contains("is null"), "SQL should contain IS NULL for null value");

        // test with Date json input
        Condition dateCondition = queryExecutionService.parseJsonToCondition(dateJsonInput);
        assertNotNull(dateCondition, "Condition should not be null");

        String dateSql = dateCondition.toString();
        System.out.println("Generated SQL: " + dateSql);

        assertTrue(dateSql.contains("name"), "SQL should contain column name");
        assertTrue(dateSql.toLowerCase().contains("is null"), "SQL should contain IS NULL for null value");

        // test with Numeric json input
        Condition numericCondition = queryExecutionService.parseJsonToCondition(numericJsonInput);
        assertNotNull(numericCondition, "Condition should not be null");

        String numericSql = numericCondition.toString();
        System.out.println("Generated SQL: " + numericSql);

        assertTrue(numericSql.contains("name"), "SQL should contain column name");
        assertTrue(numericSql.toLowerCase().contains("is null"), "SQL should contain IS NULL for null value");
    }

    /* ============================
       TestCase Class
       ============================ */
    static class TestCase {
        final String operator;
        final String queryType;
        final String column;
        final String value;
        final String valueType;

        TestCase(String operator, String queryType, String column, String value, String valueType) {
            this.operator = operator;
            this.queryType = queryType;
            this.column = column;
            this.value = value;
            this.valueType = valueType;
        }
    }
}
