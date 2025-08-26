package com.example.qe.model.operator.impl;

import com.example.qe.queryengine.operator.impl.LikeOperator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("LikeOperator Unit Tests")
class LikeOperatorUnitTest {

    private LikeOperator likeOperator;
    private Field<String> stringField;

    @BeforeEach
    void setUp() {
        likeOperator = new LikeOperator();
        stringField = DSL.field("description", String.class);
    }

    @Test
    @DisplayName("apply_givenWildcardPattern_shouldReturnLikeCondition")
    void apply_givenWildcardPattern_shouldReturnLikeCondition() {
        String value = "test%";

        Condition condition = likeOperator.apply(stringField, value);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("description"), "SQL should contain field name");
        assertTrue(sql.toLowerCase().contains("like"), "SQL should contain LIKE operator");
        assertTrue(sql.contains("test%"), "SQL should contain the pattern with wildcard");
    }

    @Test
    @DisplayName("apply_givenPrefixWildcard_shouldReturnLikeCondition")
    void apply_givenPrefixWildcard_shouldReturnLikeCondition() {
        String value = "%suffix";

        Condition condition = likeOperator.apply(stringField, value);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("description"), "SQL should contain field name");
        assertTrue(sql.toLowerCase().contains("like"), "SQL should contain LIKE operator");
        assertTrue(sql.contains("%suffix"), "SQL should contain the pattern with prefix wildcard");
    }

    @Test
    @DisplayName("apply_givenBothWildcards_shouldReturnLikeCondition")
    void apply_givenBothWildcards_shouldReturnLikeCondition() {
        String value = "%middle%";

        Condition condition = likeOperator.apply(stringField, value);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("description"), "SQL should contain field name");
        assertTrue(sql.toLowerCase().contains("like"), "SQL should contain LIKE operator");
        assertTrue(sql.contains("%middle%"), "SQL should contain the pattern with both wildcards");
    }

    @Test
    @DisplayName("apply_givenExactMatch_shouldReturnLikeCondition")
    void apply_givenExactMatch_shouldReturnLikeCondition() {
        String value = "exact_match";

        Condition condition = likeOperator.apply(stringField, value);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("description"), "SQL should contain field name");
        assertTrue(sql.toLowerCase().contains("like"), "SQL should contain LIKE operator");
        assertTrue(sql.contains("exact_match"), "SQL should contain the exact value");
    }

    @Test
    @DisplayName("apply_givenEmptyString_shouldReturnLikeCondition")
    void apply_givenEmptyString_shouldReturnLikeCondition() {
        String value = "";

        Condition condition = likeOperator.apply(stringField, value);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("description"), "SQL should contain field name");
        assertTrue(sql.toLowerCase().contains("like"), "SQL should contain LIKE operator");
    }
}
