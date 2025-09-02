package com.example.qe.integration;

import com.example.qe.queryengine.QueryExecutionService;
import com.example.qe.queryengine.operator.OperatorFactory;
import com.example.qe.queryengine.operator.OperatorRegistry;
import com.example.qe.queryengine.operator.OperatorScanner;
//import com.example.qe.queryengine.replacement.ReplacementResolver;
//import com.example.qe.queryengine.replacement.ReplacementService;
//import com.example.qe.queryengine.replacement.impl.BasicPlaceholderResolver;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class QueryExecutionServiceSimpleIntegrationTest {

    private static QueryExecutionService queryExecutionService;

    @BeforeAll
    static void setup() {
        OperatorRegistry operatorRegistry = new OperatorRegistry();
        OperatorScanner operatorScanner = new OperatorScanner(operatorRegistry);
        operatorScanner.scanAndRegister("com.example.qe.queryengine.operator.impl");
        OperatorFactory operatorFactory = new OperatorFactory(operatorRegistry);
//        List<ReplacementResolver> resolvers = List.of(new BasicPlaceholderResolver());
//        ReplacementService replacementService = new ReplacementService(resolvers);
        DSLContext dsl = DSL.using(SQLDialect.DEFAULT);
//        queryExecutionService = new QueryExecutionService(operatorFactory, dsl, replacementService);
        queryExecutionService = new QueryExecutionService(operatorFactory, dsl);
    }

    static Stream<TestCase> operatorTestCases() {
        return Stream.of(
                // StringQuery operators
                new TestCase("equals", "StringQuery", "name", "John Doe", "STRING"),
                new TestCase("notEquals", "StringQuery", "name", "Jane Doe", "STRING"),
                new TestCase("like", "StringQuery", "name", "Doe", "STRING"),
                new TestCase("startsWith", "StringQuery", "name", "John", "STRING"),
                new TestCase("endsWith", "StringQuery", "name", "Doe", "STRING"),
//                new TestCase("isNull", "StringQuery", "name", "Doe", "STRING"),
//                new TestCase("isNotNull", "StringQuery", "name", "Doe", "STRING"),

                // NumericQuery operators
                new TestCase("equals", "NumericQuery", "amount", "100.50", "NUMERIC"),
                new TestCase("notEquals", "NumericQuery", "amount", "100.50", "NUMERIC"),
                new TestCase("greaterThan", "NumericQuery", "amount", "100.50", "NUMERIC"),
                new TestCase("greaterThanEqual", "NumericQuery", "amount", "100.50", "NUMERIC"),
                new TestCase("lessThan", "NumericQuery", "amount", "100.50", "NUMERIC"),
                new TestCase("lessThanEqual", "NumericQuery", "amount", "100.50", "NUMERIC"),
//                new TestCase("isNull", "NumericQuery", "amount", "100.50", "NUMERIC"),
//                new TestCase("isNotNull", "NumericQuery", "amount", "100.50", "NUMERIC"),

                // BoolQuery operators
                new TestCase("equals", "BoolQuery", "active", "true", "BOOLEAN"),
                new TestCase("equals", "BoolQuery", "active", "false", "BOOLEAN"),
                new TestCase("equals", "BoolQuery", "active", null, "BOOLEAN"),
                new TestCase("notEquals", "BoolQuery", "active", "true", "BOOLEAN"),
                new TestCase("notEquals", "BoolQuery", "active", "false", "BOOLEAN"),
                new TestCase("notEquals", "BoolQuery", "active", null, "BOOLEAN"),

                // DateQuery operators
                new TestCase("lessThan", "DateQuery", "createdDate", "2023-12-31", "DATE"),
                new TestCase("greaterThan", "DateQuery", "createdDate", "2023-01-01", "DATE"),
                new TestCase("equals", "DateQuery", "createdDate", "2023-06-15", "DATE"),
                new TestCase("notEquals", "DateQuery", "createdDate", "2023-06-15", "DATE")

//                new TestCase("dayOfWeek", "DateQuery", "createdDate", "1", "NUMERIC"),
//                new TestCase("dayOfMonth", "DateQuery", "createdDate", "15", "NUMERIC"),
//                new TestCase("daysAfter", "DateQuery", "createdDate", "10", "NUMERIC"),
//                new TestCase("daysBefore", "DateQuery", "createdDate", "5", "NUMERIC")
        );
    }

    @ParameterizedTest
    @MethodSource("operatorTestCases")
    @DisplayName("Integration test for all supported operators")
    void testOperatorIntegration(TestCase testCase) throws Exception {
        String jsonInput = String.format("""
        {
          "type": "%s",
          "column": "%s",
          "operator": "%s",
          "value": "%s",
          "valueType": "%s"
        }
        """, testCase.queryType, testCase.column, testCase.operator, testCase.value, testCase.valueType);

        Condition condition = queryExecutionService.parseJsonToCondition(jsonInput);

        String sql = condition.toString();
        assertTrue(sql.contains(testCase.column), "SQL should contain the column name");

        if (testCase.value == null || testCase.value.equalsIgnoreCase("null")) {
            if (testCase.operator.equals("equals")) {
                assertTrue(sql.toLowerCase().contains("is null"), "SQL should contain IS NULL");
            } else if (testCase.operator.equals("notEquals")) {
                assertTrue(sql.toLowerCase().contains("is not null"), "SQL should contain IS NOT NULL");
            }
        } else {
            assertTrue(sql.contains(testCase.value), "SQL should contain the value");
        }

        assertFalse(sql.isEmpty(), "SQL should not be empty");
    }

    @ParameterizedTest
    @MethodSource("specialDateOperatorTestCases")
    @DisplayName("Integration test for special LocalDate operators")
    void testSpecialDateOperators(TestCase testCase) throws Exception {
        String jsonInput = String.format("""
        {
          "type": "%s",
          "column": "%s",
          "operator": "%s",
          "value": "%s",
          "valueType": "%s"
        }
        """, testCase.queryType, testCase.column, testCase.operator, testCase.value, testCase.valueType);

        Condition condition = queryExecutionService.parseJsonToCondition(jsonInput);
        assertNotNull(condition, "Condition should not be null");

        String sql = condition.toString().replaceAll("\\s+", "").toLowerCase();
        System.out.println("Generated SQL: " + sql);

        switch (testCase.operator) {
            case "dayOfWeek":
                // SQL should contain modulo logic like ((datepart(weekday,...)+@@datefirst-2)%7)+1 = value
                assertTrue(sql.contains("%7"), "Expected modulo operation in dayOfWeek SQL");
                assertTrue(sql.contains("=" + testCase.value), "Expected dayOfWeek to compare to " + testCase.value);
                break;
            case "dayOfMonth":
                // SQL should contain day(...) = value
                assertTrue(sql.contains("day(" + testCase.column + ")=" + testCase.value));
                break;
            case "daysAfter":
            case "daysBefore":
                // SQL should contain equality to a computed date (today +/- n days)
                assertTrue(sql.contains(testCase.column + "="), "Expected computed date comparison");
                break;
            default:
                fail("Unknown special operator: " + testCase.operator);
        }
    }

    static Stream<TestCase> specialDateOperatorTestCases() {
        return Stream.of(
                new TestCase("dayOfWeek", "DateQuery", "createdDate", "1", "NUMERIC"),
                new TestCase("dayOfMonth", "DateQuery", "createdDate", "15", "NUMERIC"),
                new TestCase("daysAfter", "DateQuery", "createdDate", "10", "NUMERIC"),
                new TestCase("daysBefore", "DateQuery", "createdDate", "5", "NUMERIC")
        );
    }




    static Stream<TestCase> negativeBoolQueryTestCases() {
        return Stream.of(
                // Invalid operator
                new TestCase("invalidOperator", "BoolQuery", "active", "true", "BOOLEAN"),
                // Missing value
                new TestCase("equals", "BoolQuery", "active", "", "BOOLEAN"),
                // Invalid value type
                new TestCase("equals", "BoolQuery", "active", "notABoolean", "BOOLEAN"),
                // Missing column
                new TestCase("equals", "BoolQuery", "", "true", "BOOLEAN"),
                // Missing operator
                new TestCase("", "BoolQuery", "active", "true", "BOOLEAN")
        );
    }

    @ParameterizedTest
    @MethodSource("negativeBoolQueryTestCases")
    @DisplayName("Negative test cases for BoolQuery")
    void testNegativeBoolQuery(TestCase testCase) {
        String jsonInput = String.format("""
        {
          "type": "%s",
          "column": "%s",
          "operator": "%s",
          "value": "%s",
          "valueType": "%s"
        }
        """, testCase.queryType, testCase.column, testCase.operator, testCase.value, testCase.valueType);

        Exception ex = assertThrows(Exception.class, () -> {
            queryExecutionService.parseJsonToCondition(jsonInput);
        });
        System.out.println("Error thrown: " + ex.getMessage());
    }

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