package com.example.qe.util;

import com.example.qe.model.operator.GenericOperator;
import com.example.qe.model.operator.impl.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Operator Component Scanning Tests")
class OperatorScannerTest {

    private static final Logger logger = LoggerFactory.getLogger(OperatorScannerTest.class);

    private OperatorRegistry registry;
    private OperatorScanner scanner;

    @BeforeEach
    void setUp() {
        logger.info("=== Setting up test environment ===");
        registry = new OperatorRegistry();
        scanner = new OperatorScanner(registry);
        logger.info("Created new OperatorRegistry and OperatorScanner instances");
    }

    @Test
    @DisplayName("Should scan and register all annotated operators in the package")
    void testScanAndRegisterOperators() {
        logger.info("=== Testing operator scanning and registration ===");

        // Act: Scan the operator package
        scanner.scanAndRegister("com.example.qe.model.operator");

        // Assert: Verify operators were found and registered
        Set<String> operatorNames = registry.getAllOperatorNames();
        logger.info("Found operator names: {}", operatorNames);

        // Expected active operators (non-commented ones)
        List<String> expectedOperators = Arrays.asList(
            "equals", "notEquals", "greaterThan", "lessThan",
            "greaterThanEqual", "lessThanEqual",
            "like", "startsWith", "endsWith", "isNull", "isNotNull",
            "daysBefore", "monthsBefore", "yearsBefore",
            "dayEqual", "monthEqual", "yearEqual",
            "daysAfter", "monthsAfter", "yearsAfter",
            "dayOfMonth"
        );

        for (String expectedOperator : expectedOperators) {
            assertTrue(operatorNames.contains(expectedOperator),
                      "Should contain '" + expectedOperator + "' operator");
            logger.info("✓ Found expected operator: {}", expectedOperator);
        }

        int totalOperators = registry.getTotalOperatorCount();
        logger.info("Total operators registered: {}", totalOperators);
        assertTrue(totalOperators > 0, "Should have registered at least some operators");

        // Log detailed registration info
        for (String operatorName : operatorNames) {
            Map<Class<?>, GenericOperator<?>> operatorsByType = registry.getOperatorsForName(operatorName);
            logger.info("Operator '{}' registered for {} types:", operatorName, operatorsByType.size());
            for (Class<?> type : operatorsByType.keySet()) {
                logger.info("  - {}: {}", type.getSimpleName(), operatorsByType.get(type).getClass().getSimpleName());
            }
        }

        // Verify minimum expected registrations
        assertTrue(totalOperators >= 15,
                  "Should have at least 15 type-specific registrations based on operator definitions");
    }

