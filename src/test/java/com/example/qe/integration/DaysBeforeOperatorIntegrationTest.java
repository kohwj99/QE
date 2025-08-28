package com.example.qe.integration;

import com.example.qe.queryengine.QueryExecutionService;
import com.example.qe.queryengine.operator.CustomOperator;
import com.example.qe.queryengine.operator.OperatorRegistry;
import com.example.qe.queryengine.operator.OperatorScanner;
import com.example.qe.queryengine.operator.GenericOperator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DaysBeforeOperatorIntegrationTest {

    private static final Logger logger = LoggerFactory.getLogger(DaysBeforeOperatorIntegrationTest.class);

    @Autowired
    private OperatorRegistry registry;

    @Autowired
    private QueryExecutionService queryExecutionService;

    @Test
    void testDaysBeforeOperatorIntegration() {
        String fieldName = "createdDate";
        String operatorName = "daysBefore";
        BigDecimal value = BigDecimal.valueOf(5);

        new OperatorScanner(registry).scanAndRegister("com.example.qe.queryengine.operator.impl");

        GenericOperator<BigDecimal> operator = (GenericOperator<BigDecimal>) registry.get(operatorName, BigDecimal.class);
        assertNotNull(operator);

        Field<LocalDate> field = DSL.field(DSL.name(fieldName), LocalDate.class);

        Condition condition = ((CustomOperator<BigDecimal>) operator).applyToField(field, value);
        assertNotNull(condition);
        String sql = condition.toString();
        System.out.println(sql);
        LocalDate expectedDate = LocalDate.now().minusDays(5);
        String expectedDateSql = "date '" + expectedDate + "'";

        assertTrue(sql.contains(fieldName));
        assertTrue(sql.contains(expectedDateSql));

        System.out.println("Integration test - Generated JOOQ condition: " + sql);
    }

    @Test
    @Order(9)
    @DisplayName("parseJsonToCondition_givenDaysBeforeOperator_shouldGenerateCorrectCondition")
    void parseJsonToCondition_givenDaysBeforeOperator_shouldGenerateCorrectCondition() throws Exception {
        logger.info("=== Test Case 9: DaysBefore Operator ===");

        int daysBefore = 5;
        String jsonInput = String.format("""
        {
          "type": "DateQuery",
          "column": "createdDate",
          "operator": "daysBefore",
          "value": %d
        }
        """, daysBefore);

        logger.info("Input JSON: {}", jsonInput);

        Condition condition = queryExecutionService.parseJsonToCondition(jsonInput);

        assertNotNull(condition, "Generated condition should not be null");
        String generatedSQL = condition.toString();
        logger.info("Generated JOOQ Condition: {}", generatedSQL);

        LocalDate expectedDate = LocalDate.now().minusDays(daysBefore);
        String expectedDateSql = "date '" + expectedDate + "'";

        assertTrue(generatedSQL.contains("createdDate"), "SQL should contain field name");
        assertTrue(generatedSQL.contains(expectedDateSql), "SQL should contain the correct date value");
        assertTrue(generatedSQL.contains("="), "SQL should contain equality operator");

        logger.info("✓ DaysBefore Operator test completed successfully");
    }
}