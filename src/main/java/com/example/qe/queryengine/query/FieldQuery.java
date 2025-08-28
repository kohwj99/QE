package com.example.qe.queryengine.query;

import com.example.qe.queryengine.operator.GenericOperator;
import com.example.qe.queryengine.operator.OperatorFactory;
import lombok.Getter;
import lombok.Setter;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import static org.jooq.impl.DSL.field;

@Setter
@Getter
public abstract class FieldQuery implements Query {

    // Getters and setters (needed for JSON deserialization)
    protected String column;
    protected String operator;
    protected Object value; // Nullable for IS NULL / IS NOT NULL
    protected ValueType valueType;

    protected FieldQuery() {
        // Default constructor for JSON deserialization
    }

    protected FieldQuery(String column, String operator, Object value, ValueType valueType) {
        this.column = column;
        this.operator = operator;
        this.value = value;
        this.valueType = valueType;
    }

    @Override
    public Condition toCondition(DSLContext dsl, OperatorFactory operatorFactory) {
        Field<?> field = field(column, getFieldClass());
        Class<?> validValueType = operatorFactory.resolveValueType(operator, getFieldClass());

        // Perform type checking to enforce Operator compatibility
        if (!validValueType.equals(valueType.getClazz())) {
            throw new IllegalArgumentException("Value type " + valueType.name() + " is not supported for operator " + operator + " on field type " + getFieldClass().getName());
        }
        GenericOperator op = operatorFactory.resolve(operator, getFieldClass(), valueType.getClazz());
        if (op == null) {
            throw new IllegalArgumentException("Unknown operator: " + operator);
        }

        return op.apply(field, valueType.getClazz().cast(value));
    }

    protected abstract Class<?> getFieldClass();

}