    @Test
    @DisplayName("Should correctly register all operators with their expected types")
    void testAllOperatorTypeMappings() {
        logger.info("=== Testing all operator type mappings ===");

        // Act
        scanner.scanAndRegister("com.example.qe.model.operator");

        // Test EqualsOperator
        logger.info("Testing EqualsOperator registrations...");
        Class<?>[] equalsTypes = {String.class, BigDecimal.class, Boolean.class, LocalDate.class};
        for (Class<?> type : equalsTypes) {
            GenericOperator<?> operator = registry.get("equals", type);
            assertNotNull(operator, "Should find equals operator for " + type.getSimpleName());
            assertInstanceOf(EqualsOperator.class, operator);
            logger.info("✓ EqualsOperator registered for: {}", type.getSimpleName());
        }

        // Test NotEqualsOperator
        logger.info("Testing NotEqualsOperator registrations...");
        Class<?>[] notEqualsTypes = {String.class, BigDecimal.class, Boolean.class, LocalDate.class};
        for (Class<?> type : notEqualsTypes) {
            GenericOperator<?> operator = registry.get("notEquals", type);
            assertNotNull(operator, "Should find notEquals operator for " + type.getSimpleName());
            assertInstanceOf(NotEqualsOperator.class, operator);
            logger.info("✓ NotEqualsOperator registered for: {}", type.getSimpleName());
        }

        // Test GreaterThanOperator
        logger.info("Testing GreaterThanOperator registrations...");
        Class<?>[] greaterThanTypes = {BigDecimal.class, LocalDate.class};
        for (Class<?> type : greaterThanTypes) {
            GenericOperator<?> operator = registry.get("greaterThan", type);
            assertNotNull(operator, "Should find greaterThan operator for " + type.getSimpleName());
            assertInstanceOf(GreaterThanOperator.class, operator);
            logger.info("✓ GreaterThanOperator registered for: {}", type.getSimpleName());
        }

        // Test LessThanOperator
        logger.info("Testing LessThanOperator registrations...");
        Class<?>[] lessThanTypes = {BigDecimal.class, LocalDate.class};
        for (Class<?> type : lessThanTypes) {
            GenericOperator<?> operator = registry.get("lessThan", type);
            assertNotNull(operator, "Should find lessThan operator for " + type.getSimpleName());
            assertInstanceOf(LessThanOperator.class, operator);
            logger.info("✓ LessThanOperator registered for: {}", type.getSimpleName());
        }

        // Test GreaterThanEqualOperator
        logger.info("Testing GreaterThanEqualOperator registrations...");
        Class<?>[] greaterThanEqualTypes = {BigDecimal.class, LocalDate.class};
        for (Class<?> type : greaterThanEqualTypes) {
            GenericOperator<?> operator = registry.get("greaterThanEqual", type);
            assertNotNull(operator, "Should find greaterThanEqual operator for " + type.getSimpleName());
            assertInstanceOf(GreaterThanEqualOperator.class, operator);
            logger.info("✓ GreaterThanEqualOperator registered for: {}", type.getSimpleName());
        }

        // Test LessThanEqualOperator
        logger.info("Testing LessThanEqualOperator registrations...");
        Class<?>[] lessThanEqualTypes = {BigDecimal.class, LocalDate.class};
        for (Class<?> type : lessThanEqualTypes) {
            GenericOperator<?> operator = registry.get("lessThanEqual", type);
            assertNotNull(operator, "Should find lessThanEqual operator for " + type.getSimpleName());
            assertInstanceOf(LessThanEqualOperator.class, operator);
            logger.info("✓ LessThanEqualOperator registered for: {}", type.getSimpleName());
        }

        // Test LikeOperator
        logger.info("Testing LikeOperator registrations...");
        GenericOperator<String> likeOperator = registry.get("like", String.class);
        assertNotNull(likeOperator, "Should find like operator for String");
        assertInstanceOf(LikeOperator.class, likeOperator);
        logger.info("✓ LikeOperator registered for: String");

        // Test StartsWithOperator
        logger.info("Testing StartsWithOperator registrations...");
        GenericOperator<String> startsWithOperator = registry.get("startsWith", String.class);
        assertNotNull(startsWithOperator, "Should find startsWith operator for String");
        assertInstanceOf(StartsWithOperator.class, startsWithOperator);
        logger.info("✓ StartsWithOperator registered for: String");

        // Test EndsWithOperator
        logger.info("Testing EndsWithOperator registrations...");
        GenericOperator<String> endsWithOperator = registry.get("endsWith", String.class);
        assertNotNull(endsWithOperator, "Should find endsWith operator for String");
        assertInstanceOf(EndsWithOperator.class, endsWithOperator);
        logger.info("✓ EndsWithOperator registered for: String");

        // Test Date Operators - Before operators
        logger.info("Testing Date Before Operators registrations...");
        String[] beforeOperators = {"daysBefore", "monthsBefore", "yearsBefore"};
        for (String operatorName : beforeOperators) {
            GenericOperator<?> operator = registry.get(operatorName, LocalDate.class);
            assertNotNull(operator, "Should find " + operatorName + " operator for LocalDate");
            logger.info("✓ {} registered for: LocalDate", operatorName);
        }

        // Test Date Operators - Equal operators
        String[] equalOperators = {"dayEqual", "monthEqual", "yearEqual", "dayOfMonth"};
        for (String operatorName : equalOperators) {
            GenericOperator<?> operator = registry.get(operatorName, LocalDate.class);
            assertNotNull(operator, "Should find " + operatorName + " operator for LocalDate");
            logger.info("✓ {} registered for: LocalDate", operatorName);
        }

        // Test Date Operators - After operators
        logger.info("Testing Date After Operators registrations...");
        String[] afterOperators = {"daysAfter", "monthsAfter", "yearsAfter"};
        for (String operatorName : afterOperators) {
            GenericOperator<?> operator = registry.get(operatorName, LocalDate.class);
            assertNotNull(operator, "Should find " + operatorName + " operator for LocalDate");
            logger.info("✓ {} registered for: LocalDate", operatorName);
        }

        // Test IsNullOperator
        logger.info("Testing IsNullOperator registrations...");
        GenericOperator<Object> isNullOperator = registry.get("isNull", Object.class);
        assertNotNull(isNullOperator, "Should find isNull operator for Object");
        assertInstanceOf(IsNullOperator.class, isNullOperator);
        logger.info("✓ IsNullOperator registered for: Object");

        // Test IsNotNullOperator
        logger.info("Testing IsNotNullOperator registrations...");
        GenericOperator<Object> isNotNullOperator = registry.get("isNotNull", Object.class);
        assertNotNull(isNotNullOperator, "Should find isNotNull operator for Object");
        assertInstanceOf(IsNotNullOperator.class, isNotNullOperator);
        logger.info("✓ IsNotNullOperator registered for: Object");

        logger.info("All operator type mappings verified successfully!");
    }

