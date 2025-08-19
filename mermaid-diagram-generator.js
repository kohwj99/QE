/**
 * JavaScript code to generate Mermaid UML Class Diagram for Query Engine (QE) Codebase
 * This code follows UML standard conventions and visualizes the complete architecture
 */

const mermaidDiagram = `
classDiagram
    %% === ANNOTATIONS ===
    class OperatorAnnotation {
        <<annotation>>
        +String value()
        +Class[] types()
        +String description()
    }

    %% === CONFIGURATION CLASSES ===
    class ConversionServiceConfig {
        <<@Configuration>>
        +ConversionService conversionService()
    }

    class QueryEngineConfig {
        <<@Configuration>>
        -String OPERATOR_BASE_PACKAGE
        +OperatorRegistry operatorRegistry()
        +OperatorScanner operatorScanner(OperatorRegistry)
        +OperatorFactory operatorFactory(OperatorRegistry)
        +ConversionService conversionService()
        +QueryExecutionContext queryExecutionContext(OperatorFactory, ConversionService)
    }

    %% === OPERATOR MODEL ===
    class GenericOperator~T~ {
        <<interface>>
        +Condition apply(Field~T~ field, T value)*
    }

    %% Operator Implementations
    class EqualsOperator~T~ {
        <<@OperatorAnnotation>>
        +Condition apply(Field~T~ field, T value)
    }

    class NotEqualsOperator~T~ {
        <<@OperatorAnnotation>>
        +Condition apply(Field~T~ field, T value)
    }

    class GreaterThanOperator~T~ {
        <<@OperatorAnnotation>>
        +Condition apply(Field~T~ field, T value)
    }

    class LessThanOperator~T~ {
        <<@OperatorAnnotation>>
        +Condition apply(Field~T~ field, T value)
    }

    class LikeOperator {
        <<@OperatorAnnotation>>
        +Condition apply(Field~String~ field, String value)
    }

    class InOperator~T~ {
        <<@OperatorAnnotation>>
        +Condition apply(Field~T~ field, T value)
    }

    class IsNullOperator~T~ {
        <<@OperatorAnnotation>>
        +Condition apply(Field~T~ field, T value)
    }

    class IsNotNullOperator~T~ {
        <<@OperatorAnnotation>>
        +Condition apply(Field~T~ field, T value)
    }

    class DaysBeforeOperator {
        <<@OperatorAnnotation>>
        +Condition apply(Field~LocalDate~ field, LocalDate value)
    }

    %% === QUERY MODEL ===
    class Query {
        <<interface>>
        <<@JsonTypeInfo>>
        +Condition toCondition(DSLContext dsl, QueryExecutionContext context)*
    }

    class FieldQuery~T~ {
        <<abstract>>
        #String column
        #String operator
        #T value
        #FieldQuery()
        #FieldQuery(String column, String operator, T value)
        +Condition toCondition(DSLContext dsl, QueryExecutionContext context)
        #Class~T~ getValueClass()*
    }

    class CompositeQuery {
        <<abstract>>
        -List~Query~ children
        +CompositeQuery(List~Query~ children)
        +List~Query~ getChildren()
        +Condition toCondition(DSLContext dsl, QueryExecutionContext context)
        #Condition combineConditions(List~Condition~ conditions)*
    }

    %% Field Query Implementations
    class StringQuery {
        <<@JsonTypeName>>
        +StringQuery()
        +StringQuery(String column, String operator, String value)
        #Class~String~ getValueClass()
    }

    class NumericQuery {
        <<@JsonTypeName>>
        +NumericQuery()
        +NumericQuery(String column, String operator, BigDecimal value)
        #Class~BigDecimal~ getValueClass()
    }

    class DateQuery {
        <<@JsonTypeName>>
        +DateQuery()
        +DateQuery(String column, String operator, LocalDate value)
        #Class~LocalDate~ getValueClass()
    }

    class BoolQuery {
        <<@JsonTypeName>>
        +BoolQuery()
        +BoolQuery(String column, String operator, Boolean value)
        #Class~Boolean~ getValueClass()
    }

    %% Composite Query Implementations
    class AndQuery {
        <<@JsonTypeName>>
        +AndQuery(List~Query~ children)
        #Condition combineConditions(List~Condition~ conditions)
    }

    class OrQuery {
        <<@JsonTypeName>>
        +OrQuery(List~Query~ children)
        #Condition combineConditions(List~Condition~ conditions)
    }

    %% === UTILITY CLASSES ===
    class OperatorRegistry {
        -Logger logger
        -Map~String, Map~Class, GenericOperator~~ operators
        +void register(String operatorName, Class~T~ valueType, GenericOperator~T~ operator)
        +GenericOperator~T~ get(String operatorName, Class~T~ valueType)
        +Set~String~ getAllOperatorNames()
        +Map~Class, GenericOperator~ getOperatorsForName(String operatorName)
        +int getTotalOperatorCount()
    }

    class OperatorFactory {
        -OperatorRegistry registry
        +OperatorFactory(OperatorRegistry registry)
        +GenericOperator~T~ resolve(String operatorName, Class~T~ valueType)
    }

    class OperatorScanner {
        -OperatorRegistry registry
        +OperatorScanner(OperatorRegistry registry)
        +void scanAndRegister(String basePackage)
        -void processOperatorClass(Class clazz)
        -void registerOperatorForTypes(Class operatorClass, String operatorName, Class[] supportedTypes)
    }

    class QueryExecutionContext {
        -OperatorFactory operatorFactory
        -ConversionService conversionService
        +QueryExecutionContext(OperatorFactory operatorFactory, ConversionService conversionService)
        +OperatorFactory getOperatorFactory()
        +ConversionService getConversionService()
    }

    %% === SERVICE CLASSES ===
    class QueryExecutionService {
        <<@Service>>
    }

    %% === MAIN APPLICATION ===
    class QeApplication {
        <<@SpringBootApplication>>
        +void main(String[] args)
    }

    %% === RELATIONSHIPS ===

    %% Operator Relationships
    GenericOperator <|.. EqualsOperator : implements
    GenericOperator <|.. NotEqualsOperator : implements
    GenericOperator <|.. GreaterThanOperator : implements
    GenericOperator <|.. LessThanOperator : implements
    GenericOperator <|.. LikeOperator : implements
    GenericOperator <|.. InOperator : implements
    GenericOperator <|.. IsNullOperator : implements
    GenericOperator <|.. IsNotNullOperator : implements
    GenericOperator <|.. DaysBeforeOperator : implements

    %% Query Relationships
    Query <|.. FieldQuery : implements
    Query <|.. CompositeQuery : implements

    FieldQuery <|-- StringQuery : extends
    FieldQuery <|-- NumericQuery : extends
    FieldQuery <|-- DateQuery : extends
    FieldQuery <|-- BoolQuery : extends

    CompositeQuery <|-- AndQuery : extends
    CompositeQuery <|-- OrQuery : extends

    %% Composition Relationships
    CompositeQuery *-- Query : contains
    OperatorRegistry *-- GenericOperator : stores
    OperatorFactory *-- OperatorRegistry : uses
    QueryExecutionContext *-- OperatorFactory : uses
    QueryExecutionContext *-- ConversionService : uses
    OperatorScanner *-- OperatorRegistry : uses

    %% Configuration Dependencies
    QueryEngineConfig ..> OperatorRegistry : creates
    QueryEngineConfig ..> OperatorScanner : creates
    QueryEngineConfig ..> OperatorFactory : creates
    QueryEngineConfig ..> QueryExecutionContext : creates
    QueryEngineConfig ..> ConversionService : creates

    %% Annotation Usage
    OperatorAnnotation ..> EqualsOperator : annotates
    OperatorAnnotation ..> NotEqualsOperator : annotates
    OperatorAnnotation ..> GreaterThanOperator : annotates
    OperatorAnnotation ..> LessThanOperator : annotates
    OperatorAnnotation ..> LikeOperator : annotates
    OperatorAnnotation ..> InOperator : annotates
    OperatorAnnotation ..> IsNullOperator : annotates
    OperatorAnnotation ..> IsNotNullOperator : annotates
    OperatorAnnotation ..> DaysBeforeOperator : annotates

    %% External Dependencies (shown for context)
    class DSLContext {
        <<external>>
        <<jOOQ>>
    }

    class Condition {
        <<external>>
        <<jOOQ>>
    }

    class Field~T~ {
        <<external>>
        <<jOOQ>>
    }

    class ConversionService {
        <<external>>
        <<Spring>>
    }

    Query ..> DSLContext : uses
    Query ..> Condition : returns
    GenericOperator ..> Field : uses
    GenericOperator ..> Condition : returns

    %% Package Structure Notes
    note for OperatorAnnotation "Package: annotation"
    note for ConversionServiceConfig "Package: config"
    note for QueryEngineConfig "Package: config"
    note for GenericOperator "Package: model.operator"
    note for EqualsOperator "Package: model.operator.impl"
    note for Query "Package: model.query"
    note for StringQuery "Package: model.query.impl"
    note for OperatorRegistry "Package: util"
    note for QueryExecutionService "Package: service"
`;

