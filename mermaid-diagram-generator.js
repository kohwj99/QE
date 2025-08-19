/**
 * JavaScript code to generate Mermaid UML Class Diagram for Query Engine (QE) Codebase
 * This code follows UML standard conventions and visualizes the complete architecture
 * Updated: August 19, 2025 - Reflects current codebase with all operators and query types
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

    %% === MAIN APPLICATION ===
    class QeApplication {
        <<@SpringBootApplication>>
        +main(String[] args)$
    }

    %% === SERVICE LAYER ===
    class QueryExecutionService {
        <<@Service>>
        -QueryExecutionContext context
        +QueryExecutionService(QueryExecutionContext)
        +Condition executeQuery(Query query, DSLContext dsl)
    }

    %% === UTILITY CLASSES ===
    class QueryExecutionContext {
        -OperatorFactory operatorFactory
        -ConversionService conversionService
        +QueryExecutionContext(OperatorFactory, ConversionService)
        +OperatorFactory getOperatorFactory()
        +ConversionService getConversionService()
    }

    class OperatorRegistry {
        -Map~String, Map~Class, GenericOperator~~ operators
        +registerOperator(String, Class, GenericOperator)
        +GenericOperator getOperator(String, Class)
        +boolean hasOperator(String, Class)
        +Set~String~ getOperatorNames()
        +Map~Class, GenericOperator~ getOperatorsForName(String)
    }

    class OperatorFactory {
        -OperatorRegistry registry
        +OperatorFactory(OperatorRegistry)
        +GenericOperator getOperator(String, Class)
    }

    class OperatorScanner {
        -OperatorRegistry registry
        +OperatorScanner(OperatorRegistry)
        +scanAndRegister(String packageName)
        -processOperatorClass(Class)
        -extractOperatorInfo(OperatorAnnotation)
    }

    %% === OPERATOR MODEL ===
    class GenericOperator~T~ {
        <<interface>>
        +Condition apply(Field field, T value)*
    }

    %% === COMPARISON OPERATORS ===
    class EqualsOperator {
        <<@OperatorAnnotation>>
        +Condition apply(Field field, Object value)
    }

    class NotEqualsOperator {
        <<@OperatorAnnotation>>
        +Condition apply(Field field, Object value)
    }

    class GreaterThanOperator {
        <<@OperatorAnnotation>>
        +Condition apply(Field field, Comparable value)
    }

    class GreaterThanEqualOperator {
        <<@OperatorAnnotation>>
        +Condition apply(Field field, Comparable value)
    }

    class LessThanOperator {
        <<@OperatorAnnotation>>
        +Condition apply(Field field, Comparable value)
    }

    class LessThanEqualOperator {
        <<@OperatorAnnotation>>
        +Condition apply(Field field, Comparable value)
    }

    %% === STRING OPERATORS ===
    class LikeOperator {
        <<@OperatorAnnotation>>
        +Condition apply(Field field, String value)
    }

    class StartsWithOperator {
        <<@OperatorAnnotation>>
        +Condition apply(Field field, String value)
    }

    class EndsWithOperator {
        <<@OperatorAnnotation>>
        +Condition apply(Field field, String value)
    }

    %% === NULL CHECK OPERATORS ===
    class IsNullOperator {
        <<@OperatorAnnotation>>
        +Condition apply(Field field, Object value)
    }

    class IsNotNullOperator {
        <<@OperatorAnnotation>>
        +Condition apply(Field field, Object value)
    }

    %% === COLLECTION OPERATORS ===
    class InOperator {
        <<@OperatorAnnotation>>
        +Condition apply(Field field, Collection value)
    }

    %% === DATE OPERATORS (with Integer parameters) ===
    class DaysBeforeOperator {
        <<@OperatorAnnotation>>
        +Condition apply(Field~LocalDate~ field, Integer days)
    }

    class DaysAfterOperator {
        <<@OperatorAnnotation>>
        +Condition apply(Field~LocalDate~ field, Integer days)
    }

    class MonthsBeforeOperator {
        <<@OperatorAnnotation>>
        +Condition apply(Field~LocalDate~ field, Integer months)
    }

    class MonthsAfterOperator {
        <<@OperatorAnnotation>>
        +Condition apply(Field~LocalDate~ field, Integer months)
    }

    class YearsBeforeOperator {
        <<@OperatorAnnotation>>
        +Condition apply(Field~LocalDate~ field, Integer years)
    }

    class YearsAfterOperator {
        <<@OperatorAnnotation>>
        +Condition apply(Field~LocalDate~ field, Integer years)
    }

    class MonthEqualOperator {
        <<@OperatorAnnotation>>
        +Condition apply(Field~LocalDate~ field, Integer month)
    }

    class YearEqualOperator {
        <<@OperatorAnnotation>>
        +Condition apply(Field~LocalDate~ field, Integer year)
    }

    class DayEqualOperator {
        <<@OperatorAnnotation>>
        +Condition apply(Field~LocalDate~ field, Integer day)
    }

    class DayOfMonthOperator {
        <<@OperatorAnnotation>>
        +Condition apply(Field~LocalDate~ field, Integer dayOfMonth)
    }

    %% === QUERY MODEL ===
    class Query {
        <<interface>>
        <<@JsonTypeInfo>>
        +Condition toCondition(DSLContext dsl, QueryExecutionContext context)*
    }

    class FieldQuery~T~ {
        <<abstract>>
        <<@JsonTypeName>>
        #String column
        #String operatorName
        #T value
        +FieldQuery(String column, String operatorName, T value)
        +Condition toCondition(DSLContext dsl, QueryExecutionContext context)
        #Class~T~ getValueClass()*
        +String getColumn()
        +String getOperatorName()
        +T getValue()
    }

    class CompositeQuery {
        <<abstract>>
        <<@JsonTypeName>>
        #List~Query~ children
        +CompositeQuery(List~Query~ children)
        +Condition toCondition(DSLContext dsl, QueryExecutionContext context)
        #Condition combineConditions(List~Condition~ conditions)*
        +List~Query~ getChildren()
    }

    %% === FIELD QUERY IMPLEMENTATIONS ===
    class StringQuery {
        <<@JsonTypeName("StringQuery")>>
        +StringQuery(String column, String operatorName, String value)
        #Class~String~ getValueClass()
    }

    class NumericQuery {
        <<@JsonTypeName("NumericQuery")>>
        +NumericQuery(String column, String operatorName, BigDecimal value)
        #Class~BigDecimal~ getValueClass()
    }

    class DateQuery {
        <<@JsonTypeName("DateQuery")>>
        +DateQuery(String column, String operatorName, LocalDate value)
        #Class~LocalDate~ getValueClass()
    }

    class BoolQuery {
        <<@JsonTypeName("BoolQuery")>>
        +BoolQuery(String column, String operatorName, Boolean value)
        #Class~Boolean~ getValueClass()
    }

    %% === COMPOSITE QUERY IMPLEMENTATIONS ===
    class AndQuery {
        <<@JsonTypeName("AndQuery")>>
        +AndQuery(List~Query~ children)
        #Condition combineConditions(List~Condition~ conditions)
    }

    class OrQuery {
        <<@JsonTypeName("OrQuery")>>
        +OrQuery(List~Query~ children)
        #Condition combineConditions(List~Condition~ conditions)
    }

    %% === RELATIONSHIPS ===
    %% Application and Service Layer
    QeApplication ..> QueryExecutionService : uses

    %% Service Dependencies
    QueryExecutionService --> QueryExecutionContext : depends on
    QueryExecutionContext --> OperatorFactory : contains
    QueryExecutionContext --> ConversionService : contains

    %% Utility Class Relationships
    OperatorFactory --> OperatorRegistry : uses
    OperatorScanner --> OperatorRegistry : registers operators
    OperatorScanner ..> OperatorAnnotation : scans for

    %% Operator Inheritance
    GenericOperator <|.. EqualsOperator
    GenericOperator <|.. NotEqualsOperator
    GenericOperator <|.. GreaterThanOperator
    GenericOperator <|.. GreaterThanEqualOperator
    GenericOperator <|.. LessThanOperator
    GenericOperator <|.. LessThanEqualOperator
    GenericOperator <|.. LikeOperator
    GenericOperator <|.. StartsWithOperator
    GenericOperator <|.. EndsWithOperator
    GenericOperator <|.. IsNullOperator
    GenericOperator <|.. IsNotNullOperator
    GenericOperator <|.. InOperator
    GenericOperator <|.. DaysBeforeOperator
    GenericOperator <|.. DaysAfterOperator
    GenericOperator <|.. MonthsBeforeOperator
    GenericOperator <|.. MonthsAfterOperator
    GenericOperator <|.. YearsBeforeOperator
    GenericOperator <|.. YearsAfterOperator
    GenericOperator <|.. MonthEqualOperator
    GenericOperator <|.. YearEqualOperator
    GenericOperator <|.. DayEqualOperator
    GenericOperator <|.. DayOfMonthOperator

    %% Query Inheritance
    Query <|.. FieldQuery
    Query <|.. CompositeQuery
    FieldQuery <|-- StringQuery
    FieldQuery <|-- NumericQuery
    FieldQuery <|-- DateQuery
    FieldQuery <|-- BoolQuery
    CompositeQuery <|-- AndQuery
    CompositeQuery <|-- OrQuery

    %% Registry Storage
    OperatorRegistry o-- GenericOperator : stores

    %% Query Processing
    Query ..> QueryExecutionContext : uses
    FieldQuery ..> OperatorFactory : uses via context

    %% External Dependencies (shown as notes)
    note for Query "Uses JOOQ DSLContext\\nfor SQL generation"
    note for GenericOperator "Returns JOOQ Condition\\nobjects for SQL building"
    note for OperatorAnnotation "Defines operator metadata:\\n- name, supported types,\\n- description"

    %% Styling
    classDef operatorClass fill:#e1f5fe,stroke:#01579b,stroke-width:2px
    classDef queryClass fill:#f3e5f5,stroke:#4a148c,stroke-width:2px
    classDef utilClass fill:#e8f5e8,stroke:#1b5e20,stroke-width:2px
    classDef serviceClass fill:#fff3e0,stroke:#e65100,stroke-width:2px

    class EqualsOperator,NotEqualsOperator,GreaterThanOperator,GreaterThanEqualOperator,LessThanOperator,LessThanEqualOperator,LikeOperator,StartsWithOperator,EndsWithOperator,IsNullOperator,IsNotNullOperator,InOperator,DaysBeforeOperator,DaysAfterOperator,MonthsBeforeOperator,MonthsAfterOperator,YearsBeforeOperator,YearsAfterOperator,MonthEqualOperator,YearEqualOperator,DayEqualOperator,DayOfMonthOperator operatorClass

    class Query,FieldQuery,CompositeQuery,StringQuery,NumericQuery,DateQuery,BoolQuery,AndQuery,OrQuery queryClass

    class OperatorRegistry,OperatorFactory,OperatorScanner,QueryExecutionContext utilClass

    class QeApplication,QueryExecutionService serviceClass
`;

// Export the diagram for use in Mermaid Live Editor or documentation
console.log('=== QE (Query Engine) Codebase Architecture Diagram ===');
console.log('Copy the following Mermaid code to visualize the architecture:');
console.log('');
console.log(mermaidDiagram);
console.log('');
console.log('=== Usage Instructions ===');
console.log('1. Copy the diagram code above');
console.log('2. Go to https://mermaid.live/');
console.log('3. Paste the code in the editor');
console.log('4. View the generated UML class diagram');
console.log('');
console.log('=== Architecture Overview ===');
console.log('• Main Application: QeApplication (Spring Boot)');
console.log('• Service Layer: QueryExecutionService for query processing');
console.log('• Query Model: Hierarchical query types (Field, Composite)');
console.log('• Operator Model: 21+ operators for different data types and operations');
console.log('• Utility Layer: Registry, Factory, Scanner for operator management');
console.log('• Date Operators: Special operators with LocalDate fields + Integer parameters');
console.log('• JSON Serialization: Jackson annotations for query deserialization');
console.log('• SQL Generation: JOOQ integration for database query building');

// Export for module usage
if (typeof module !== 'undefined' && module.exports) {
    module.exports = { mermaidDiagram };
}
