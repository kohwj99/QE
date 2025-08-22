package com.example.qe.model.operator.impl.old;

import com.example.qe.model.operator.impl.*;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Date Operator Tests - All 9 Date Operators")
class DateOperatorTest {

    private static final Logger logger = LoggerFactory.getLogger(DateOperatorTest.class);

    // Before operators
    private DaysBeforeOperator daysBeforeOperator;
    private MonthsBeforeOperator monthsBeforeOperator;
    private YearsBeforeOperator yearsBeforeOperator;

    // Equal operators
    private DayEqualOperator dayEqualOperator;
    private MonthEqualOperator monthEqualOperator;
    private YearEqualOperator yearEqualOperator;

    // After operators
    private DaysAfterOperator daysAfterOperator;
    private MonthsAfterOperator monthsAfterOperator;
    private YearsAfterOperator yearsAfterOperator;

    private Field<LocalDate> dateField;

    @BeforeEach
    void setUp() {
        logger.info("=== Setting up Date Operator tests ===");

        // Initialize Before operators
        daysBeforeOperator = new DaysBeforeOperator();
        monthsBeforeOperator = new MonthsBeforeOperator();
        yearsBeforeOperator = new YearsBeforeOperator();

        // Initialize Equal operators
        dayEqualOperator = new DayEqualOperator();
        monthEqualOperator = new MonthEqualOperator();
        yearEqualOperator = new YearEqualOperator();

        // Initialize After operators
        daysAfterOperator = new DaysAfterOperator();
        monthsAfterOperator = new MonthsAfterOperator();
        yearsAfterOperator = new YearsAfterOperator();

        // Create test field
        dateField = DSL.field("birth_date", LocalDate.class);
        logger.info("Created all 9 date operators and test field");
    }

    @Test
    @DisplayName("DaysBeforeOperator should create correct condition")
    void testDaysBeforeOperator() {
        logger.info("=== Testing DaysBeforeOperator ===");

        Integer daysValue = 7;
        var condition = daysBeforeOperator.applyToField(dateField, BigDecimal.valueOf(daysValue));

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        logger.info("Generated SQL for daysBefore({}) : {}", daysValue, sql);

        LocalDate expectedDate = LocalDate.now().minusDays(7);
        assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equality operator");
        assertTrue(sql.contains(expectedDate.toString()), "SQL should contain the calculated date");

        logger.info("✓ DaysBeforeOperator correctly generates condition");
    }

    @Test
    @DisplayName("MonthsBeforeOperator should create correct condition")
    void testMonthsBeforeOperator() {
        logger.info("=== Testing MonthsBeforeOperator ===");

        Integer monthsValue = 3;
        var condition = monthsBeforeOperator.applyToField(dateField, BigDecimal.valueOf(monthsValue));

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        logger.info("Generated SQL for monthsBefore({}) : {}", monthsValue, sql);

        LocalDate expectedDate = LocalDate.now().minusMonths(3);
        assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equality operator");
        assertTrue(sql.contains(expectedDate.toString()), "SQL should contain the calculated date");

        logger.info("✓ MonthsBeforeOperator correctly generates condition");
    }

    @Test
    @DisplayName("YearsBeforeOperator should create correct condition")
    void testYearsBeforeOperator() {
        logger.info("=== Testing YearsBeforeOperator ===");

        Integer yearsValue = 2;
        var condition = yearsBeforeOperator.applyToField(dateField, BigDecimal.valueOf(yearsValue));

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        logger.info("Generated SQL for yearsBefore({}) : {}", yearsValue, sql);

        LocalDate expectedDate = LocalDate.now().minusYears(2);
        assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equality operator");
        assertTrue(sql.contains(expectedDate.toString()), "SQL should contain the calculated date");

        logger.info("✓ YearsBeforeOperator correctly generates condition");
    }

    @Test
    @DisplayName("DayEqualOperator should create correct condition")
    void testDayEqualOperator() {
        logger.info("=== Testing DayEqualOperator ===");

        Integer dayValue = 15;
        var condition = dayEqualOperator.applyToField(dateField, BigDecimal.valueOf(dayValue));

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        logger.info("Generated SQL for dayEqual({}) : {}", dayValue, sql);

        assertTrue(sql.contains("day") || sql.contains("DAY"), "SQL should contain day extraction");
        assertTrue(sql.contains("15"), "SQL should contain the day value");

        logger.info("✓ DayEqualOperator correctly generates condition");
    }

