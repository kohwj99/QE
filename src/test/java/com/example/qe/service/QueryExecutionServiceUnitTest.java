package com.example.qe.service;

import com.example.qe.resolver.impl.BasicPlaceholderResolver;
import com.example.qe.util.OperatorFactory;
import com.example.qe.util.OperatorRegistry;
import com.example.qe.util.OperatorScanner;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.jooq.Condition;
import org.jooq.SQLDialect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("QueryExecutionService Unit Tests")
class QueryExecutionServiceUnitTest {

    private QueryExecutionService queryExecutionService;
    private OperatorFactory operatorFactory;
    private ConversionService conversionService;

    @Mock
    private ReplacementService mockReplacementService;

    @BeforeEach
    void setUp() {
        // Set up operator factory with real operators
        OperatorRegistry registry = new OperatorRegistry();
        OperatorScanner scanner = new OperatorScanner(registry);
        scanner.scanAndRegister("com.example.qe.model.operator");
        operatorFactory = new OperatorFactory(registry);

        // Set up conversion service
        conversionService = new DefaultConversionService();

        // Create service with mock replacement service
        queryExecutionService = new QueryExecutionService(
            operatorFactory,
            conversionService,
            "DEFAULT",
            mockReplacementService
        );
    }

    @Test
    @DisplayName("parseJsonToCondition_givenValidStringQuery_shouldReturnCondition")
    void parseJsonToCondition_givenValidStringQuery_shouldReturnCondition() throws JsonProcessingException {
        String json = """
        {
          "type": "StringQuery",
          "column": "name",
          "operator": "equals",
          "value": "John Doe"
        }
        """;

        when(mockReplacementService.processJsonPlaceholders(json)).thenReturn(json);

        Condition result = queryExecutionService.parseJsonToCondition(json);

        assertNotNull(result, "Result condition should not be null");
        verify(mockReplacementService).processJsonPlaceholders(json);
    }

    @Test
    @DisplayName("parseJsonToCondition_givenValidNumericQuery_shouldReturnCondition")
    void parseJsonToCondition_givenValidNumericQuery_shouldReturnCondition() throws JsonProcessingException {
        String json = """
        {
          "type": "NumericQuery",
          "column": "age",
          "operator": "greaterThan",
          "value": 18
        }
        """;

        when(mockReplacementService.processJsonPlaceholders(json)).thenReturn(json);

        Condition result = queryExecutionService.parseJsonToCondition(json);

        assertNotNull(result, "Result condition should not be null");
        verify(mockReplacementService).processJsonPlaceholders(json);
    }

    @Test
    @DisplayName("parseJsonToCondition_givenValidDateQuery_shouldReturnCondition")
    void parseJsonToCondition_givenValidDateQuery_shouldReturnCondition() throws JsonProcessingException {
        String json = """
        {
          "type": "DateQuery",
          "column": "created_date",
          "operator": "equals",
          "value": "2025-08-21"
        }
        """;

        when(mockReplacementService.processJsonPlaceholders(json)).thenReturn(json);

        Condition result = queryExecutionService.parseJsonToCondition(json);

        assertNotNull(result, "Result condition should not be null");
        verify(mockReplacementService).processJsonPlaceholders(json);
    }

    @Test
    @DisplayName("parseJsonToCondition_givenValidBoolQuery_shouldReturnCondition")
    void parseJsonToCondition_givenValidBoolQuery_shouldReturnCondition() throws JsonProcessingException {
        String json = """
        {
          "type": "BoolQuery",
          "column": "is_active",
          "operator": "equals",
          "value": true
        }
        """;

        when(mockReplacementService.processJsonPlaceholders(json)).thenReturn(json);

        Condition result = queryExecutionService.parseJsonToCondition(json);

        assertNotNull(result, "Result condition should not be null");
        verify(mockReplacementService).processJsonPlaceholders(json);
    }

    @Test
    @DisplayName("parseJsonToCondition_givenComplexAndQuery_shouldReturnCondition")
    void parseJsonToCondition_givenComplexAndQuery_shouldReturnCondition() throws JsonProcessingException {
        String json = """
        {
          "type": "AndQuery",
          "children": [
            {
              "type": "StringQuery",
              "column": "department",
              "operator": "equals",
              "value": "Engineering"
            },
            {
              "type": "NumericQuery",
              "column": "salary",
              "operator": "greaterThan",
              "value": 50000
            },
            {
              "type": "BoolQuery",
              "column": "is_active",
              "operator": "equals",
              "value": true
            }
          ]
        }
        """;

        when(mockReplacementService.processJsonPlaceholders(json)).thenReturn(json);

        Condition result = queryExecutionService.parseJsonToCondition(json);

        assertNotNull(result, "Result condition should not be null");
        verify(mockReplacementService).processJsonPlaceholders(json);
    }

