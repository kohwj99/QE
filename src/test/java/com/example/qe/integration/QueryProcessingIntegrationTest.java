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
 * Comprehensive integration test that demonstrates the complete flow from JSON query input
 * to JOOQ Condition output. This test validates the entire pipeline including:
 * - JSON parsing through QueryExecutionService
 * - OperatorScanner discovery and registration
 * - OperatorFactory resolution
 * - Query object creation and processing
 * - JOOQ Condition generation
 * - Error handling and edge cases
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Query Processing Integration Tests - Complete Pipeline Validation")
class QueryProcessingIntegrationTest {

    private static final Logger logger = LoggerFactory.getLogger(QueryProcessingIntegrationTest.class);

    private QueryExecutionService queryExecutionService;
    private OperatorRegistry operatorRegistry;
    private OperatorFactory operatorFactory;
    private OperatorScanner operatorScanner;
    private ReplacementService replacementService;
    private DSLContext dsl;

    @BeforeEach
    void setUp() {
        logger.info("=== Setting up Integration Test Environment ===");

        try {
            // Initialize operator infrastructure
            operatorRegistry = new OperatorRegistry();
            operatorScanner = new OperatorScanner(operatorRegistry);

            logger.info("Scanning and registering operators...");
            operatorScanner.scanAndRegister("com.example.qe.queryengine.operator.impl");
            logger.info("Registered {} unique operators with {} total type registrations",
                       operatorRegistry.getAllOperatorNames().size(),
                       operatorRegistry.getTotalOperatorCount());

            operatorFactory = new OperatorFactory(operatorRegistry);

            // Create ValueResolver list for ReplacementService
            List<ReplacementResolver> resolvers = List.of(new BasicPlaceholderResolver());
            replacementService = new ReplacementService(resolvers);
            dsl = DSL.using(SQLDialect.DEFAULT);
            // Initialize QueryExecutionService with correct constructor
            queryExecutionService = new QueryExecutionService(
                    operatorFactory,
                    dsl,
                    replacementService
            );

            // Initialize DSL context for direct testing
            dsl = DSL.using(SQLDialect.DEFAULT);

            logger.info("Integration test environment setup complete");
        } catch (Exception e) {
            logger.error("Failed to setup integration test environment", e);
            throw new RuntimeException("Setup failed", e);
        }
    }

    /**
     * Test Case 1: Simple String Equality
     * Tests the complete flow for a basic string equality operation
     */
    @Test
    @Order(1)
    @DisplayName("parseJsonToCondition_givenSimpleStringEquality_shouldGenerateCorrectCondition")
    void parseJsonToCondition_givenSimpleStringEquality_shouldGenerateCorrectCondition() throws Exception {
        logger.info("=== Test Case 1: Simple String Equality ===");

        String jsonInput = """
        {
          "type": "StringQuery",
          "column": "name",
          "operator": "equals",
          "value": "John Doe"
        }
        """;

        logger.info("Input JSON: {}", jsonInput);

        try {
            // Process through QueryExecutionService
            Condition condition = queryExecutionService.parseJsonToCondition(jsonInput);

            // Validate results
            assertNotNull(condition, "Generated condition should not be null");
            String generatedSQL = condition.toString();
            logger.info("Generated JOOQ Condition: {}", generatedSQL);

            // Assertions
            assertTrue(generatedSQL.contains("name"), "SQL should contain field name");
            assertTrue(generatedSQL.contains("John Doe"), "SQL should contain the value");
            assertTrue(generatedSQL.contains("=") || generatedSQL.contains("eq"),
                      "SQL should contain equality operator");

            logger.info("✓ Simple String Equality test completed successfully");
        } catch (Exception e) {
            logger.error("Test failed with exception", e);
            throw e;
        }
    }

    /**
     * Test Case 2: Numeric Range Query
     * Tests numeric comparison with BigDecimal values
     */
    @Test
    @Order(2)
    @DisplayName("parseJsonToCondition_givenNumericRangeQuery_shouldGenerateCorrectCondition")
    void parseJsonToCondition_givenNumericRangeQuery_shouldGenerateCorrectCondition() throws Exception {
        logger.info("=== Test Case 2: Numeric Range Query ===");

        String jsonInput = """
        {
          "type": "NumericQuery",
          "column": "amount",
          "operator": "greaterThan",
          "value": "100.50"
        }
        """;

        logger.info("Input JSON: {}", jsonInput);

        try {
            // Process through QueryExecutionService
            Condition condition = queryExecutionService.parseJsonToCondition(jsonInput);

            // Validate results
            assertNotNull(condition, "Generated condition should not be null");
            String generatedSQL = condition.toString();
            logger.info("Generated JOOQ Condition: {}", generatedSQL);

            // Assertions
            assertTrue(generatedSQL.contains("amount"), "SQL should contain field name");
            assertTrue(generatedSQL.contains("100.50"), "SQL should contain the numeric value");
            assertTrue(generatedSQL.contains(">") || generatedSQL.contains("gt"),
                      "SQL should contain greater than operator");

            logger.info("✓ Numeric Range Query test completed successfully");
        } catch (Exception e) {
            logger.error("Test failed with exception", e);
            throw e;
        }
    }

