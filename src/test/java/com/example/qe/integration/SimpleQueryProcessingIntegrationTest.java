//package com.example.qe.integration;
//
//import com.example.qe.queryengine.QueryExecutionService;
//import com.example.qe.queryengine.operator.OperatorFactory;
//import com.example.qe.queryengine.operator.OperatorRegistry;
//import com.example.qe.queryengine.operator.impl.DayEqualOperator;
//import com.example.qe.queryengine.operator.impl.EqualsOperator;
//import com.example.qe.queryengine.query.Query;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.jooq.Condition;
//import org.jooq.DSLContext;
//import org.jooq.SQLDialect;
//import org.jooq.impl.DSL;
//import org.junit.jupiter.api.*;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//class SimpleQueryProcessingIntegrationTest {
//
//    private QueryExecutionService queryExecutionService;
//    private OperatorRegistry operatorRegistry;
//    private OperatorFactory operatorFactory;
//    private DSLContext dsl;
//    private ObjectMapper objectMapper;
//
//    @BeforeEach
//    void arrange_setup() {
//        operatorRegistry = new OperatorRegistry();
//        operatorRegistry.register("equals", new Class[]{String.class}, new Class[]{String.class}, new EqualsOperator());
//        operatorRegistry.register("equals", new Class[]{Boolean.class}, new Class[]{Boolean.class}, new EqualsOperator());
//        operatorRegistry.register("equals", new Class[]{LocalDate.class}, new Class[]{LocalDate.class}, new EqualsOperator());
//        operatorRegistry.register("equals", new Class[]{BigDecimal.class}, new Class[]{BigDecimal.class}, new EqualsOperator());
//        operatorRegistry.register("dayEqual", new Class[]{LocalDate.class}, new Class[]{BigDecimal.class}, new DayEqualOperator());
//
//        operatorFactory = new OperatorFactory(operatorRegistry);
//        dsl = DSL.using(SQLDialect.DEFAULT);
//        ReplacementService mockReplacementService = mock(ReplacementService.class);
//        queryExecutionService = new QueryExecutionService(operatorFactory, dsl, mockReplacementService);
//
//        objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//    }
//
//    @Test
//    @Order(1)
//    void parseJsonToCondition_givenBoolQuery_shouldGenerateCondition() throws Exception {
//        // Arrange
//        String json = """
//        {
//          "type": "BoolQuery",
//          "column": "is_active",
//          "operator": "equals",
//          "value": true
//        }
//        """;
//        when(queryExecutionService.getReplacementService().processJsonPlaceholders(json)).thenReturn(json);
//
//        // Act
//        Query query = objectMapper.readValue(json, Query.class);
//        assertEquals("BoolQuery", query.getType());
//        var operator = operatorFactory.resolve("equals", Boolean.class);
//        assertNotNull(operator);
//        Condition condition = queryExecutionService.parseJsonToCondition(json);
//
//        // Assert
//        assertNotNull(condition);
//        String sql = condition.toString();
//        assertTrue(sql.contains("is_active"));
//        assertTrue(sql.contains("true"));
//    }
//
//    @Test
//    @Order(2)
//    void parseJsonToCondition_givenStringQuery_shouldGenerateCondition() throws Exception {
//        // Arrange
//        String json = """
//        {
//          "type": "StringQuery",
//          "column": "department",
//          "operator": "equals",
//          "value": "Engineering"
//        }
//        """;
//        when(queryExecutionService.getReplacementService().processJsonPlaceholders(json)).thenReturn(json);
//
//        // Act
//        Query query = objectMapper.readValue(json, Query.class);
//        assertEquals("StringQuery", query.getType());
//        var operator = operatorFactory.resolve("equals", String.class);
//        assertNotNull(operator);
//        Condition condition = queryExecutionService.parseJsonToCondition(json);
//
//        // Assert
//        assertNotNull(condition);
//        String sql = condition.toString();
//        assertTrue(sql.contains("department"));
//        assertTrue(sql.contains("Engineering"));
//    }
//
//    @Test
//    @Order(3)
//    void parseJsonToCondition_givenLocalDateQueryWithEquals_shouldGenerateCondition() throws Exception {
//        // Arrange
//        String json = """
//        {
//          "type": "LocalDateQuery",
//          "column": "created_date",
//          "operator": "equals",
//          "value": "2024-06-01"
//        }
//        """;
//        when(queryExecutionService.getReplacementService().processJsonPlaceholders(json)).thenReturn(json);
//
//        // Act
//        Query query = objectMapper.readValue(json, Query.class);
//        assertEquals("LocalDateQuery", query.getType());
//        var operator = operatorFactory.resolve("equals", LocalDate.class);
//        assertNotNull(operator);
//        Condition condition = queryExecutionService.parseJsonToCondition(json);
//
//        // Assert
//        assertNotNull(condition);
//        String sql = condition.toString();
//        assertTrue(sql.contains("created_date"));
//        assertTrue(sql.contains("2024-06-01"));
//    }
//
//    @Test
//    @Order(4)
//    void parseJsonToCondition_givenLocalDateQueryWithDayEqual_shouldGenerateCondition() throws Exception {
//        // Arrange
//        String json = """
//        {
//          "type": "LocalDateQuery",
//          "column": "created_date",
//          "operator": "dayEqual",
//          "value": 1
//        }
//        """;
//        when(queryExecutionService.getReplacementService().processJsonPlaceholders(json)).thenReturn(json);
//
//        // Act
//        Query query = objectMapper.readValue(json, Query.class);
//        assertEquals("LocalDateQuery", query.getType());
//        var operator = operatorFactory.resolve("dayEqual", LocalDate.class, BigDecimal.class);
//        assertNotNull(operator);
//        Condition condition = queryExecutionService.parseJsonToCondition(json);
//
//        // Assert
//        assertNotNull(condition);
//        String sql = condition.toString();
//        assertTrue(sql.contains("created_date"));
//        assertTrue(sql.contains("1"));
//    }
//
//    @Test
//    @Order(5)
//    void parseJsonToCondition_givenNumericQuery_shouldGenerateCondition() throws Exception {
//        // Arrange
//        String json = """
//        {
//          "type": "NumericQuery",
//          "column": "salary",
//          "operator": "equals",
//          "value": 50000
//        }
//        """;
//        when(queryExecutionService.getReplacementService().processJsonPlaceholders(json)).thenReturn(json);
//
//        // Act
//        Query query = objectMapper.readValue(json, Query.class);
//        assertEquals("NumericQuery", query.getType());
//        var operator = operatorFactory.resolve("equals", BigDecimal.class);
//        assertNotNull(operator);
//        Condition condition = queryExecutionService.parseJsonToCondition(json);
//
//        // Assert
//        assertNotNull(condition);
//        String sql = condition.toString();
//        assertTrue(sql.contains("salary"));
//        assertTrue(sql.contains("50000"));
//    }
//}