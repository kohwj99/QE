package com.example.qe.queryengine.replaceable;

import com.example.qe.queryengine.exception.QueryReplaceableException;
import com.example.qe.queryengine.query.QueryContextDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReplaceableFactoryTest {

    private ReplaceableRegistry registry;
    private ReplaceableFactory factory;

    @BeforeEach
    void setUp() {
        registry = new ReplaceableRegistry();
        factory = new ReplaceableFactory(registry);
    }

    // --- Test data classes ---
    static class DummyReplaceable implements Replaceable {
        @Override
        public String resolve(QueryContextDto context) {
            return "dummy";
        }
    }

    static class FailingReplaceable implements Replaceable {
        private FailingReplaceable() {
            throw new RuntimeException("Cannot instantiate");
        }

        @Override
        public String resolve(QueryContextDto context) {
            return "fail";
        }
    }

    // ---------- Tests ----------

    @Test
    void create_givenValidRegisteredPlaceholder_shouldReturnNewInstance() {
        // Arrange
        registry.register("[TEST]", DummyReplaceable.class);

        // Act
        Replaceable result = factory.create("[TEST]");

        // Assert
        assertThat(result).isInstanceOf(DummyReplaceable.class);
    }

    @Test
    void create_givenUnregisteredPlaceholder_shouldThrowQueryReplaceableException() {
        // Act & Assert
        assertThatThrownBy(() -> factory.create("[MISSING]"))
                .isInstanceOf(QueryReplaceableException.class)
                .hasMessageContaining("No Replaceable found for placeholder: [MISSING]");
    }

    @Test
    void create_givenMultipleCalls_shouldReturnDifferentInstances() {
        // Arrange
        registry.register("[TEST]", DummyReplaceable.class);

        // Act
        Replaceable first = factory.create("[TEST]");
        Replaceable second = factory.create("[TEST]");

        // Assert
        assertThat(first).isNotSameAs(second); // factory should create a new instance each time
    }
}
