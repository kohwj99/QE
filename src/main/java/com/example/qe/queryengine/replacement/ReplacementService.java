package com.example.qe.queryengine.replacement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service responsible for replacing dynamic placeholders with actual values
 * Simplified - no priority needed since placeholders are hardcoded by developers with no overlaps
 */
@Service
public class ReplacementService {

    private final List<ReplacementResolver> resolvers;

    @Autowired
    public ReplacementService(List<ReplacementResolver> resolvers) {
        // Spring will automatically inject all ValueResolver beans
        // No need for priority sorting since there are no overlaps
        this.resolvers = resolvers;
    }

    /**
     * Processes a JSON string and replaces placeholders like [me] and [today]
     * @param json The JSON string potentially containing placeholders
     * @return JSON string with placeholders replaced
     */
    public String processJsonPlaceholders(String json) {
        if (json == null || json.trim().isEmpty()) {
            return json;
        }

        String processedJson = json;

        // Process each resolver - order doesn't matter since no overlaps
        for (ReplacementResolver resolver : resolvers) {
            processedJson = replacePlaceholdersInJson(processedJson, resolver);
        }

        return processedJson;
    }

    private String replacePlaceholdersInJson(String json, ReplacementResolver resolver) {
        // Find and replace patterns like "[me]" or "[today]" in JSON values
        Pattern pattern = Pattern.compile("\"\\[([^\\]]+)\\]\"");
        Matcher matcher = pattern.matcher(json);

        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            String placeholder = matcher.group(1);

            if (resolver.canResolve(placeholder)) {
                Object resolved = resolver.resolve(placeholder, String.class);
                String replacement = "\"" + resolved.toString() + "\"";
                matcher.appendReplacement(result, replacement);
            } else {
                // Keep the original if can't resolve
                matcher.appendReplacement(result, matcher.group());
            }
        }

        matcher.appendTail(result);
        return result.toString();
    }

    /**
     * Checks if a value contains a resolvable placeholder
     */
    public boolean hasResolvablePlaceholder(String value) {
        if (value == null || !value.matches(".*\\[\\w+\\].*")) {
            return false;
        }

        Pattern pattern = Pattern.compile("\\[([^\\]]+)\\]");
        Matcher matcher = pattern.matcher(value);

        while (matcher.find()) {
            String placeholder = matcher.group(1);
            if (resolvers.stream().anyMatch(r -> r.canResolve(placeholder))) {
                return true;
            }
        }

        return false;
    }
}