    @Test
    @DisplayName("parseJsonToCondition_givenComplexOrQuery_shouldReturnCondition")
    void parseJsonToCondition_givenComplexOrQuery_shouldReturnCondition() throws JsonProcessingException {
        String json = """
        {
          "type": "OrQuery",
          "children": [
            {
              "type": "StringQuery",
              "column": "status",
              "operator": "equals",
              "value": "active"
            },
            {
              "type": "StringQuery",
              "column": "status",
              "operator": "equals",
              "value": "pending"
            }
          ]
        }
        """;

        when(mockReplacementService.processJsonPlaceholders(json)).thenReturn(json);

        Condition result = queryExecutionService.parseJsonToCondition(json);

        assertNotNull(result, "Result condition should not be null");
        verify(mockReplacementService).processJsonPlaceholders(json);
    }

    @Test
    @DisplayName("parseJsonToCondition_givenJsonWithPlaceholders_shouldProcessPlaceholdersFirst")
    void parseJsonToCondition_givenJsonWithPlaceholders_shouldProcessPlaceholdersFirst() throws JsonProcessingException {
        String originalJson = """
        {
          "type": "StringQuery",
          "column": "created_by",
          "operator": "equals",
          "value": "[me]"
        }
        """;

        String processedJson = """
        {
          "type": "StringQuery",
          "column": "created_by",
          "operator": "equals",
          "value": "current_user"
        }
        """;

        when(mockReplacementService.processJsonPlaceholders(originalJson)).thenReturn(processedJson);

        Condition result = queryExecutionService.parseJsonToCondition(originalJson);

        assertNotNull(result, "Result condition should not be null");
        verify(mockReplacementService).processJsonPlaceholders(originalJson);
    }

    @Test
    @DisplayName("parseJsonToCondition_givenJsonWithMultiplePlaceholders_shouldProcessAllPlaceholders")
    void parseJsonToCondition_givenJsonWithMultiplePlaceholders_shouldProcessAllPlaceholders() throws JsonProcessingException {
        String originalJson = """
        {
          "type": "AndQuery",
          "children": [
            {
              "type": "StringQuery",
              "column": "owner",
              "operator": "equals",
              "value": "[me]"
            },
            {
              "type": "DateQuery",
              "column": "created_date",
              "operator": "equals",
              "value": "[today]"
            }
          ]
        }
        """;

        String processedJson = """
        {
          "type": "AndQuery",
          "children": [
            {
              "type": "StringQuery",
              "column": "owner",
              "operator": "equals",
              "value": "current_user"
            },
            {
              "type": "DateQuery",
              "column": "created_date",
              "operator": "equals",
              "value": "2025-08-21"
            }
          ]
        }
        """;

        when(mockReplacementService.processJsonPlaceholders(originalJson)).thenReturn(processedJson);

        Condition result = queryExecutionService.parseJsonToCondition(originalJson);

        assertNotNull(result, "Result condition should not be null");
        verify(mockReplacementService).processJsonPlaceholders(originalJson);
    }

