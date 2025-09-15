package com.example.qe.queryengine.integration;

import com.example.qe.util.QueryTestCase;
import org.jooq.Condition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class IsNullOperatorIntegrationTest extends OperatorIntegrationTest {

    /* ============================
       Positive / Normal Cases
       ============================ */
    static Stream<QueryTestCase> positiveTestCases() {
        return Stream.of(
                new QueryTestCase("isNull", "StringQuery", "name", null, "STRING"),
                new QueryTestCase("isNull", "NumericQuery", "amount", null, "NUMERIC"),
                new QueryTestCase("isNull", "BoolQuery", "active", null, "BOOLEAN"),
                new QueryTestCase("isNull", "DateQuery", "createdDate", null, "DATE")
        );
    }

    @ParameterizedTest
    @MethodSource("positiveTestCases")
    @DisplayName("IsNullOperator Positive Test Cases")
    void parseJsonToCondition_givenIsNullOperator_shouldReturnIsNullCondition(QueryTestCase testCase){
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

        Condition condition = conditionParser.parseJsonToCondition(jsonInput);
        assertNotNull(condition, "Condition should not be null");

        String sql = condition.toString();
        System.out.println("Generated SQL: " + sql);

        assertTrue(sql.contains(testCase.column()), "SQL should contain column name");
        assertTrue(sql.toLowerCase().contains("is null"), "SQL should contain IS NULL");
    }

    /* ============================
       Edge / Negative Cases
       ============================ */
    static Stream<QueryTestCase> edgeTestCases() {
        return Stream.of(
                new QueryTestCase("isNull", "StringQuery", "", null, "STRING"),  // missing column
                new QueryTestCase(null, "StringQuery", "name", null, "STRING")    // null operator
        );
    }

    @ParameterizedTest
    @MethodSource("edgeTestCases")
    @DisplayName("IsNullOperator Edge Cases")
    void parseJsonToCondition_givenIsNullOperatorEdgeCases_shouldThrowException(QueryTestCase testCase) {
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

        assertThrows(Exception.class, () -> conditionParser.parseJsonToCondition(jsonInput));
    }

    /* ============================
       Null value should be ignored
       ============================ */
    @Test
    @DisplayName("IsNullOperator should ignore value and return IS NULL condition")
    void parseJsonToCondition_givenIsNullOperatorWithAnyValue_shouldReturnIsNullCondition() {
        String jsonInput = """
            {
              "type": "StringQuery",
              "column": "name",
              "operator": "isNull",
              "value": "ignored",
              "valueType": "STRING"
            }
            """;

        Condition condition = conditionParser.parseJsonToCondition(jsonInput);
        assertNotNull(condition);

        String sql = condition.toString();
        System.out.println("Generated SQL: " + sql);

        assertTrue(sql.contains("name"), "SQL should contain column name");
        assertTrue(sql.toLowerCase().contains("is null"), "SQL should contain IS NULL");
    }
}