    @Test
    @DisplayName("Should return null for non-existent operators")
    void testNonExistentOperators() {
        logger.info("=== Testing retrieval of non-existent operators ===");

        // Act
        scanner.scanAndRegister("com.example.qe.model.operator");

        // Assert: Test non-existent operator
        GenericOperator<String> nonExistent = registry.get("nonExistentOperator", String.class);
        assertNull(nonExistent, "Should return null for non-existent operator");
        logger.info("✓ Correctly returned null for non-existent operator");

        // Assert: Test existing operator with unsupported type
        GenericOperator<Double> equalsForDouble = registry.get("equals", Double.class);
        assertNull(equalsForDouble, "Should return null for unsupported type");
        logger.info("✓ Correctly returned null for unsupported type (Double) with equals operator");

        // Test commented-out operators that should not be registered
        GenericOperator<?> inOperator = registry.get("in", Object.class);
        assertNull(inOperator, "Should return null for commented-out 'in' operator");
        logger.info("✓ Correctly excluded commented-out 'in' operator");

        GenericOperator<?> daysBeforeOperator = registry.get("daysBefore", Integer.class);
        assertNull(daysBeforeOperator, "Should return null for commented-out 'daysBefore' operator");
        logger.info("✓ Correctly excluded commented-out 'daysBefore' operator");
    }

    @Test
    @DisplayName("Should handle empty package scanning gracefully")
    void testEmptyPackageScanning() {
        logger.info("=== Testing scanning of empty package ===");

        // Act: Scan a package with no operators
        scanner.scanAndRegister("com.example.qe.controller"); // Empty package

        // Assert
        Set<String> operatorNames = registry.getAllOperatorNames();
        assertTrue(operatorNames.isEmpty(), "Should have no operators from empty package");
        assertEquals(0, registry.getTotalOperatorCount(), "Total operator count should be 0");
        logger.info("✓ Empty package scanning handled correctly - found {} operators", operatorNames.size());
    }

    @Test
    @DisplayName("Should demonstrate complete operator functionality with comprehensive validation")
    void testCompleteOperatorFunctionality() {
        logger.info("=== Demonstrating complete operator functionality ===");

        // Step 1: Scan and register
        logger.info("Step 1: Scanning for operators...");
        scanner.scanAndRegister("com.example.qe.model.operator");

        // Step 2: Validate expected operators are present
        Set<String> operatorNames = registry.getAllOperatorNames();
        logger.info("Step 2: Found {} unique operator names: {}", operatorNames.size(), operatorNames);

        List<String> expectedOperators = Arrays.asList(
            "equals", "notEquals", "greaterThan", "lessThan",
            "greaterThanEqual", "lessThanEqual",
            "like", "startsWith", "endsWith", "isNull", "isNotNull",
            "daysBefore", "monthsBefore", "yearsBefore",
            "dayEqual", "monthEqual", "yearEqual",
            "daysAfter", "monthsAfter", "yearsAfter",
            "dayOfMonth"
        );

        for (String expectedOperator : expectedOperators) {
            assertTrue(operatorNames.contains(expectedOperator),
                      "Missing expected operator: " + expectedOperator);
        }

        // Step 3: Test retrieval and demonstrate functionality
        logger.info("Step 3: Testing operator retrieval and functionality...");

        int successfulRetrievals = 0;
        for (String operatorName : operatorNames) {
            logger.info("Testing operator: '{}'", operatorName);
            Map<Class<?>, GenericOperator<?>> operatorsByType = registry.getOperatorsForName(operatorName);

            for (Class<?> type : operatorsByType.keySet()) {
                GenericOperator<?> operator = registry.get(operatorName, type);
                assertNotNull(operator, "Operator should be retrievable");
                successfulRetrievals++;
                logger.info("  ✓ Successfully retrieved '{}' operator for type: {} (Implementation: {})",
                           operatorName, type.getSimpleName(), operator.getClass().getSimpleName());
            }
        }

        // Step 4: Comprehensive validation
        int totalRegistrations = registry.getTotalOperatorCount();
        logger.info("=== COMPREHENSIVE VALIDATION SUMMARY ===");
        logger.info("Expected operators found: {}/{}", expectedOperators.size(), operatorNames.size());
        logger.info("Total unique operators: {}", operatorNames.size());
        logger.info("Total type-specific registrations: {}", totalRegistrations);
        logger.info("Successful operator retrievals: {}", successfulRetrievals);
        logger.info("Component scanning test completed successfully!");

        // Final comprehensive assertions
        assertEquals(expectedOperators.size(), operatorNames.size(),
                    "Should have exactly the expected number of operators");
        assertTrue(totalRegistrations >= 15,
                  "Should have at least 15 registrations based on operator type definitions");
        assertEquals(totalRegistrations, successfulRetrievals,
                    "All registered operators should be retrievable");

        logger.info("🎉 ALL OPERATOR REGISTRATIONS VERIFIED SUCCESSFULLY! 🎉");
    }
}
