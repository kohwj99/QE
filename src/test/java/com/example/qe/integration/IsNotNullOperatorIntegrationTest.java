package com.example.qe.integration;

import com.example.qe.queryengine.QueryExecutionService;
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

class IsNotNullOperatorIntegrationTest {

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
                new QueryTestCase("isNotNull", "StringQuery", "name", null, "STRING"),
                new QueryTestCase("isNotNull", "NumericQuery", "amount", null, "NUMERIC"),
                new QueryTestCase("isNotNull", "BoolQuery", "active", null, "BOOLEAN"),
                new QueryTestCase("isNotNull", "DateQuery", "createdDate", null, "DATE")
        );
    }

    @ParameterizedTest
    @MethodSource("positiveTestCases")
    @DisplayName("IsNotNullOperator Positive Test Cases")
    void parseJsonToCondition_givenIsNotNullOperator_shouldReturnIsNotNullCondition(QueryTestCase testCase) throws Exception {
        String jsonInput = String.format("""
                {
                  "type": "%s",
                  "column": "%s",
                  "operator": "%s",
                  "value": %s,
                  "valueType": "%s"
                }
                """, testCase.queryType(), testCase.column(), testCase.operator(),
                "null",
                testCase.valueType());

        Condition condition = queryExecutionService.parseJsonToCondition(jsonInput);
        assertNotNull(condition, "Condition should not be null");

        String sql = condition.toString();
        System.out.println("Generated SQL: " + sql);

        assertTrue(sql.contains(testCase.column()), "SQL should contain column name");
        assertTrue(sql.toLowerCase().contains("is not null"), "SQL should contain IS NOT NULL");
    }

    /* ============================
       Edge / Negative Cases
       ============================ */
    static Stream<QueryTestCase> edgeTestCases() {
        return Stream.of(
                new QueryTestCase("isNotNull", "StringQuery", "", null, "STRING"),  // missing column
                new QueryTestCase(null, "StringQuery", "name", null, "STRING")       // null operator
        );
    }

    @ParameterizedTest
    @MethodSource("edgeTestCases")
    @DisplayName("IsNotNullOperator Edge Cases")
    void parseJsonToCondition_givenIsNotNullOperatorEdgeCases_shouldThrowException(QueryTestCase testCase) {
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
                "null",
                testCase.valueType()
        );

        assertThrows(Exception.class, () -> queryExecutionService.parseJsonToCondition(jsonInput));
    }

    /* ============================
       Value is ignored behavior
       ============================ */
    @Test
    @DisplayName("IsNotNullOperator should ignore value and return IS NOT NULL condition")
    void parseJsonToCondition_givenIsNotNullOperatorWithAnyValue_shouldReturnIsNotNullCondition() throws Exception {
        String jsonInput = """
            {
              "type": "StringQuery",
              "column": "name",
              "operator": "isNotNull",
              "value": "ignored",
              "valueType": "STRING"
            }
            """;

        Condition condition = queryExecutionService.parseJsonToCondition(jsonInput);
        assertNotNull(condition);

        String sql = condition.toString();
        System.out.println("Generated SQL: " + sql);

        assertTrue(sql.contains("name"), "SQL should contain column name");
        assertTrue(sql.toLowerCase().contains("is not null"), "SQL should contain IS NOT NULL");
    }
}
