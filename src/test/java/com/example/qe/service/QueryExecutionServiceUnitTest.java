//package com.example.qe.service;
//
//import com.example.qe.queryengine.QueryExecutionService;
//import com.example.qe.queryengine.operator.OperatorFactory;
//import com.example.qe.queryengine.operator.OperatorRegistry;
//import com.example.qe.queryengine.operator.impl.EqualsOperator;
//import com.example.qe.queryengine.operator.impl.DayEqualOperator;
//import com.example.qe.queryengine.query.Query;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.jooq.Condition;
//import org.jooq.DSLContext;
//import org.jooq.SQLDialect;
//import org.jooq.impl.DSL;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DisplayName("QueryExecutionService Full Flow Unit Tests")
//class QueryExecutionServiceUnitTest {
//
//    private QueryExecutionService queryExecutionService;
//    private OperatorFactory operatorFactory;
//    private OperatorRegistry operatorRegistry;
//    private DSLContext dsl;
//    private ObjectMapper objectMapper;
//
//    @BeforeEach
//    void setUp() {
//        operatorRegistry = new OperatorRegistry();
//        operatorRegistry.register("equals", new Class[]{String.class}, new Class[]{String.class}, new EqualsOperator());
//        operatorRegistry.register("equals", new Class[]{Boolean.class}, new Class[]{Boolean.class}, new EqualsOperator());
//        operatorRegistry.register("equals", new Class[]{LocalDate.class}, new Class[]{LocalDate.class}, new EqualsOperator());
//        operatorRegistry.register("equals", new Class[]{BigDecimal.class}, new Class[]{BigDecimal.class}, new EqualsOperator());
//        operatorRegistry.register("dayEqual", new Class[]{LocalDate.class}, new Class[]{BigDecimal.class}, new DayEqualOperator());
//
//        operatorFactory = new OperatorFactory(operatorRegistry);
//        dsl = DSL.using(SQLDialect.DEFAULT);
//        queryExecutionService = new QueryExecutionService(operatorFactory, dsl);
//
//        objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//    }
//
//    @Test
//    @DisplayName("parseJsonToCondition_givenBoolQuery_shouldValidateFullFlow")
//    void parseJsonToCondition_givenBoolQuery_shouldValidateFullFlow() throws Exception {
//        String json = """
//        {
//          "type": "BoolQuery",
//          "column": "is_active",
//          "operator": "equals",
//          "value": true
//        }
//        """;
//
//        // Deserialize
//        Query query = objectMapper.readValue(json, Query.class);
//        assertEquals("BoolQuery", query.getType());
//
//        // Operator resolution
//        var operator = operatorFactory.resolve("equals", Boolean.class);
//        assertNotNull(operator);
//
//        // Condition generation
//        Condition condition = queryExecutionService.parseJsonToCondition(json);
//        assertNotNull(condition);
//
//        // SQL output
//        String sql = condition.toString();
//        assertTrue(sql.contains("is_active"));
//        assertTrue(sql.contains("true"));
//    }
//
//    @Test
//    @DisplayName("parseJsonToCondition_givenStringQuery_shouldValidateFullFlow")
//    void parseJsonToCondition_givenStringQuery_shouldValidateFullFlow() throws Exception {
//        String json = """
//        {
//          "type": "StringQuery",
//          "column": "department",
//          "operator": "equals",
//          "value": "Engineering"
//        }
//        """;
//
//        Query query = objectMapper.readValue(json, Query.class);
//        assertEquals("StringQuery", query.getType());
//
//        var operator = operatorFactory.resolve("equals", String.class);
//        assertNotNull(operator);
//
//        Condition condition = queryExecutionService.parseJsonToCondition(json);
//        assertNotNull(condition);
//
//        String sql = condition.toString();
//        assertTrue(sql.contains("department"));
//        assertTrue(sql.contains("Engineering"));
//    }
//
//    @Test
//    @DisplayName("parseJsonToCondition_givenLocalDateQueryWithEquals_shouldValidateFullFlow")
//    void parseJsonToCondition_givenLocalDateQueryWithEquals_shouldValidateFullFlow() throws Exception {
//        String json = """
//        {
//          "type": "LocalDateQuery",
//          "column": "created_date",
//          "operator": "equals",
//          "value": "2024-06-01"
//        }
//        """;
//
//        Query query = objectMapper.readValue(json, Query.class);
//        assertEquals("LocalDateQuery", query.getType());
//
//        var operator = operatorFactory.resolve("equals", LocalDate.class);
//        assertNotNull(operator);
//
//        Condition condition = queryExecutionService.parseJsonToCondition(json);
//        assertNotNull(condition);
//
//        String sql = condition.toString();
//        assertTrue(sql.contains("created_date"));
//        assertTrue(sql.contains("2024-06-01"));
//    }
//
//    @Test
//    @DisplayName("parseJsonToCondition_givenLocalDateQueryWithDayEqual_shouldValidateFullFlow")
//    void parseJsonToCondition_givenLocalDateQueryWithDayEqual_shouldValidateFullFlow() throws Exception {
//        String json = """
//        {
//          "type": "LocalDateQuery",
//          "column": "created_date",
//          "operator": "dayEqual",
//          "value": 1
//        }
//        """;
//
//        Query query = objectMapper.readValue(json, Query.class);
//        assertEquals("LocalDateQuery", query.getType());
//
//        var operator = operatorFactory.resolve("dayEqual", LocalDate.class, BigDecimal.class);
//        assertNotNull(operator);
//
//        Condition condition = queryExecutionService.parseJsonToCondition(json);
//        assertNotNull(condition);
//
//        String sql = condition.toString();
//        assertTrue(sql.contains("created_date"));
//        assertTrue(sql.contains("1"));
//    }
//
//    @Test
//    @DisplayName("parseJsonToCondition_givenNumericQuery_shouldValidateFullFlow")
//    void parseJsonToCondition_givenNumericQuery_shouldValidateFullFlow() throws Exception {
//        String json = """
//        {
//          "type": "NumericQuery",
//          "column": "salary",
//          "operator": "equals",
//          "value": 50000
//        }
//        """;
//
//        Query query = objectMapper.readValue(json, Query.class);
//        assertEquals("NumericQuery", query.getType());
//
//        var operator = operatorFactory.resolve("equals", BigDecimal.class);
//        assertNotNull(operator);
//
//        Condition condition = queryExecutionService.parseJsonToCondition(json);
//        assertNotNull(condition);
//
//        String sql = condition.toString();
//        assertTrue(sql.contains("salary"));
//        assertTrue(sql.contains("50000"));
//    }
//}