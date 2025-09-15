package com.example.qe.queryengine.query;

import com.example.qe.queryengine.exception.FieldTypeMismatchException;
import com.example.qe.queryengine.exception.InvalidQueryException;
import com.example.qe.queryengine.exception.OperatorNotFoundException;
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

    protected String column;
    protected String operator;
    protected Object value;
    protected ValueType valueType;

    protected FieldQuery() {
    }

    protected FieldQuery(String column, String operator, Object value, ValueType valueType) {
        this.column = column;
        this.operator = operator;
        this.value = ValueNormalizer.normalize(value, valueType);
        this.valueType = valueType;
    }

    @Override
    public Condition toCondition(DSLContext dsl, OperatorFactory operatorFactory) {
        Field<?> field = field(column, getFieldClass());
        Class<?> validValueType = operatorFactory.resolveValueType(operator, getFieldClass());

        // Perform type checking to enforce Operator compatibility
        if (!validValueType.equals(valueType.getClazz())) {
            throw new FieldTypeMismatchException("Value type " + valueType.name() + " is not supported for operator " + operator + " on field type " + getFieldClass().getName());
        }
        GenericOperator op = operatorFactory.resolve(operator, getFieldClass(), valueType.getClazz());
        if (op == null) {
            throw new OperatorNotFoundException("Unknown operator: " + operator);
        }

        // Perform type checking to ensure value is of the correct type
        if (value != null && !valueType.getClazz().isInstance(value)) {
            throw new InvalidQueryException("Value is not of the expected type: " + valueType.getClazz().getName());
        }

        return op.apply(field, valueType.getClazz().cast(value));
    }

    protected abstract Class<?> getFieldClass();

    public void validate() {
        if (column == null || column.trim().isEmpty()) {
            throw new InvalidQueryException("Column cannot be null or empty");
        }
        if (operator == null || operator.trim().isEmpty()) {
            throw new InvalidQueryException("Operator cannot be null or empty");
        }
        if (valueType == null || valueType.getClazz().getSimpleName().trim().isEmpty()) {
            throw new InvalidQueryException("Value type cannot be null or empty");
        }
    }
}
