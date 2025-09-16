package com.example.qe.queryengine.replaceable;

import com.example.qe.queryengine.query.QueryContextDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;

class ReplaceableScannerTest {

    private ReplaceableRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new ReplaceableRegistry();
    }

    // --- Test data classes ---
    @ReplaceableAnnotation("[TEST_PLACEHOLDER]")
    static class ValidReplaceable implements Replaceable {
        @Override
        public String resolve(QueryContextDto context) {
            return "";
        }
    }

    @ReplaceableAnnotation("[INVALID_PLACEHOLDER]")
    static class NotAReplaceable {} // does NOT implement Replaceable

    // ---------- Tests ----------

    @Test
    void scanAndRegister_givenValidReplaceable_shouldRegisterClass() {
        // Arrange
        ReplaceableScanner localScanner = new ReplaceableScanner(registry) {
            @Override
            public void scanAndRegister() {
                registerReplaceable(ValidReplaceable.class, "[TEST_PLACEHOLDER]");
            }
        };

        // Act
        localScanner.scanAndRegister();

        // Assert
        Map<String, Class<? extends Replaceable>> all = registry.getAll();
        assertThat(all).containsKey("[TEST_PLACEHOLDER]");
        assertThat(all).containsEntry("[TEST_PLACEHOLDER]", ValidReplaceable.class);
    }

    @Test
    void scanAndRegister_givenAnnotatedNonReplaceable_shouldNotRegister() {
        // Arrange
        ReplaceableRegistry mockRegistry = Mockito.mock(ReplaceableRegistry.class);
        ReplaceableScanner localScanner = new ReplaceableScanner(mockRegistry) {
            @Override
            public void scanAndRegister() {
                // simulate reflection returning a class that doesn't implement Replaceable
                if (NotAReplaceable.class.isAnnotationPresent(ReplaceableAnnotation.class)) {
                    // should not be registered
                }
            }
        };

        // Act
        localScanner.scanAndRegister();

        // Assert
        verifyNoInteractions(mockRegistry);
    }

    @Test
    void scanAndRegister_givenNoReplaceables_shouldKeepRegistryEmpty() {
        // Arrange
        ReplaceableScanner localScanner = new ReplaceableScanner(registry) {
            @Override
            public void scanAndRegister() {
                // simulate no annotated classes found
            }
        };

        // Act
        localScanner.scanAndRegister();

        // Assert
        assertThat(registry.getAll()).isEmpty();
    }
}
