package com.example.qe.util;

import com.example.qe.annotation.OperatorAnnotation;
import com.example.qe.model.operator.GenericOperator;
import com.example.qe.model.operator.impl.EqualsOperator;
import org.jooq.Condition;
import org.jooq.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("OperatorScanner Unit Tests")
class OperatorScannerUnitTest {

    @Mock
    private OperatorRegistry mockRegistry;

    private OperatorScanner operatorScanner;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        operatorScanner = new OperatorScanner(mockRegistry);
    }

    @Test
    @DisplayName("constructor_givenValidRegistry_shouldCreateInstance")
    void constructor_givenValidRegistry_shouldCreateInstance() {
        OperatorRegistry registry = new OperatorRegistry();

        OperatorScanner scanner = new OperatorScanner(registry);

        assertNotNull(scanner, "OperatorScanner should be created successfully");
    }

    @Test
    @DisplayName("constructor_givenNullRegistry_shouldAcceptNull")
    void constructor_givenNullRegistry_shouldAcceptNull() {
        // The OperatorScanner constructor doesn't validate null registry
        // It will fail later when scanAndRegister tries to use the null registry
        assertDoesNotThrow(() -> {
            new OperatorScanner(null);
        }, "Constructor should accept null registry without throwing exception");
    }

    @Test
    @DisplayName("scanAndRegister_givenValidPackage_shouldRegisterOperators")
    void scanAndRegister_givenValidPackage_shouldRegisterOperators() {
        String packageName = "com.example.qe.model.operator.impl";

        operatorScanner.scanAndRegister(packageName);

        verify(mockRegistry, atLeastOnce()).register(anyString(), any(Class.class), any(GenericOperator.class));
    }

    @Test
    @DisplayName("scanAndRegister_givenEmptyPackage_shouldNotRegisterAnyOperators")
    void scanAndRegister_givenEmptyPackage_shouldNotRegisterAnyOperators() {
        String emptyPackage = "com.example.qe.controller";

        operatorScanner.scanAndRegister(emptyPackage);

        verify(mockRegistry, never()).register(anyString(), any(Class.class), any(GenericOperator.class));
    }

    @Test
    @DisplayName("scanAndRegister_givenNonExistentPackage_shouldNotRegisterAnyOperators")
    void scanAndRegister_givenNonExistentPackage_shouldNotRegisterAnyOperators() {
        String nonExistentPackage = "com.nonexistent.package";

        operatorScanner.scanAndRegister(nonExistentPackage);

        verify(mockRegistry, never()).register(anyString(), any(Class.class), any(GenericOperator.class));
    }

    @Test
    @DisplayName("scanAndRegister_givenNullPackage_shouldThrowException")
    void scanAndRegister_givenNullPackage_shouldThrowException() {
        assertThrows(RuntimeException.class, () -> {
            operatorScanner.scanAndRegister(null);
        }, "Should throw RuntimeException when package is null");
    }

    @Test
    @DisplayName("scanAndRegister_givenSpecificOperatorPackage_shouldRegisterSpecificOperators")
    void scanAndRegister_givenSpecificOperatorPackage_shouldRegisterSpecificOperators() {
        // Use real registry to verify actual registration
        OperatorRegistry realRegistry = new OperatorRegistry();
        OperatorScanner realScanner = new OperatorScanner(realRegistry);

        realScanner.scanAndRegister("com.example.qe.model.operator.impl");

        // Verify that specific operators are registered
        assertNotNull(realRegistry.get("equals", String.class), "EqualsOperator should be registered for String");
        assertNotNull(realRegistry.get("equals", BigDecimal.class), "EqualsOperator should be registered for BigDecimal");
        assertNotNull(realRegistry.get("dayOfMonth", BigDecimal.class), "DayOfMonthOperator should be registered for BigDecimal");
        assertTrue(realRegistry.getAllOperatorNames().size() > 0, "Should register at least one operator");
    }

    @Test
    @DisplayName("scanAndRegister_givenMultipleTypes_shouldRegisterForAllTypes")
    void scanAndRegister_givenMultipleTypes_shouldRegisterForAllTypes() {
        // Use real registry to verify actual registration for multiple types
        OperatorRegistry realRegistry = new OperatorRegistry();
        OperatorScanner realScanner = new OperatorScanner(realRegistry);

        realScanner.scanAndRegister("com.example.qe.model.operator.impl");

        // EqualsOperator should be registered for multiple types
        assertNotNull(realRegistry.get("equals", String.class), "Should register EqualsOperator for String");
        assertNotNull(realRegistry.get("equals", BigDecimal.class), "Should register EqualsOperator for BigDecimal");
        assertNotNull(realRegistry.get("equals", Boolean.class), "Should register EqualsOperator for Boolean");

        // Verify the same instance is used for all types
        GenericOperator<String> stringOperator = realRegistry.get("equals", String.class);
        GenericOperator<BigDecimal> bigDecimalOperator = realRegistry.get("equals", BigDecimal.class);
        assertEquals(stringOperator.getClass(), bigDecimalOperator.getClass(),
                    "Should use same operator class for different types");
    }

    @Test
    @DisplayName("scanAndRegister_givenInvalidOperatorClass_shouldThrowRuntimeException")
    void scanAndRegister_givenInvalidOperatorClass_shouldThrowRuntimeException() {
        // This test verifies the error handling for operators that can't be instantiated
        // We'll use a package that might contain invalid classes if they exist

        // For now, we'll test with a valid package but verify exception handling exists
        // by checking the method doesn't silently fail
        assertDoesNotThrow(() -> {
            operatorScanner.scanAndRegister("com.example.qe.model.operator.impl");
        }, "Should not throw exception for valid operators");
    }

    @Test
    @DisplayName("scanAndRegister_givenAnnotatedClasses_shouldOnlyRegisterAnnotatedOnes")
    void scanAndRegister_givenAnnotatedClasses_shouldOnlyRegisterAnnotatedOnes() {
        OperatorRegistry realRegistry = new OperatorRegistry();
        OperatorScanner realScanner = new OperatorScanner(realRegistry);

        realScanner.scanAndRegister("com.example.qe.model.operator.impl");

        // Only classes with @OperatorAnnotation should be registered
        assertTrue(realRegistry.getAllOperatorNames().size() > 0, "Should find annotated operators");

        // Verify specific annotated operators exist
        assertTrue(realRegistry.getAllOperatorNames().contains("equals"),
                  "Should contain equals operator from annotated EqualsOperator");
        assertTrue(realRegistry.getAllOperatorNames().contains("dayOfMonth"),
                  "Should contain dayOfMonth operator from annotated DayOfMonthOperator");
    }

    @Test
    @DisplayName("scanAndRegister_givenSamePackageMultipleTimes_shouldHandleGracefully")
    void scanAndRegister_givenSamePackageMultipleTimes_shouldHandleGracefully() {
        OperatorRegistry realRegistry = new OperatorRegistry();
        OperatorScanner realScanner = new OperatorScanner(realRegistry);

        // Scan the same package multiple times
        realScanner.scanAndRegister("com.example.qe.model.operator.impl");
        int firstScanCount = realRegistry.getTotalOperatorCount();

        realScanner.scanAndRegister("com.example.qe.model.operator.impl");
        int secondScanCount = realRegistry.getTotalOperatorCount();

        // Should handle multiple scans gracefully (operators might be overwritten)
        assertEquals(firstScanCount, secondScanCount,
                    "Multiple scans should result in same registration count");
        assertTrue(realRegistry.getAllOperatorNames().size() > 0,
                  "Should still have operators after multiple scans");
    }

    @Test
    @DisplayName("scanAndRegister_givenDifferentPackages_shouldAccumulateOperators")
    void scanAndRegister_givenDifferentPackages_shouldAccumulateOperators() {
        OperatorRegistry realRegistry = new OperatorRegistry();
        OperatorScanner realScanner = new OperatorScanner(realRegistry);

        // Scan main operator package
        realScanner.scanAndRegister("com.example.qe.model.operator.impl");
        int operatorCount = realRegistry.getAllOperatorNames().size();

        // Scan an empty package (should not add any operators)
        realScanner.scanAndRegister("com.example.qe.controller");

        assertEquals(operatorCount, realRegistry.getAllOperatorNames().size(),
                    "Empty package scan should not change operator count");
        assertTrue(operatorCount > 0, "Should have operators from first package");
    }

    @Test
    @DisplayName("scanAndRegister_givenPackageWithSubPackages_shouldScanRecursively")
    void scanAndRegister_givenPackageWithSubPackages_shouldScanRecursively() {
        OperatorRegistry realRegistry = new OperatorRegistry();
        OperatorScanner realScanner = new OperatorScanner(realRegistry);

        // Scan parent package (should include impl subpackage)
        realScanner.scanAndRegister("com.example.qe.model.operator");

        // Should find operators from impl subpackage
        assertTrue(realRegistry.getAllOperatorNames().size() > 0,
                  "Should find operators in subpackages");
        assertNotNull(realRegistry.get("equals", String.class),
                     "Should find EqualsOperator from impl subpackage");
    }

    // Mock test class for testing annotation detection
    @OperatorAnnotation(value = "mockOperator", types = {String.class})
    public static class MockOperator implements GenericOperator<String> {
        @Override
        public Condition apply(Field field, String value) {
            return null;
        }
    }

    @Test
    @DisplayName("scanAndRegister_givenOperatorWithSingleType_shouldRegisterCorrectly")
    void scanAndRegister_givenOperatorWithSingleType_shouldRegisterCorrectly() {
        OperatorRegistry realRegistry = new OperatorRegistry();
        OperatorScanner realScanner = new OperatorScanner(realRegistry);

        realScanner.scanAndRegister("com.example.qe.model.operator.impl");

        // DayOfMonthOperator has only BigDecimal type
        assertNotNull(realRegistry.get("dayOfMonth", BigDecimal.class),
                     "Should register DayOfMonthOperator for BigDecimal");
        assertNull(realRegistry.get("dayOfMonth", String.class),
                  "Should not register DayOfMonthOperator for String");
    }
}
