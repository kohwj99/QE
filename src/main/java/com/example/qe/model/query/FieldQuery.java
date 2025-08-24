package com.example.qe.model.query;

import com.example.qe.model.operator.Operator;
import com.example.qe.util.OperatorFactory;
import lombok.Getter;
import lombok.Setter;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import static org.jooq.impl.DSL.field;

@Setter
@Getter
public abstract class FieldQuery<F, T> implements Query {

    // Getters and setters (needed for JSON deserialization)
    protected String column;
    protected String operator;
    protected T value; // Nullable for IS NULL / IS NOT NULL

    protected FieldQuery() {
        // Default constructor for JSON deserialization
    }

    protected FieldQuery(String column, String operator, T value) {
        this.column = column;
        this.operator = operator;
        this.value = value;
    }

    @Override
    public Condition toCondition(DSLContext dsl, OperatorFactory operatorFactory) {
        // Get the jOOQ field with the right type
        Field<F> field = field(column, getFieldClass());

        // Resolve operator from the factory directly and cast to correct type
        Operator<F, T> op;
        try {
            op = (Operator<F, T>) operatorFactory.resolve(operator, getValueClass());
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Operator type mismatch for: " + operator, e);
        }

        if (op == null) {
            throw new IllegalArgumentException("Unknown operator: " + operator);
        }

        // Apply operator to generate condition
        return op.apply(field, value);
    }

    protected abstract Class<F> getFieldClass();
    protected abstract Class<T> getValueClass();

}
