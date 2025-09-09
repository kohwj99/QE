package com.example.qe.queryengine.replaceable;

import com.example.qe.queryengine.exception.QueryReplaceableException;
import com.example.qe.queryengine.query.QueryContextDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ReplaceableResolverTest {

    private ReplaceableFactory factory;
    private ReplaceableResolver resolver;

    @BeforeEach
    void setUp() {
        factory = mock(ReplaceableFactory.class);
        resolver = new ReplaceableResolver(factory);
    }

    static class DummyReplaceable implements Replaceable {
        private final String value;
        DummyReplaceable(String value) {
            this.value = value;
        }
        @Override
        public String resolve(QueryContextDto context) {
            return value;
        }
    }

    private QueryContextDto makeContext(String json) throws Exception {
        QueryContextDto context = new QueryContextDto();
        context.setJson(new ObjectMapper().readTree(json));
        return context;
    }

    @Test
    void processJsonPlaceholders_givenSimpleQueryPlaceholder_shouldResolve() throws Exception {
        // Arrange
        String json = """
                {
                  "type": "StringQuery",
                  "column": "employee_id",
                  "operator": "equals",
                  "value": "[me]",
                  "valueType": "STRING"
                }
                """;
        QueryContextDto context = makeContext(json);
        when(factory.create(eq("[me]"))).thenReturn(new DummyReplaceable("resolved-me"));

        // Act
        String result = resolver.processJsonPlaceholders(context);

        // Assert
        assertThat(result).contains("\"value\":\"resolved-me\"");
        verify(factory).create("[me]");
    }

    @Test
    void processJsonPlaceholders_givenNestedAndQueryPlaceholders_shouldResolveAll() throws Exception {
        // Arrange
        String json = """
                {
                  "type": "AndQuery",
                  "children": [
                    {
                      "type": "DateQuery",
                      "column": "birthday",
                      "operator": "equals",
                      "value": "[today]",
                      "valueType": "DATE"
                    },
                    {
                      "type": "StringQuery",
                      "column": "employee_id",
                      "operator": "equals",
                      "value": "[me]",
                      "valueType": "STRING"
                    }
                  ]
                }
                """;
        QueryContextDto context = makeContext(json);

        when(factory.create(eq("[today]"))).thenReturn(new DummyReplaceable("2025-09-09"));
        when(factory.create(eq("[me]"))).thenReturn(new DummyReplaceable("user-12345"));

        // Act
        String result = resolver.processJsonPlaceholders(context);

        // Assert
        assertThat(result)
                .contains("\"value\":\"2025-09-09\"")
                .contains("\"value\":\"user-12345\"");
        verify(factory).create("[today]");
        verify(factory).create("[me]");
    }

    @Test
    void processJsonPlaceholders_givenFactoryThrows_shouldWrapInQueryReplaceableException() throws Exception {
        // Arrange
        String json = """
                {
                  "type": "StringQuery",
                  "column": "employee_id",
                  "operator": "equals",
                  "value": "[bad]",
                  "valueType": "STRING"
                }
                """;
        QueryContextDto context = makeContext(json);
        when(factory.create(eq("[bad]"))).thenThrow(new RuntimeException("boom"));

        // Act & Assert
        assertThatThrownBy(() -> resolver.processJsonPlaceholders(context))
                .isInstanceOf(QueryReplaceableException.class)
                .hasMessageContaining("Failed to process placeholders")
                .hasCauseInstanceOf(RuntimeException.class);
    }

    @Test
    void processJsonPlaceholders_givenNoPlaceholders_shouldReturnOriginalJson() throws Exception {
        // Arrange
        String json = """
                {
                  "type": "StringQuery",
                  "column": "department",
                  "operator": "equals",
                  "value": "Engineering",
                  "valueType": "STRING"
                }
                """;
        QueryContextDto context = makeContext(json);

        // Act
        String result = resolver.processJsonPlaceholders(context);

        // Assert
        assertThat(result).isEqualToIgnoringWhitespace(json);
        verifyNoInteractions(factory);
    }
}
