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

    protected FieldQuery() {
        // Default constructor for JSON deserialization
    }

    protected FieldQuery(String column, String operator, Object value) {
        this.column = column;
        this.operator = operator;
        this.value = value;
    }

    @Override
    public Condition toCondition(DSLContext dsl, OperatorFactory operatorFactory) {
        Field<?> field = field(column, getFieldClass());
        Class<?> valueType = operatorFactory.resolveValueType(operator, getFieldClass());
        GenericOperator op = operatorFactory.resolve(operator, getFieldClass(), valueType);

        if (op == null) {
            throw new IllegalArgumentException("Unknown operator: " + operator);
        }

        return op.apply(field, valueType.cast(value));
    }

    protected abstract Class<?> getFieldClass();

}