    /**
     * Test Case 3: Date Comparison
     * Tests date comparison operations
     */
    @Test
    @Order(3)
    @DisplayName("parseJsonToCondition_givenDateComparison_shouldGenerateCorrectCondition")
    void parseJsonToCondition_givenDateComparison_shouldGenerateCorrectCondition() throws Exception {
        logger.info("=== Test Case 3: Date Comparison ===");

        String jsonInput = """
        {
          "type": "DateQuery",
          "column": "createdDate",
          "operator": "lessThan",
          "value": "2023-12-31"
        }
        """;

        logger.info("Input JSON: {}", jsonInput);

        try {
            // Process through QueryExecutionService
            Condition condition = queryExecutionService.parseJsonToCondition(jsonInput);

            // Validate results
            assertNotNull(condition, "Generated condition should not be null");
            String generatedSQL = condition.toString();
            logger.info("Generated JOOQ Condition: {}", generatedSQL);

            // Assertions
            assertTrue(generatedSQL.contains("createdDate"), "SQL should contain field name");
            assertTrue(generatedSQL.contains("2023-12-31"), "SQL should contain the date value");
            assertTrue(generatedSQL.contains("<") || generatedSQL.contains("lt"),
                      "SQL should contain less than operator");

            logger.info("✓ Date Comparison test completed successfully");
        } catch (Exception e) {
            logger.error("Test failed with exception", e);
            throw e;
        }
    }

