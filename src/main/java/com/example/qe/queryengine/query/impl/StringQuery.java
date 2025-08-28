package com.example.qe.queryengine.query.impl;

//import com.example.qe.queryengine.operator.CustomOperator;
//import com.example.qe.queryengine.operator.GenericOperator;
//import com.example.qe.queryengine.operator.OperatorFactory;
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
                       @JsonProperty("operator") String operator,
                       @JsonProperty("value") String value) {
        super(column, operator, value);
    }

    @Override
    protected Class<?> getFieldClass() {
        return String.class;
    }
}