/**
 * Function to render the Mermaid diagram
 * This can be called from an HTML page with Mermaid.js loaded
 */
function renderMermaidDiagram() {
    return mermaidDiagram;
}

/**
 * Function to generate HTML page with the diagram
 */
function generateHTMLPage() {
    return `
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Query Engine (QE) - Architecture Diagram</title>
    <script src="https://cdn.jsdelivr.net/npm/mermaid@10.6.1/dist/mermaid.min.js"></script>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f5f5f5;
        }
        .container {
            max-width: 100%;
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        h1 {
            color: #333;
            text-align: center;
            margin-bottom: 30px;
        }
        .diagram-container {
            text-align: center;
            overflow-x: auto;
        }
        .legend {
            margin-top: 30px;
            padding: 15px;
            background-color: #f8f9fa;
            border-radius: 5px;
        }
        .legend h3 {
            margin-top: 0;
            color: #495057;
        }
        .legend ul {
            list-style-type: none;
            padding-left: 0;
        }
        .legend li {
            margin-bottom: 5px;
            padding: 5px;
            background-color: white;
            border-left: 4px solid #007bff;
            margin-bottom: 8px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Query Engine (QE) - UML Class Diagram</h1>

        <div class="diagram-container">
            <div class="mermaid">
${mermaidDiagram}
            </div>
        </div>

        <div class="legend">
            <h3>Architecture Overview</h3>
            <ul>
                <li><strong>Annotations:</strong> OperatorAnnotation - Used to mark and configure operator implementations</li>
                <li><strong>Configuration:</strong> Spring Boot configuration classes for dependency injection</li>
                <li><strong>Operators:</strong> Implementation of various database query operations (equals, like, etc.)</li>
                <li><strong>Queries:</strong> Hierarchical query structure supporting both field-based and composite queries</li>
                <li><strong>Utilities:</strong> Registry, factory, and scanning utilities for operator management</li>
                <li><strong>Services:</strong> Business logic layer for query execution</li>
            </ul>

            <h3>Key Design Patterns</h3>
            <ul>
                <li><strong>Strategy Pattern:</strong> GenericOperator interface with multiple implementations</li>
                <li><strong>Composite Pattern:</strong> CompositeQuery for building complex query trees</li>
                <li><strong>Factory Pattern:</strong> OperatorFactory for creating operator instances</li>
                <li><strong>Registry Pattern:</strong> OperatorRegistry for managing operator instances</li>
                <li><strong>Template Method:</strong> FieldQuery abstract class with concrete implementations</li>
            </ul>
        </div>
    </div>

    <script>
        mermaid.initialize({
            startOnLoad: true,
            theme: 'default',
            flowchart: {
                useMaxWidth: true,
                htmlLabels: true
            },
            classDiagram: {
                useMaxWidth: true
            }
        });
    </script>
</body>
</html>
    `;
}

// Export functions for use
if (typeof module !== 'undefined' && module.exports) {
    module.exports = {
        renderMermaidDiagram,
        generateHTMLPage,
        mermaidDiagram
    };
}

// If running in browser, make functions available globally
if (typeof window !== 'undefined') {
    window.QEDiagram = {
        renderMermaidDiagram,
        generateHTMLPage,
        mermaidDiagram
    };
}

console.log('Query Engine UML Diagram Generator loaded successfully!');
console.log('Use renderMermaidDiagram() to get the Mermaid diagram string');
console.log('Use generateHTMLPage() to get a complete HTML page with the diagram');
