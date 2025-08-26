package com.example.qe.integration;

import com.example.qe.queryengine.query.Query;
import com.example.qe.queryengine.replacement.ReplacementResolver;
import com.example.qe.queryengine.replacement.impl.BasicPlaceholderResolver;
import com.example.qe.queryengine.QueryExecutionService;
import com.example.qe.queryengine.replacement.ReplacementService;
import com.example.qe.queryengine.operator.OperatorFactory;
import com.example.qe.queryengine.operator.OperatorRegistry;
import com.example.qe.queryengine.operator.OperatorScanner;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Simplified integration test that demonstrates the complete flow from JSON query input
 * to JOOQ Condition output. This test validates the core pipeline functionality.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Query Processing Integration Tests")
class SimpleQueryProcessingIntegrationTest {

    private static final Logger logger = LoggerFactory.getLogger(SimpleQueryProcessingIntegrationTest.class);

    private QueryExecutionService queryExecutionService;
    private OperatorRegistry operatorRegistry;
    private OperatorFactory operatorFactory;
    private DSLContext dsl;
    @BeforeEach
    void setUp() {
        logger.info("=== Setting up Integration Test ===");

        // Initialize components
        operatorRegistry = new OperatorRegistry();
        OperatorScanner operatorScanner = new OperatorScanner(operatorRegistry);

        // Scan and register operators
        operatorScanner.scanAndRegister("com.example.qe.queryengine.operator.impl");
        logger.info("Registered {} operators", operatorRegistry.getAllOperatorNames().size());

        operatorFactory = new OperatorFactory(operatorRegistry);

        // Create ValueResolver list for ReplacementService
        List<ReplacementResolver> resolvers = List.of(new BasicPlaceholderResolver());
        ReplacementService replacementService = new ReplacementService(resolvers);
        dsl = DSL.using(SQLDialect.DEFAULT);
        // Initialize service
        queryExecutionService = new QueryExecutionService(
            operatorFactory,
            dsl,
            replacementService
        );

        logger.info("Setup complete");
    }

    @Test
    @Order(1)
    @DisplayName("processStringQuery_givenValidInput_shouldGenerateCondition")
    void processStringQuery_givenValidInput_shouldGenerateCondition() throws Exception {
        logger.info("=== Test: String Query Processing ===");

        String jsonInput = """
        {
          "type": "StringQuery",
          "column": "name",
          "operator": "equals",
          "value": "John Doe"
        }
        """;

        logger.info("Input: {}", jsonInput);

        Condition condition = queryExecutionService.parseJsonToCondition(jsonInput);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        logger.info("Generated SQL: {}", sql);

        assertTrue(sql.contains("name"), "Should contain field name");
        assertTrue(sql.contains("John Doe"), "Should contain value");

        logger.info("✓ String query test passed");
    }

    @Test
    @Order(2)
    @DisplayName("processNumericQuery_givenValidInput_shouldGenerateCondition")
    void processNumericQuery_givenValidInput_shouldGenerateCondition() throws Exception {
        logger.info("=== Test: Numeric Query Processing ===");

        String jsonInput = """
        {
          "type": "NumericQuery",
          "column": "amount",
          "operator": "greaterThan",
          "value": "100.50"
        }
        """;

        logger.info("Input: {}", jsonInput);

        Condition condition = queryExecutionService.parseJsonToCondition(jsonInput);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        logger.info("Generated SQL: {}", sql);

        assertTrue(sql.contains("amount"), "Should contain field name");
        assertTrue(sql.contains("100.50"), "Should contain value");

        logger.info("✓ Numeric query test passed");
    }

    @Test
    @Order(3)
    @DisplayName("processComplexQuery_givenAndLogic_shouldGenerateCondition")
    void processComplexQuery_givenAndLogic_shouldGenerateCondition() throws Exception {
        logger.info("=== Test: Complex AND Query Processing ===");

        String jsonInput = """
        {
          "type": "AndQuery",
          "children": [
            {
              "type": "StringQuery",
              "column": "status",
              "operator": "equals",
              "value": "ACTIVE"
            },
            {
              "type": "NumericQuery",
              "column": "amount",
              "operator": "greaterThan",
              "value": "50.00"
            }
          ]
        }
        """;

        logger.info("Input: {}", jsonInput);

        Condition condition = queryExecutionService.parseJsonToCondition(jsonInput);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        logger.info("Generated SQL: {}", sql);

        assertTrue(sql.contains("status"), "Should contain status field");
        assertTrue(sql.contains("amount"), "Should contain amount field");
        assertTrue(sql.toLowerCase().contains("and"), "Should contain AND logic");

        logger.info("✓ Complex query test passed");
    }

    @Test
    @Order(4)
    @DisplayName("processInvalidOperator_givenInvalidInput_shouldThrowException")
    void processInvalidOperator_givenInvalidInput_shouldThrowException() {
        logger.info("=== Test: Invalid Operator Error Handling ===");

        String jsonInput = """
        {
          "type": "StringQuery",
          "column": "name",
          "operator": "invalidOperator",
          "value": "test"
        }
        """;

        logger.info("Input with invalid operator: {}", jsonInput);

        assertThrows(Exception.class, () -> {
            queryExecutionService.parseJsonToCondition(jsonInput);
        }, "Should throw exception for invalid operator");

        logger.info("✓ Error handling test passed");
    }

    @Test
    @Order(5)
    @DisplayName("validatePipelineComponents_givenSetup_shouldValidateAllComponents")
    void validatePipelineComponents_givenSetup_shouldValidateAllComponents() throws Exception {
        logger.info("=== Test: Complete Pipeline Validation ===");

        // Validate OperatorScanner
        assertTrue(operatorRegistry.getAllOperatorNames().size() > 0,
                  "Should have registered operators");
        logger.info("✓ OperatorScanner: {} operators registered",
                   operatorRegistry.getAllOperatorNames().size());

        // Validate OperatorFactory
        var equalsOp = operatorFactory.resolve("equals", String.class);
        assertNotNull(equalsOp, "Should resolve equals operator");
        logger.info("✓ OperatorFactory: Resolved {}", equalsOp.getClass().getSimpleName());

        // Validate complete flow
        String testJson = """
        {
          "type": "StringQuery",
          "column": "test",
          "operator": "equals",
          "value": "value"
        }
        """;

        Condition condition = queryExecutionService.parseJsonToCondition(testJson);
        assertNotNull(condition, "Should generate condition");
        logger.info("✓ Complete flow: Generated SQL: {}", condition.toString());

        // Validate direct Query processing
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        Query query = mapper.readValue(testJson, Query.class);
        assertNotNull(query, "Should create Query object");

        DSLContext dsl = DSL.using(SQLDialect.DEFAULT);
        Condition directCondition = query.toCondition(dsl, operatorFactory);
        assertNotNull(directCondition, "Should generate condition directly");
        logger.info("✓ Direct Query processing working");

        logger.info("🎉 Complete pipeline validation successful!");
    }

    @AfterEach
    void tearDown() {
        logger.info("Test completed");
    }
}
