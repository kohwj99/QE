package com.example.qe.model.operator.impl;

import com.example.qe.queryengine.operator.impl.DayEqualOperator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DayEqualOperator Unit Tests")
class DayEqualOperatorUnitTest {

    private DayEqualOperator operator;
    private Field<?> field;

    @BeforeEach
    void setUp() {
        operator = new DayEqualOperator();
        field = DSL.field("birth_date");
    }

    @Test
    void applyToField_givenValidDay_shouldReturnDayEqualCondition() {
        // Arrange
        BigDecimal dayValue = BigDecimal.valueOf(15);

        // Act
        Condition condition = operator.applyToField(field, dayValue);

        // Assert
        assertConditionContains(condition, "birth_date", "15");
    }

    @Test
    void applyToField_givenFirstDayOfMonth_shouldReturnDayEqualCondition() {
        // Arrange
        BigDecimal dayValue = BigDecimal.valueOf(1);

        // Act
        Condition condition = operator.applyToField(field, dayValue);

        // Assert
        assertConditionContains(condition, "birth_date", "1");
    }

    @Test
    void applyToField_givenLastDayOfMonth_shouldReturnDayEqualCondition() {
        // Arrange
        BigDecimal dayValue = BigDecimal.valueOf(31);

        // Act
        Condition condition = operator.applyToField(field, dayValue);

        // Assert
        assertConditionContains(condition, "birth_date", "31");
    }

    @Test
    void applyToField_givenMidMonthDay_shouldReturnDayEqualCondition() {
        // Arrange
        BigDecimal dayValue = BigDecimal.valueOf(25);

        // Act
        Condition condition = operator.applyToField(field, dayValue);

        // Assert
        assertConditionContains(condition, "birth_date", "25");
    }

    @Test
    void applyToField_givenZeroDay_shouldReturnDayEqualCondition() {
        // Arrange
        BigDecimal dayValue = BigDecimal.valueOf(0);

        // Act
        Condition condition = operator.applyToField(field, dayValue);

        // Assert
        assertConditionContains(condition, "birth_date", "0");
    }

    private void assertConditionContains(Condition condition, String... expected) {
        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        for (String s : expected) {
            assertTrue(sql.contains(s), "SQL should contain: " + s);
        }
    }
}