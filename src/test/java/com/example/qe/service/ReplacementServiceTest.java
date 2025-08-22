package com.example.qe.service;

import com.example.qe.resolver.impl.BasicPlaceholderResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ReplacementService Tests - Basic Placeholder Resolution")
class ReplacementServiceTest {

    private ReplacementService replacementService;
    private String todayString;

    @BeforeEach
    void setUp() {
        // Create service with basic resolver
        BasicPlaceholderResolver resolver = new BasicPlaceholderResolver();
        replacementService = new ReplacementService(List.of(resolver));
        todayString = LocalDate.now().toString();
    }

    @Test
    @DisplayName("Should replace [me] placeholder in StringQuery")
    void shouldReplaceMePlaceholderInStringQuery() {
        String jsonWithPlaceholder = """
        {
          "type": "StringQuery",
          "column": "created_by",
          "operator": "equals",
          "value": "[me]"
        }
        """;

        String expectedJson = """
        {
          "type": "StringQuery",
          "column": "created_by",
          "operator": "equals",
          "value": "current_user"
        }
        """;

        String result = replacementService.processJsonPlaceholders(jsonWithPlaceholder);
        
        // Remove whitespace for comparison
        String cleanResult = result.replaceAll("\\s", "");
        String cleanExpected = expectedJson.replaceAll("\\s", "");
        
        assertEquals(cleanExpected, cleanResult);
    }

    @Test
    @DisplayName("Should replace [today] placeholder in DateQuery")
    void shouldReplaceTodayPlaceholderInDateQuery() {
        String jsonWithPlaceholder = """
        {
          "type": "DateQuery",
          "column": "created_date",
          "operator": "equals",
          "value": "[today]"
        }
        """;

        String result = replacementService.processJsonPlaceholders(jsonWithPlaceholder);
        
        assertTrue(result.contains("\"" + todayString + "\""), 
                   "Result should contain today's date: " + todayString);
        assertTrue(result.contains("DateQuery"));
        assertTrue(result.contains("created_date"));
        assertTrue(result.contains("equals"));
    }

    @Test
    @DisplayName("Should replace multiple placeholders in complex query")
    void shouldReplaceMultiplePlaceholdersInComplexQuery() {
        String jsonWithPlaceholders = """
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

        String result = replacementService.processJsonPlaceholders(jsonWithPlaceholders);
        
        // Should replace both placeholders
        assertTrue(result.contains("\"current_user\""), "Should replace [me] with current_user");
        assertTrue(result.contains("\"" + todayString + "\""), "Should replace [today] with today's date");
        assertFalse(result.contains("[me]"), "Should not contain original [me] placeholder");
        assertFalse(result.contains("[today]"), "Should not contain original [today] placeholder");
    }

    @Test
    @DisplayName("Should not modify JSON without placeholders")
    void shouldNotModifyJsonWithoutPlaceholders() {
        String originalJson = """
        {
          "type": "StringQuery",
          "column": "name",
          "operator": "equals",
          "value": "regular_value"
        }
        """;

        String result = replacementService.processJsonPlaceholders(originalJson);
        
        // Should be unchanged (ignoring whitespace)
        String cleanResult = result.replaceAll("\\s", "");
        String cleanOriginal = originalJson.replaceAll("\\s", "");
        
        assertEquals(cleanOriginal, cleanResult);
    }

    @Test
    @DisplayName("Should not modify unsupported placeholders")
    void shouldNotModifyUnsupportedPlaceholders() {
        String jsonWithUnsupportedPlaceholder = """
        {
          "type": "StringQuery",
          "column": "status",
          "operator": "equals",
          "value": "[unknown]"
        }
        """;

        String result = replacementService.processJsonPlaceholders(jsonWithUnsupportedPlaceholder);
        
        // Should keep the unsupported placeholder unchanged
        assertTrue(result.contains("[unknown]"), "Should keep unsupported placeholder");
    }

    @Test
    @DisplayName("Should handle empty and null JSON")
    void shouldHandleEmptyAndNullJson() {
        assertNull(replacementService.processJsonPlaceholders(null));
        assertEquals("", replacementService.processJsonPlaceholders(""));
        assertEquals("   ", replacementService.processJsonPlaceholders("   "));
    }

    @Test
    @DisplayName("Should correctly identify resolvable placeholders")
    void shouldCorrectlyIdentifyResolvablePlaceholders() {
        assertTrue(replacementService.hasResolvablePlaceholder("[me]"));
        assertTrue(replacementService.hasResolvablePlaceholder("[today]"));
        assertTrue(replacementService.hasResolvablePlaceholder("some text [me] more text"));
        
        assertFalse(replacementService.hasResolvablePlaceholder("[unknown]"));
        assertFalse(replacementService.hasResolvablePlaceholder("regular text"));
        assertFalse(replacementService.hasResolvablePlaceholder(null));
        assertFalse(replacementService.hasResolvablePlaceholder(""));
    }
}
