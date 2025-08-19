//package com.example.qe.model.operator.impl;
//
//import com.example.qe.model.query.Query;
//import com.example.qe.util.OperatorFactory;
//import com.example.qe.util.OperatorRegistry;
//import com.example.qe.util.OperatorScanner;
//import com.example.qe.util.QueryExecutionContext;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.jooq.Condition;
//import org.jooq.DSLContext;
//import org.jooq.SQLDialect;
//import org.jooq.impl.DSL;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.core.convert.support.DefaultConversionService;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DisplayName("InOperator Tests")
//class InOperatorTest {
//
//    private static final Logger logger = LoggerFactory.getLogger(InOperatorTest.class);
//
//    private ObjectMapper mapper;
//    private DSLContext dsl;
//    private QueryExecutionContext context;
//
//    @BeforeEach
//    void setUp() {
//        mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
//        dsl = DSL.using(SQLDialect.DEFAULT);
//
//        OperatorRegistry registry = new OperatorRegistry();
//        OperatorScanner scanner = new OperatorScanner(registry);
//        scanner.scanAndRegister("com.example.qe.model.operator");
//
//        OperatorFactory factory = new OperatorFactory(registry);
//        context = new QueryExecutionContext(factory, new DefaultConversionService());
//    }
//
//    @Test
//    @DisplayName("apply_givenValidStringFieldWithMultipleValues_shouldReturnInCondition")
//    void apply_givenValidStringFieldWithMultipleValues_shouldReturnInCondition() throws Exception {
//        String json = """
//        {
//          "type": "StringQuery",
//          "column": "status",
//          "operator": "in",
//          "value": ["active", "pending", "approved"]
//        }
//        """;
//
//        Query query = mapper.readValue(json, Query.class);
//        Condition condition = query.toCondition(dsl, context);
//        String sql = dsl.renderInlined(condition);
//
//        logger.info("String in operator SQL: {}", sql);
//
//        assertAll("String in operator validation",
//                () -> assertNotNull(condition, "Condition should not be null"),
//                () -> assertTrue(sql.contains("status"), "SQL should contain column name"),
//                () -> assertTrue(sql.toLowerCase().contains("in"), "SQL should contain IN operator"),
//                () -> assertTrue(sql.contains("active"), "SQL should contain 'active' value"),
//                () -> assertTrue(sql.contains("pending"), "SQL should contain 'pending' value"),
//                () -> assertTrue(sql.contains("approved"), "SQL should contain 'approved' value")
//        );
//    }
//
//    @Test
//    @DisplayName("apply_givenValidIntegerFieldWithMultipleValues_shouldReturnInCondition")
//    void apply_givenValidIntegerFieldWithMultipleValues_shouldReturnInCondition() throws Exception {
//        String json = """
//        {
//          "type": "NumericQuery",
//          "column": "category_id",
//          "operator": "in",
//          "value": [1, 3, 5, 7]
//        }
//        """;
//
//        Query query = mapper.readValue(json, Query.class);
//        Condition condition = query.toCondition(dsl, context);
//        String sql = dsl.renderInlined(condition);
//
//        logger.info("Integer in operator SQL: {}", sql);
//
//        assertAll("Integer in operator validation",
//                () -> assertNotNull(condition, "Condition should not be null"),
//                () -> assertTrue(sql.contains("category_id"), "SQL should contain column name"),
//                () -> assertTrue(sql.toLowerCase().contains("in"), "SQL should contain IN operator"),
//                () -> assertTrue(sql.contains("1"), "SQL should contain value 1"),
//                () -> assertTrue(sql.contains("3"), "SQL should contain value 3"),
//                () -> assertTrue(sql.contains("5"), "SQL should contain value 5"),
//                () -> assertTrue(sql.contains("7"), "SQL should contain value 7")
//        );
//    }
//
//    @Test
//    @DisplayName("apply_givenValidDoubleFieldWithMultipleValues_shouldReturnInCondition")
//    void apply_givenValidDoubleFieldWithMultipleValues_shouldReturnInCondition() throws Exception {
//        String json = """
//        {
//          "type": "NumericQuery",
//          "column": "price",
//          "operator": "in",
//          "value": [9.99, 19.99, 29.99]
//        }
//        """;
//
//        Query query = mapper.readValue(json, Query.class);
//        Condition condition = query.toCondition(dsl, context);
//        String sql = dsl.renderInlined(condition);
//
//        logger.info("Double in operator SQL: {}", sql);
//
//        assertAll("Double in operator validation",
//                () -> assertNotNull(condition, "Condition should not be null"),
//                () -> assertTrue(sql.contains("price"), "SQL should contain column name"),
//                () -> assertTrue(sql.toLowerCase().contains("in"), "SQL should contain IN operator"),
//                () -> assertTrue(sql.contains("9.99"), "SQL should contain value 9.99"),
//                () -> assertTrue(sql.contains("19.99"), "SQL should contain value 19.99"),
//                () -> assertTrue(sql.contains("29.99"), "SQL should contain value 29.99")
//        );
//    }
//
//    @Test
//    @DisplayName("apply_givenSingleValueInArray_shouldReturnInCondition")
//    void apply_givenSingleValueInArray_shouldReturnInCondition() throws Exception {
//        String json = """
//        {
//          "type": "StringQuery",
//          "column": "region",
//          "operator": "in",
//          "value": ["APAC"]
//        }
//        """;
//
//        Query query = mapper.readValue(json, Query.class);
//        Condition condition = query.toCondition(dsl, context);
//        String sql = dsl.renderInlined(condition);
//
//        logger.info("Single value in operator SQL: {}", sql);
//
//        assertAll("Single value in operator validation",
//                () -> assertNotNull(condition, "Condition should not be null"),
//                () -> assertTrue(sql.contains("region"), "SQL should contain column name"),
//                () -> assertTrue(sql.toLowerCase().contains("in") || sql.contains("="), "SQL should contain IN or equals operator"),
//                () -> assertTrue(sql.contains("APAC"), "SQL should contain APAC value")
//        );
//    }
//
//    @Test
//    @DisplayName("apply_givenEmptyArray_shouldThrowException")
//    void apply_givenEmptyArray_shouldThrowException() {
//        String json = """
//        {
//          "type": "StringQuery",
//          "column": "status",
//          "operator": "in",
//          "value": []
//        }
//        """;
//
//        assertThrows(Exception.class, () -> {
//            Query query = mapper.readValue(json, Query.class);
//            query.toCondition(dsl, context);
//        }, "Should throw exception for empty array in IN operator");
//    }
//
//    @Test
//    @DisplayName("apply_givenComplexNestedQuery_shouldReturnValidCondition")
//    void apply_givenComplexNestedQuery_shouldReturnValidCondition() throws Exception {
//        String json = """
//        {
//          "type": "AndQuery",
//          "children": [
//            {
//              "type": "StringQuery",
//              "column": "department",
//              "operator": "in",
//              "value": ["IT", "HR", "Finance"]
//            },
//            {
//              "type": "NumericQuery",
//              "column": "level",
//              "operator": "in",
//              "value": [3, 4, 5]
//            }
//          ]
//        }
//        """;
//
//        Query query = mapper.readValue(json, Query.class);
//        Condition condition = query.toCondition(dsl, context);
//        String sql = dsl.renderInlined(condition);
//
//        logger.info("Complex nested in operator SQL: {}", sql);
//
//        assertAll("Complex nested in operator validation",
//                () -> assertNotNull(condition, "Condition should not be null"),
//                () -> assertTrue(sql.toLowerCase().contains("and"), "SQL should contain AND operator"),
//                () -> assertTrue(sql.contains("department"), "SQL should contain department column"),
//                () -> assertTrue(sql.contains("IT"), "SQL should contain IT value"),
//                () -> assertTrue(sql.contains("HR"), "SQL should contain HR value"),
//                () -> assertTrue(sql.contains("Finance"), "SQL should contain Finance value"),
//                () -> assertTrue(sql.contains("level"), "SQL should contain level column"),
//                () -> assertTrue(sql.contains("3"), "SQL should contain level 3"),
//                () -> assertTrue(sql.contains("4"), "SQL should contain level 4"),
//                () -> assertTrue(sql.contains("5"), "SQL should contain level 5")
//        );
//    }
//}
