package com.example.qe.queryengine.operator;

import com.example.qe.queryengine.operator.impl.NotEqualsOperator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class NotEqualsOperatorTest {

    private NotEqualsOperator operator;

    @BeforeEach
    void setup() {
        operator = new NotEqualsOperator();
    }

    private String renderSql(Condition condition) {
        String sql = DSL.using(SQLDialect.DEFAULT).renderInlined(condition);
        System.out.println("Generated SQL: " + sql);
        return sql.replaceAll("\\s+", "").toLowerCase();
    }

    // --- Positive cases ---

    @Test
    void apply_givenStringFieldAndValue_shouldReturnNotEqualsCondition() {
        Field<String> field = DSL.field("name", String.class);
        String value = "Alice";

        Condition condition = operator.apply(field, value);
        String sql = renderSql(condition);

        assertTrue(sql.contains("name<>'alice'"));
    }

    @Test
    void apply_givenBigDecimalFieldAndValue_shouldReturnNotEqualsCondition() {
        Field<BigDecimal> field = DSL.field("amount", BigDecimal.class);
        BigDecimal value = BigDecimal.valueOf(123.45);

        Condition condition = operator.apply(field, value);
        String sql = renderSql(condition);

        assertTrue(sql.contains("amount<>123.45"));
    }

    @Test
    void apply_givenBooleanFieldAndValue_shouldReturnNotEqualsCondition() {
        Field<Boolean> field = DSL.field("active", Boolean.class);
        Boolean value = true;

        Condition condition = operator.apply(field, value);
        String sql = renderSql(condition);

        assertTrue(sql.contains("active<>true"));
    }

    @Test
    void apply_givenLocalDateFieldAndValue_shouldReturnNotEqualsCondition() {
        Field<LocalDate> field = DSL.field("created", LocalDate.class);
        LocalDate value = LocalDate.of(2024, 6, 1);

        Condition condition = operator.apply(field, value);
        String sql = renderSql(condition);

        assertTrue(sql.contains("created<>date'2024-06-01'"));
    }

    // --- Null and mismatched type cases ---

    @Test
    void apply_givenNullValue_shouldReturnIsNullCondition() {
        Field<String> field = DSL.field("name", String.class);

        Condition condition = operator.apply(field, null);
        String sql = renderSql(condition);

        assertTrue(sql.contains("nameisnotnull"));
    }

    @Test
    void apply_givenNullField_shouldThrowNullPointerException() {
        Field<String> field = null;
        String value = "Alice";

        assertThrows(NullPointerException.class, () -> operator.apply(field, value));
    }

    // --- Negative and edge cases ---

    @Test
    void apply_givenEmptyString_shouldReturnNotEqualsCondition() {
        Field<String> field = DSL.field("description", String.class);
        String value = "";

        Condition condition = operator.apply(field, value);
        String sql = renderSql(condition);

        assertTrue(sql.contains("description<>''"));
    }

    @Test
    void apply_givenZeroNumeric_shouldReturnNotEqualsCondition() {
        Field<BigDecimal> field = DSL.field("quantity", BigDecimal.class);
        BigDecimal value = BigDecimal.ZERO;

        Condition condition = operator.apply(field, value);
        String sql = renderSql(condition);

        assertTrue(sql.contains("quantity<>0"));
    }

    @Test
    void apply_givenFalseBoolean_shouldReturnNotEqualsCondition() {
        Field<Boolean> field = DSL.field("enabled", Boolean.class);
        Boolean value = false;

        Condition condition = operator.apply(field, value);
        String sql = renderSql(condition);

        assertTrue(sql.contains("enabled<>false"));
    }

    @Test
    void apply_givenEpochDate_shouldReturnNotEqualsCondition() {
        Field<LocalDate> field = DSL.field("start_date", LocalDate.class);
        LocalDate value = LocalDate.ofEpochDay(0); // 1970-01-01

        Condition condition = operator.apply(field, value);
        String sql = renderSql(condition);

        assertTrue(sql.contains("start_date<>date'1970-01-01'"));
    }

    @Test
    void apply_givenNegativeNumeric_shouldReturnNotEqualsCondition() {
        Field<BigDecimal> field = DSL.field("balance", BigDecimal.class);
        BigDecimal value = BigDecimal.valueOf(-100.5);

        Condition condition = operator.apply(field, value);
        String sql = renderSql(condition);

        assertTrue(sql.contains("balance<>-100.5"));
    }
}