    @Test
    @DisplayName("MonthEqualOperator should create correct condition")
    void testMonthEqualOperator() {
        logger.info("=== Testing MonthEqualOperator ===");

        Integer monthValue = 6;
        var condition = monthEqualOperator.applyToField(dateField, BigDecimal.valueOf(monthValue));

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        logger.info("Generated SQL for monthEqual({}) : {}", monthValue, sql);

        assertTrue(sql.contains("month") || sql.contains("MONTH"), "SQL should contain month extraction");
        assertTrue(sql.contains("6"), "SQL should contain the month value");

        logger.info("✓ MonthEqualOperator correctly generates condition");
    }

    @Test
    @DisplayName("YearEqualOperator should create correct condition")
    void testYearEqualOperator() {
        logger.info("=== Testing YearEqualOperator ===");

        Integer yearValue = 2023;
        var condition = yearEqualOperator.applyToField(dateField, BigDecimal.valueOf(yearValue));

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        logger.info("Generated SQL for yearEqual({}) : {}", yearValue, sql);

        assertTrue(sql.contains("year") || sql.contains("YEAR"), "SQL should contain year extraction");
        assertTrue(sql.contains("2023"), "SQL should contain the year value");

        logger.info("✓ YearEqualOperator correctly generates condition");
    }

    @Test
    @DisplayName("DaysAfterOperator should create correct condition")
    void testDaysAfterOperator() {
        logger.info("=== Testing DaysAfterOperator ===");

        Integer daysValue = 10;
        var condition = daysAfterOperator.applyToField(dateField, BigDecimal.valueOf(daysValue));

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        logger.info("Generated SQL for daysAfter({}) : {}", daysValue, sql);

        LocalDate expectedDate = LocalDate.now().plusDays(10);
        assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equality operator");
        assertTrue(sql.contains(expectedDate.toString()), "SQL should contain the calculated date");

        logger.info("✓ DaysAfterOperator correctly generates condition");
    }

    @Test
    @DisplayName("MonthsAfterOperator should create correct condition")
    void testMonthsAfterOperator() {
        logger.info("=== Testing MonthsAfterOperator ===");

        Integer monthsValue = 6;
        var condition = monthsAfterOperator.applyToField(dateField, BigDecimal.valueOf(monthsValue));

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        logger.info("Generated SQL for monthsAfter({}) : {}", monthsValue, sql);

        LocalDate expectedDate = LocalDate.now().plusMonths(6);
        assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equality operator");
        assertTrue(sql.contains(expectedDate.toString()), "SQL should contain the calculated date");

        logger.info("✓ MonthsAfterOperator correctly generates condition");
    }

    @Test
    @DisplayName("YearsAfterOperator should create correct condition")
    void testYearsAfterOperator() {
        logger.info("=== Testing YearsAfterOperator ===");

        Integer yearsValue = 5;
        var condition = yearsAfterOperator.applyToField(dateField, BigDecimal.valueOf(yearsValue));

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        logger.info("Generated SQL for yearsAfter({}) : {}", yearsValue, sql);

        LocalDate expectedDate = LocalDate.now().plusYears(5);
        assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equality operator");
        assertTrue(sql.contains(expectedDate.toString()), "SQL should contain the calculated date");

        logger.info("✓ YearsAfterOperator correctly generates condition");
    }

    @Test
    @DisplayName("All operators should work with different field names")
    void testWithDifferentFields() {
        logger.info("=== Testing operators with different field names ===");

        Field<LocalDate> createdField = DSL.field("created_at", LocalDate.class);
        Field<LocalDate> updatedField = DSL.field("updated_at", LocalDate.class);

        // Test a few operators with different fields
        var createdCondition = daysBeforeOperator.applyToField(createdField, BigDecimal.valueOf(30));
        assertNotNull(createdCondition);
        logger.info("Days before with created_at field: {}", createdCondition);

        var updatedCondition = monthEqualOperator.applyToField(updatedField, BigDecimal.valueOf(12));
        assertNotNull(updatedCondition);
        logger.info("Month equal with updated_at field: {}", updatedCondition);

        logger.info("✓ All operators work correctly with different field names");
    }
}
