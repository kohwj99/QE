/**
 * JavaScript code to generate Mermaid UML Class Diagram for Query Engine (QE) Codebase
 * This code follows UML standard conventions and visualizes the complete architecture
 * Updated: August 22, 2025 - ACCURATE representation of current codebase with ALL components
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
        -OperatorFactory operatorFactory
        -ObjectMapper objectMapper
        -DSLContext dsl
        -ReplacementService replacementService
        +QueryExecutionService(OperatorFactory, ConversionService, String, ReplacementService)
        +Condition parseJsonToCondition(String json)
    }

    class ReplacementService {
        <<@Service>>
        -List~ValueResolver~ resolvers
        +ReplacementService(List~ValueResolver~ resolvers)
        +String processJsonPlaceholders(String json)
        -String replacePlaceholder(String value, Class targetType)
    }

    %% === REPLACEMENT MODEL ===
    class Replaceable {
        -String placeholder
        -String type
        +Replaceable(String placeholder, String type)
        +fromString(String value)$
        +isPlaceholder(String value)$
        -inferType(String placeholderName)$
        +getPlaceholder()
        +getType()
    }

    %% === VALUE RESOLVER PATTERN ===
    class ValueResolver {
        <<interface>>
        +boolean canResolve(String placeholder)*
        +Object resolve(String placeholder, Class targetType)*
    }

    class BasicPlaceholderResolver {
        <<@Component>>
        +boolean canResolve(String placeholder)
        +Object resolve(String placeholder, Class targetType)
    }

    %% === UTILITY CLASSES ===
    class OperatorRegistry {
        -Map~String, Map~Class, GenericOperator~~ operators
        +register(String, Class, GenericOperator)
        +GenericOperator get(String, Class)
        +Set~String~ getAllOperatorNames()
        +Map~Class, GenericOperator~ getOperatorsForName(String)
        +int getTotalOperatorCount()
    }

    class OperatorFactory {
        -OperatorRegistry registry
        +OperatorFactory(OperatorRegistry)
        +GenericOperator resolve(String, Class)
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
        +Condition apply(Field~T~ field, T value)*
    }

    class CustomOperator~T~ {
        <<interface>>
        +Condition applyToField(Field<?> field, T value)*
        +Condition apply(Field~T~ field, T value)
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

    %% === DATE OPERATORS (with BigDecimal parameters) ===
    class DaysBeforeOperator {
        <<@OperatorAnnotation>>
        +Condition apply(Field~LocalDate~ field, BigDecimal days)
    }

    class DaysAfterOperator {
        <<@OperatorAnnotation>>
        +Condition apply(Field~LocalDate~ field, BigDecimal days)
    }

    class MonthsBeforeOperator {
        <<@OperatorAnnotation>>
        +Condition apply(Field~LocalDate~ field, BigDecimal months)
    }

    class MonthsAfterOperator {
        <<@OperatorAnnotation>>
        +Condition apply(Field~LocalDate~ field, BigDecimal months)
    }

    class YearsBeforeOperator {
        <<@OperatorAnnotation>>
        +Condition apply(Field~LocalDate~ field, BigDecimal years)
    }

    class YearsAfterOperator {
        <<@OperatorAnnotation>>
        +Condition apply(Field~LocalDate~ field, BigDecimal years)
    }

    class MonthEqualOperator {
        <<@OperatorAnnotation>>
        +Condition apply(Field~LocalDate~ field, BigDecimal month)
    }

    class YearEqualOperator {
        <<@OperatorAnnotation>>
        +Condition apply(Field~LocalDate~ field, BigDecimal year)
    }

    class DayEqualOperator {
        <<@OperatorAnnotation>>
        +Condition apply(Field~LocalDate~ field, BigDecimal day)
    }

    class DayOfMonthOperator {
        <<@OperatorAnnotation>>
        +Condition apply(Field~LocalDate~ field, BigDecimal dayOfMonth)
    }

    %% === QUERY MODEL ===
    class Query {
        <<interface>>
        <<@JsonTypeInfo>>
        +Condition toCondition(DSLContext dsl, OperatorFactory operatorFactory)*
    }

    class FieldQuery~T~ {
        <<abstract>>
        <<@Getter @Setter>>
        #String column
        #String operator
        #T value
        +FieldQuery()
        +FieldQuery(String column, String operator, T value)
        +Condition toCondition(DSLContext dsl, OperatorFactory operatorFactory)
        #Class~T~ getValueClass()*
        +String getColumn()
        +String getOperator()
        +T getValue()
    }

    class CompositeQuery {
        <<abstract>>
        <<@Getter>>
        #List~Query~ children
        +CompositeQuery(List~Query~ children)
        +Condition toCondition(DSLContext dsl, OperatorFactory operatorFactory)
        #Condition combineConditions(List~Condition~ conditions)*
        +List~Query~ getChildren()
    }

    %% === FIELD QUERY IMPLEMENTATIONS ===
    class StringQuery {
        <<@JsonTypeName("StringQuery")>>
        +StringQuery()
        +StringQuery(String column, String operator, String value)
        #Class~String~ getValueClass()
    }

    class NumericQuery {
        <<@JsonTypeName("NumericQuery")>>
        +NumericQuery()
        +NumericQuery(String column, String operator, BigDecimal value)
        #Class~BigDecimal~ getValueClass()
    }

    class DateQuery {
        <<@JsonTypeName("DateQuery")>>
        +DateQuery()
        +DateQuery(String column, String operator, LocalDate value)
        #Class~LocalDate~ getValueClass()
    }

    class BoolQuery {
        <<@JsonTypeName("BoolQuery")>>
        +BoolQuery()
        +BoolQuery(String column, String operator, Boolean value)
        #Class~Boolean~ getValueClass()
    }

    %% === COMPOSITE QUERY IMPLEMENTATIONS ===
    class AndQuery {
        <<@JsonTypeName("AndQuery")>>
        +AndQuery()
        +AndQuery(List~Query~ queries)
        #Condition combineConditions(List~Condition~ conditions)
    }

    class OrQuery {
        <<@JsonTypeName("OrQuery")>>
        +OrQuery()
        +OrQuery(List~Query~ queries)
        #Condition combineConditions(List~Condition~ conditions)
    }

    %% === COMPREHENSIVE TEST SUITE ===
    class QueryProcessingIntegrationTest {
        <<@TestMethodOrder>>
        -QueryExecutionService queryExecutionService
        -OperatorRegistry operatorRegistry
        -OperatorFactory operatorFactory
        +parseJsonToCondition_givenSimpleStringEquality_shouldGenerateCorrectCondition()
        +parseJsonToCondition_givenNumericRangeQuery_shouldGenerateCorrectCondition()
        +parseJsonToCondition_givenDateComparison_shouldGenerateCorrectCondition()
        +parseJsonToCondition_givenComplexBooleanLogic_shouldGenerateCorrectCondition()
        +parseJsonToCondition_givenInvalidOperator_shouldThrowException()
        +parseJsonToCondition_givenNullInput_shouldThrowIllegalArgumentException()
        +parseJsonToCondition_givenEmptyInput_shouldThrowIllegalArgumentException()
        +parseJsonToCondition_givenCompleteFlow_shouldValidateEntirePipeline()
    }

    class SimpleQueryProcessingIntegrationTest {
        <<@TestMethodOrder>>
        -QueryExecutionService queryExecutionService
        -OperatorRegistry operatorRegistry
        -OperatorFactory operatorFactory
        +processStringQuery_givenValidInput_shouldGenerateCondition()
        +processNumericQuery_givenValidInput_shouldGenerateCondition()
        +processComplexQuery_givenAndLogic_shouldGenerateCondition()
        +processInvalidOperator_givenInvalidInput_shouldThrowException()
        +validatePipelineComponents_givenSetup_shouldValidateAllComponents()
    }

    class OperatorScannerUnitTest {
        <<@DisplayName>>
        -OperatorRegistry mockRegistry
        -OperatorScanner operatorScanner
        +constructor_givenValidRegistry_shouldCreateInstance()
        +scanAndRegister_givenValidPackage_shouldRegisterOperators()
        +scanAndRegister_givenEmptyPackage_shouldNotRegisterAnyOperators()
        +scanAndRegister_givenSpecificOperatorPackage_shouldRegisterSpecificOperators()
    }

    class QueryExecutionServiceUnitTest {
        <<@DisplayName>>
        +parseJsonToCondition_givenValidJSON_shouldReturnCondition()
    }

    class OperatorFactoryUnitTest {
        <<@DisplayName>>
        +resolve_givenValidOperator_shouldReturnOperator()
    }

    class ReplacementServiceTest {
        <<@DisplayName>>
        +processJsonPlaceholders_givenValidPlaceholders_shouldReplace()
    }

    class QueryDeserializationTest {
        <<@DisplayName>>
        +deserializeQuery_givenValidJSON_shouldCreateQuery()
    }

    class QeApplicationTests {
        <<@SpringBootTest>>
        +contextLoads()
    }

    %% === RELATIONSHIPS ===
    %% Application and Service Layer
    QeApplication ..> QueryExecutionService : uses

    %% Service Dependencies
    QueryExecutionService --> OperatorFactory : depends on
    QueryExecutionService --> ReplacementService : depends on
    QueryExecutionService --> ObjectMapper : uses
    QueryExecutionService --> DSLContext : uses

    %% Replacement Service Dependencies
    ReplacementService --> ValueResolver : uses list of
    ValueResolver <|.. BasicPlaceholderResolver : implements
    ReplacementService ..> Replaceable : processes

    %% Utility Class Relationships
    OperatorFactory --> OperatorRegistry : uses
    OperatorScanner --> OperatorRegistry : registers operators
    OperatorScanner ..> OperatorAnnotation : scans for

    %% Operator Inheritance - CORRECTED
    GenericOperator <|-- CustomOperator : extends
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

    %% Query Inheritance - CORRECTED
    Query <|.. FieldQuery : implements
    Query <|.. CompositeQuery : implements
    FieldQuery <|-- StringQuery : extends
    FieldQuery <|-- NumericQuery : extends
    FieldQuery <|-- DateQuery : extends
    FieldQuery <|-- BoolQuery : extends
    CompositeQuery <|-- AndQuery : extends
    CompositeQuery <|-- OrQuery : extends

    %% Registry Storage
    OperatorRegistry o-- GenericOperator : stores

    %% Query Processing
    Query ..> OperatorFactory : uses
    FieldQuery ..> OperatorFactory : resolves operators

    %% Test Dependencies
    QueryProcessingIntegrationTest ..> QueryExecutionService : tests
    QueryProcessingIntegrationTest ..> OperatorRegistry : validates
    QueryProcessingIntegrationTest ..> OperatorFactory : validates
    SimpleQueryProcessingIntegrationTest ..> QueryExecutionService : tests
    OperatorScannerUnitTest ..> OperatorScanner : tests
    QueryExecutionServiceUnitTest ..> QueryExecutionService : tests
    OperatorFactoryUnitTest ..> OperatorFactory : tests
    ReplacementServiceTest ..> ReplacementService : tests
    QueryDeserializationTest ..> Query : tests
    QeApplicationTests ..> QeApplication : tests

    %% External Dependencies (shown as notes)
    note for Query "Uses JOOQ DSLContext\\nfor SQL generation"
    note for GenericOperator "Returns JOOQ Condition\\nobjects for SQL building"
    note for CustomOperator "Extends GenericOperator\\nwith flexible field handling"
    note for OperatorAnnotation "Defines operator metadata:\\n- name, supported types,\\n- description"
    note for ReplacementService "Processes placeholders\\nlike [me] and [today]\\nin JSON queries"
    note for ValueResolver "Strategy pattern for\\nresolving different\\nplaceholder types"

    %% Styling
    classDef operatorClass fill:#e1f5fe,stroke:#01579b,stroke-width:2px
    classDef queryClass fill:#f3e5f5,stroke:#4a148c,stroke-width:2px
    classDef utilClass fill:#e8f5e8,stroke:#1b5e20,stroke-width:2px
    classDef serviceClass fill:#fff3e0,stroke:#e65100,stroke-width:2px
    classDef testClass fill:#fce4ec,stroke:#880e4f,stroke-width:2px
    classDef replacementClass fill:#f1f8e9,stroke:#33691e,stroke-width:2px

    class EqualsOperator,NotEqualsOperator,GreaterThanOperator,GreaterThanEqualOperator,LessThanOperator,LessThanEqualOperator,LikeOperator,StartsWithOperator,EndsWithOperator,IsNullOperator,IsNotNullOperator,DaysBeforeOperator,DaysAfterOperator,MonthsBeforeOperator,MonthsAfterOperator,YearsBeforeOperator,YearsAfterOperator,MonthEqualOperator,YearEqualOperator,DayEqualOperator,DayOfMonthOperator operatorClass

    class GenericOperator,CustomOperator operatorClass

    class Query,FieldQuery,CompositeQuery,StringQuery,NumericQuery,DateQuery,BoolQuery,AndQuery,OrQuery queryClass

    class OperatorRegistry,OperatorFactory,OperatorScanner utilClass

    class QeApplication,QueryExecutionService,ReplacementService serviceClass

    class QueryProcessingIntegrationTest,SimpleQueryProcessingIntegrationTest,OperatorScannerUnitTest,QueryExecutionServiceUnitTest,OperatorFactoryUnitTest,ReplacementServiceTest,QueryDeserializationTest,QeApplicationTests testClass

    class ValueResolver,BasicPlaceholderResolver,Replaceable replacementClass
`;

// Export the diagram for use in Mermaid Live Editor or documentation
console.log('=== QE (Query Engine) Codebase Architecture Diagram ===');
console.log('Updated: August 22, 2025 - Complete current architecture');
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
console.log('• Service Layer: QueryExecutionService + ReplacementService');
console.log('• Replacement System: ValueResolver pattern for dynamic placeholders');
console.log('• Query Model: Hierarchical query types (FieldQuery, CompositeQuery)');
console.log('• Operator Model: GenericOperator + CustomOperator interfaces with 21+ implementations');
console.log('• Utility Layer: Registry, Factory, Scanner for operator management');
console.log('• Date Operators: BigDecimal parameters for flexible date calculations');
console.log('• JSON Processing: Jackson annotations + placeholder replacement');
console.log('• SQL Generation: JOOQ integration for database query building');
console.log('• Testing: Comprehensive integration tests and unit tests (8 test classes)');
console.log('• Value Resolution: Supports [me], [today] and extensible placeholders');
console.log('• CORRECTED: Added missing CustomOperator interface and all test classes');

// Export for module usage
if (typeof module !== 'undefined' && module.exports) {
    module.exports = { mermaidDiagram };
}
