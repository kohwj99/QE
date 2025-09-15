package com.example.qe.queryengine.query;

import com.example.qe.queryengine.exception.InvalidQueryException;
import com.example.qe.queryengine.operator.OperatorFactory;
import com.example.qe.queryengine.operator.RunConditionOperator;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;
import org.jooq.Condition;
import org.jooq.DSLContext;

@Setter
@Getter
@JsonTypeName("DateQuery")
public class JavaQuery implements Query{

    //"e.g [today] dayOfWeek 1"
    protected String placeholder;
    protected String operator;
    protected Object value;
    protected ValueType valueType;

    protected JavaQuery() {
    }

    @JsonCreator
    protected JavaQuery(@JsonProperty("column") String placeholder,
                        @JsonProperty("operatorName") String operator,
                        @JsonProperty("value") Object value,
                        @JsonProperty("valueType") ValueType valueType) {
        this.placeholder = placeholder;
        this.operator = operator;
        this.value = ValueNormalizer.normalize(value, valueType);
        this.valueType = valueType;
    }

    @Override
    public Condition toCondition(DSLContext dsl, OperatorFactory operatorFactory) {
        RunConditionOperator op = operatorFactory.resolveRunCondition(operator, valueType.getClazz());
        return op.evaluate(placeholder,valueType.getClazz().cast(value));
    }

    public void validate() {
        if (placeholder == null || placeholder.trim().isEmpty()) {
            throw new InvalidQueryException("Placeholder cannot be null or empty");
        }
        if (operator == null || operator.trim().isEmpty()) {
            throw new InvalidQueryException("Operator cannot be null or empty");
        }
        if (valueType == null || valueType.getClazz().getSimpleName().trim().isEmpty()) {
            throw new InvalidQueryException("Value type cannot be null or empty");
        }
    }
}
