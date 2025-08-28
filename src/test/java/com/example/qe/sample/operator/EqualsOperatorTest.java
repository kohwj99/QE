package com.example.qe.sample.operator;

import com.example.qe.queryengine.operator.impl.EqualsOperator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EqualsOperatorTest {

    private EqualsOperator operator;

    @BeforeEach
    void setup() {
        operator = new EqualsOperator();
    }

    private String renderSql(Condition condition) {
        String sql = DSL.using(SQLDialect.DEFAULT).renderInlined(condition);
        System.out.println("Generated SQL: " + sql);
        return sql.replaceAll("\\s+", "").toLowerCase();
    }

    @Test
    void apply_givenStringFieldAndValue_shouldGenerateCorrectSQL() {
        Field<String> field = DSL.field("name", String.class);
        String value = "Alice";

        Condition condition = operator.apply(field, value);
        String sql = renderSql(condition);

        assertTrue(sql.contains("name='alice'"));
    }

    @Test
    void apply_givenBigDecimalFieldAndValue_shouldGenerateCorrectSQL() {
        Field<BigDecimal> field = DSL.field("amount", BigDecimal.class);
        BigDecimal value = BigDecimal.valueOf(123.45);

        Condition condition = operator.apply(field, value);
        String sql = renderSql(condition);

        assertTrue(sql.contains("amount=123.45"));
    }

    @Test
    void apply_givenBooleanFieldAndValue_shouldGenerateCorrectSQL() {
        Field<Boolean> field = DSL.field("active", Boolean.class);
        Boolean value = true;

        Condition condition = operator.apply(field, value);
        String sql = renderSql(condition);

        assertTrue(sql.contains("active=true"));
    }

    @Test
    void apply_givenLocalDateFieldAndValue_shouldGenerateCorrectSQL() {
        Field<LocalDate> field = DSL.field("created", LocalDate.class);
        LocalDate value = LocalDate.of(2024, 6, 1);

        Condition condition = operator.apply(field, value);
        String sql = renderSql(condition);

        assertTrue(sql.contains("created=date'2024-06-01'"));
    }

    @Test
    void apply_givenMismatchedFieldAndValueType_shouldStillGenerateSQL() {
        Field<BigDecimal> field = DSL.field("amount", BigDecimal.class);
        String value = "not a number";

        Condition condition = operator.apply(field, value);
        String sql = renderSql(condition);
        System.out.println(sql);
        assertTrue(sql.contains("amount='notanumber'"));
    }

    @Test
    void apply_givenNullValue_shouldGenerateIsNullSQL() {
        Field<String> field = DSL.field("name", String.class);

        Condition condition = operator.apply(field, null);
        String sql = renderSql(condition);

        assertTrue(sql.contains("nameisnull"));
    }

    @Test
    void apply_givenNullField_shouldThrowNullPointerException() {
        Field<String> field = null;
        String value = "Alice";

        assertThrows(NullPointerException.class, () -> operator.apply(field, value));
    }
}