    /**
     * Test Case 4: Complex Boolean Logic
     * Tests AND/OR logic with multiple conditions
     */
    @Test
    @Order(4)
    @DisplayName("parseJsonToCondition_givenComplexBooleanLogic_shouldGenerateCorrectCondition")
    void parseJsonToCondition_givenComplexBooleanLogic_shouldGenerateCorrectCondition() throws Exception {
        logger.info("=== Test Case 4: Complex Boolean Logic ===");

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
              "operator": "greaterThanEqual",
              "value": "50.00"
            }
          ]
        }
        """;

        logger.info("Input JSON: {}", jsonInput);

        try {
            // Process through QueryExecutionService
            Condition condition = queryExecutionService.parseJsonToCondition(jsonInput);

            // Validate results
            assertNotNull(condition, "Generated condition should not be null");
            String generatedSQL = condition.toString();
            logger.info("Generated JOOQ Condition: {}", generatedSQL);

            // Assertions for AND logic
            assertTrue(generatedSQL.contains("status"), "SQL should contain status field");
            assertTrue(generatedSQL.contains("amount"), "SQL should contain amount field");
            assertTrue(generatedSQL.contains("ACTIVE"), "SQL should contain status value");
            assertTrue(generatedSQL.contains("50.00"), "SQL should contain amount value");
            assertTrue(generatedSQL.toLowerCase().contains("and"), "SQL should contain AND logic");

            logger.info("✓ Complex Boolean Logic test completed successfully");
        } catch (Exception e) {
            logger.error("Test failed with exception", e);
            throw e;
        }
    }

    /**
     * Test Case 5: Error Handling - Invalid Operator
     * Tests error handling for unsupported operators
     */
    @Test
    @Order(5)
    @DisplayName("parseJsonToCondition_givenInvalidOperator_shouldThrowException")
    void parseJsonToCondition_givenInvalidOperator_shouldThrowException() {
        logger.info("=== Test Case 5: Invalid Operator Name ===");

        String jsonInput = """
        {
          "type": "StringQuery",
          "column": "name",
          "operator": "invalidOperator",
          "value": "test"
        }
        """;

        logger.info("Input JSON with invalid operator: {}", jsonInput);

        // Expect exception for invalid operator
        Exception exception = assertThrows(Exception.class, () -> {
            queryExecutionService.parseJsonToCondition(jsonInput);
        });

        // Validate error handling
        logger.info("Expected exception caught: {} - {}", exception.getClass().getSimpleName(), exception.getMessage());
        assertNotNull(exception.getMessage(), "Exception should have a message");

        logger.info("✓ Invalid Operator Name test completed successfully");
    }

    /**
     * Test Case 6: Error Handling - Null Input
     * Tests error handling when JSON input is null
     */
    @Test
    @Order(6)
    @DisplayName("parseJsonToCondition_givenNullInput_shouldThrowIllegalArgumentException")
    void parseJsonToCondition_givenNullInput_shouldThrowIllegalArgumentException() {
        logger.info("=== Test Case 6: Null JSON Input ===");

        // Expect IllegalArgumentException for null input
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            queryExecutionService.parseJsonToCondition(null);
        });

        // Validate error handling
        logger.info("Expected exception caught: {}", exception.getMessage());
        assertTrue(exception.getMessage().contains("JSON cannot be null"),
                  "Exception message should mention null JSON");

        logger.info("✓ Null JSON Input test completed successfully");
    }

    /**
     * Test Case 7: Error Handling - Empty Input
     * Tests error handling when JSON input is empty
     */
    @Test
    @Order(7)
    @DisplayName("parseJsonToCondition_givenEmptyInput_shouldThrowIllegalArgumentException")
    void parseJsonToCondition_givenEmptyInput_shouldThrowIllegalArgumentException() {
        logger.info("=== Test Case 7: Empty JSON Input ===");

        // Expect IllegalArgumentException for empty input
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            queryExecutionService.parseJsonToCondition("");
        });

        // Validate error handling
        logger.info("Expected exception caught: {}", exception.getMessage());
        assertTrue(exception.getMessage().contains("JSON cannot be null or empty"),
                  "Exception message should mention empty JSON");

        logger.info("✓ Empty JSON Input test completed successfully");
    }

    /**
     * Test Case 8: Complete Pipeline Validation
     * Comprehensive test that validates every component in the pipeline
     */
    @Test
    @Order(8)
    @DisplayName("parseJsonToCondition_givenCompleteFlow_shouldValidateEntirePipeline")
    void parseJsonToCondition_givenCompleteFlow_shouldValidateEntirePipeline() throws Exception {
        logger.info("=== Test Case 8: Complete Pipeline Validation ===");

        String jsonInput = """
        {
          "type": "StringQuery",
          "column": "status",
          "operator": "equals",
          "value": "PREMIUM_USER"
        }
        """;

        logger.info("Testing complete pipeline with input: {}", jsonInput);

        try {
            // Step 1: Validate OperatorScanner registration
            logger.info("Step 1: Validating OperatorScanner registration...");
            assertTrue(operatorRegistry.getAllOperatorNames().contains("equals"),
                      "OperatorRegistry should contain 'equals' operator");
            logger.info("✓ OperatorScanner successfully registered {} operators",
                       operatorRegistry.getAllOperatorNames().size());

            // Step 2: Validate OperatorFactory resolution
            logger.info("Step 2: Validating OperatorFactory resolution...");
            var equalsOperator = operatorFactory.resolve("equals", String.class);
            assertNotNull(equalsOperator, "OperatorFactory should resolve 'equals' operator for String");
            logger.info("✓ OperatorFactory resolved: {}", equalsOperator.getClass().getSimpleName());

            // Step 3: Validate JSON parsing and processing through service
            logger.info("Step 3: Validating complete JSON to Condition processing...");
            Condition condition = queryExecutionService.parseJsonToCondition(jsonInput);
            assertNotNull(condition, "QueryExecutionService should generate condition");
            logger.info("✓ QueryExecutionService processed successfully");

            // Step 4: Validate JOOQ Condition output
            logger.info("Step 4: Validating JOOQ Condition output...");
            String generatedSQL = condition.toString();
            assertFalse(generatedSQL.isEmpty(), "Generated SQL should not be empty");
            assertTrue(generatedSQL.contains("status"), "SQL should contain field name");
            assertTrue(generatedSQL.contains("PREMIUM_USER"), "SQL should contain value");
            logger.info("✓ JOOQ Condition generated: {}", generatedSQL);

            // Step 5: Validate direct Query object processing
            logger.info("Step 5: Validating direct Query object processing...");
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            Query query = mapper.readValue(jsonInput, Query.class);
            assertNotNull(query, "Query object should be created from JSON");

            Condition directCondition = query.toCondition(dsl, operatorFactory);
            assertNotNull(directCondition, "Query should generate condition directly");
            logger.info("✓ Direct Query processing working");

            logger.info("=== Complete Pipeline Validation Summary ===");
            logger.info("✓ All pipeline components validated successfully:");
            logger.info("  - OperatorScanner: {} operators registered", operatorRegistry.getAllOperatorNames().size());
            logger.info("  - OperatorRegistry: {} total registrations", operatorRegistry.getTotalOperatorCount());
            logger.info("  - OperatorFactory: Operator resolution working");
            logger.info("  - JSON Processing: Parsing and Query creation working");
            logger.info("  - QueryExecutionService: End-to-end processing working");
            logger.info("  - JOOQ Integration: Condition generation working");
            logger.info("  - Final SQL: {}", generatedSQL);

            logger.info("🎉 Complete Pipeline Validation test completed successfully! 🎉");
        } catch (Exception e) {
            logger.error("Pipeline validation failed", e);
            throw e;
        }
    }

    @AfterEach
    void tearDown() {
        logger.info("Test cleanup completed");
    }

    @AfterAll
    static void tearDownAll() {
        logger.info("=== All Integration Tests Completed ===");
    }
}
