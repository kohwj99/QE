package com.example.qe.model.query;

import com.example.qe.model.operator.GenericOperator;
import com.example.qe.util.QueryExecutionContext;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import static org.jooq.impl.DSL.field;

public abstract class FieldQuery<T> implements Query {

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
    public Condition toCondition(DSLContext dsl, QueryExecutionContext context) {
        // Get the jOOQ field with the right type
        Field<T> field = field(column, getValueClass());

        // Resolve operator from the factory
        GenericOperator<T> op = context.resolveOperator(operator, getValueClass());

        if (op == null) {
            throw new IllegalArgumentException("Unknown operator: " + operator);
        }

        // Apply operator to generate condition
        return op.apply(field, value);
    }

    protected abstract Class<T> getValueClass();

    // Getters and setters (needed for JSON deserialization)
    public String getColumn() { return column; }
    public void setColumn(String column) { this.column = column; }

    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }

    public T getValue() { return value; }
    public void setValue(T value) { this.value = value; }
}
