package com.example.qe.queryengine.replaceable;

import com.example.qe.queryengine.query.QueryContextDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ReplaceableRegistryTest {

    private ReplaceableRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new ReplaceableRegistry();
    }

    // --- Test data class ---
    static class DummyReplaceable implements Replaceable {
        @Override
        public String resolve(QueryContextDto context) {
            return "dummy";
        }
    }

    // ---------- Tests ----------

    @Test
    void register_givenNewPlaceholder_shouldStoreClass() {
        // Arrange
        String placeholder = "[TEST]";
        Class<? extends Replaceable> clazz = DummyReplaceable.class;

        // Act
        registry.register(placeholder, clazz);

        // Assert
        assertThat(registry.get(placeholder)).isEqualTo(clazz);
        assertThat(registry.contains(placeholder)).isTrue();
    }

    @Test
    void register_givenDuplicatePlaceholder_shouldOverwritePrevious() {
        // Arrange
        String placeholder = "[DUPLICATE]";
        registry.register(placeholder, DummyReplaceable.class);

        class AnotherReplaceable implements Replaceable {
            @Override
            public String resolve(QueryContextDto context) {
                return "another";
            }
        }

        // Act
        registry.register(placeholder, AnotherReplaceable.class);

        // Assert
        assertThat(registry.get(placeholder)).isEqualTo(AnotherReplaceable.class);
        assertThat(registry.getAll()).hasSize(1);
    }

    @Test
    void get_givenNonExistentPlaceholder_shouldReturnNull() {
        // Act
        Class<? extends Replaceable> result = registry.get("[MISSING]");

        // Assert
        assertThat(result).isNull();
    }

    @Test
    void contains_givenNonExistentPlaceholder_shouldReturnFalse() {
        // Act
        boolean result = registry.contains("[MISSING]");

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    void getAll_givenMultipleRegistrations_shouldReturnUnmodifiableViewOfAllEntries() {
        // Arrange
        registry.register("[ONE]", DummyReplaceable.class);

        class AnotherReplaceable implements Replaceable {
            @Override
            public String resolve(com.example.qe.queryengine.query.QueryContextDto context) {
                return "another";
            }
        }
        registry.register("[TWO]", AnotherReplaceable.class);

        // Act
        Map<String, Class<? extends Replaceable>> all = registry.getAll();

        // Assert
        assertThat(all)
                .hasSize(2)
                .containsEntry("[ONE]", DummyReplaceable.class)
                .containsEntry("[TWO]", AnotherReplaceable.class);
    }
}