    @Test
    @DisplayName("parseJsonToCondition_givenNullJson_shouldThrowIllegalArgumentException")
    void parseJsonToCondition_givenNullJson_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            queryExecutionService.parseJsonToCondition(null);
        });

        assertEquals("JSON cannot be null or empty", exception.getMessage());
        verify(mockReplacementService, never()).processJsonPlaceholders(anyString());
    }

    @Test
    @DisplayName("parseJsonToCondition_givenEmptyJson_shouldThrowIllegalArgumentException")
    void parseJsonToCondition_givenEmptyJson_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            queryExecutionService.parseJsonToCondition("");
        });

        assertEquals("JSON cannot be null or empty", exception.getMessage());
        verify(mockReplacementService, never()).processJsonPlaceholders(anyString());
    }

    @Test
    @DisplayName("parseJsonToCondition_givenWhitespaceOnlyJson_shouldThrowIllegalArgumentException")
    void parseJsonToCondition_givenWhitespaceOnlyJson_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            queryExecutionService.parseJsonToCondition("   ");
        });

        assertEquals("JSON cannot be null or empty", exception.getMessage());
        verify(mockReplacementService, never()).processJsonPlaceholders(anyString());
    }

    @Test
    @DisplayName("parseJsonToCondition_givenMalformedJson_shouldThrowJsonProcessingException")
    void parseJsonToCondition_givenMalformedJson_shouldThrowJsonProcessingException() throws JsonProcessingException {
        String malformedJson = """
        {
          "type": "StringQuery",
          "column": "name",
          "operator": "equals"
          // missing value and closing brace
        """;

        when(mockReplacementService.processJsonPlaceholders(malformedJson)).thenReturn(malformedJson);

        assertThrows(JsonProcessingException.class, () -> {
            queryExecutionService.parseJsonToCondition(malformedJson);
        });

        verify(mockReplacementService).processJsonPlaceholders(malformedJson);
    }

    @Test
    @DisplayName("parseJsonToCondition_givenUnsupportedQueryType_shouldThrowException")
    void parseJsonToCondition_givenUnsupportedQueryType_shouldThrowException() throws JsonProcessingException {
        String unsupportedJson = """
        {
          "type": "UnsupportedQuery",
          "column": "name",
          "operator": "equals",
          "value": "test"
        }
        """;

        when(mockReplacementService.processJsonPlaceholders(unsupportedJson)).thenReturn(unsupportedJson);

        assertThrows(Exception.class, () -> {
            queryExecutionService.parseJsonToCondition(unsupportedJson);
        });

        verify(mockReplacementService).processJsonPlaceholders(unsupportedJson);
    }

    @Test
    @DisplayName("parseJsonToCondition_givenInvalidOperator_shouldThrowException")
    void parseJsonToCondition_givenInvalidOperator_shouldThrowException() throws JsonProcessingException {
        String invalidOperatorJson = """
        {
          "type": "StringQuery",
          "column": "name",
          "operator": "invalidOperator",
          "value": "test"
        }
        """;

        when(mockReplacementService.processJsonPlaceholders(invalidOperatorJson)).thenReturn(invalidOperatorJson);

        assertThrows(Exception.class, () -> {
            queryExecutionService.parseJsonToCondition(invalidOperatorJson);
        });

        verify(mockReplacementService).processJsonPlaceholders(invalidOperatorJson);
    }

    @Test
    @DisplayName("parseJsonToCondition_givenDeeplyNestedQuery_shouldReturnCondition")
    void parseJsonToCondition_givenDeeplyNestedQuery_shouldReturnCondition() throws JsonProcessingException {
        String deeplyNestedJson = """
        {
          "type": "AndQuery",
          "children": [
            {
              "type": "StringQuery",
              "column": "department",
              "operator": "equals",
              "value": "Engineering"
            },
            {
              "type": "OrQuery",
              "children": [
                {
                  "type": "AndQuery",
                  "children": [
                    {
                      "type": "NumericQuery",
                      "column": "experience",
                      "operator": "greaterThan",
                      "value": 5
                    },
                    {
                      "type": "BoolQuery",
                      "column": "is_senior",
                      "operator": "equals",
                      "value": true
                    }
                  ]
                },
                {
                  "type": "NumericQuery",
                  "column": "salary",
                  "operator": "greaterThan",
                  "value": 120000
                }
              ]
            }
          ]
        }
        """;

        when(mockReplacementService.processJsonPlaceholders(deeplyNestedJson)).thenReturn(deeplyNestedJson);

        Condition result = queryExecutionService.parseJsonToCondition(deeplyNestedJson);

        assertNotNull(result, "Result condition should not be null");
        verify(mockReplacementService).processJsonPlaceholders(deeplyNestedJson);
    }

    @Test
    @DisplayName("parseJsonToCondition_givenNumericOperators_shouldReturnCondition")
    void parseJsonToCondition_givenNumericOperators_shouldReturnCondition() throws JsonProcessingException {
        String[] operators = {"equals", "notEquals", "greaterThan", "lessThan", "greaterThanEqual", "lessThanEqual"};

        for (String operator : operators) {
            String json = String.format("""
            {
              "type": "NumericQuery",
              "column": "value",
              "operator": "%s",
              "value": 100
            }
            """, operator);

            when(mockReplacementService.processJsonPlaceholders(json)).thenReturn(json);

            assertDoesNotThrow(() -> {
                Condition result = queryExecutionService.parseJsonToCondition(json);
                assertNotNull(result, "Result should not be null for operator: " + operator);
            }, "Should not throw exception for operator: " + operator);
        }
    }

    @Test
    @DisplayName("parseJsonToCondition_givenStringOperators_shouldReturnCondition")
    void parseJsonToCondition_givenStringOperators_shouldReturnCondition() throws JsonProcessingException {
        String[] stringOperators = {"equals", "notEquals", "like", "startsWith", "endsWith"};

        for (String operator : stringOperators) {
            String json = String.format("""
            {
              "type": "StringQuery",
              "column": "name",
              "operator": "%s",
              "value": "test"
            }
            """, operator);

            when(mockReplacementService.processJsonPlaceholders(json)).thenReturn(json);

            assertDoesNotThrow(() -> {
                Condition result = queryExecutionService.parseJsonToCondition(json);
                assertNotNull(result, "Result should not be null for string operator: " + operator);
            }, "Should not throw exception for string operator: " + operator);
        }
    }

    @Test
    @DisplayName("parseJsonToCondition_givenNullOperators_shouldReturnCondition")
    void parseJsonToCondition_givenNullOperators_shouldReturnCondition() throws JsonProcessingException {
        String[] nullOperators = {"isNull", "isNotNull"};

        for (String operator : nullOperators) {
            String json = String.format("""
            {
              "type": "StringQuery",
              "column": "description",
              "operator": "%s",
              "value": null
            }
            """, operator);

            when(mockReplacementService.processJsonPlaceholders(json)).thenReturn(json);

            assertDoesNotThrow(() -> {
                Condition result = queryExecutionService.parseJsonToCondition(json);
                assertNotNull(result, "Result should not be null for null operator: " + operator);
            }, "Should not throw exception for null operator: " + operator);
        }
    }
}
