package com.example.qe.queryengine.query.impl;

import com.example.qe.queryengine.operator.OperatorFactory;
import com.example.qe.queryengine.query.FieldQuery;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import static org.jooq.impl.DSL.field;

//public class StringQuery extends FieldQuery<String> {
//
//
//
//    @Override
//    protected Class<String> getValueClass() {
//        return String.class;
//    }
//}

@JsonTypeName("StringQuery")
public class StringQuery extends FieldQuery {


    @JsonCreator
    public StringQuery(@JsonProperty("column") String column,
                       @JsonProperty("operatorName") String operatorName,
                       @JsonProperty("value") String value) {
        super(column, operatorName, value);
    }

    @Override
    protected Class<?> getFieldClass() {
        return String.class;
    }

    @Override
    public Condition toCondition(DSLContext dsl, OperatorFactory operatorFactory) {
        Class<?> fieldType = getFieldClass();
        OperatorFactory.ResolvedOperator resolved = operatorFactory.resolveWithDynamicValueType(operator, fieldType);
        Field<?> jooqField = field(column, fieldType);
        Object castedValue = resolved.valueType().cast(value);
        return resolved.operator().apply(jooqField, castedValue);
    }
}