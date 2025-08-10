package com.example.qe.model.query;
import com.example.qe.model.query.impl.*;
import com.example.qe.util.QueryExecutionContext;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.jooq.Condition;
import org.jooq.DSLContext;

/**
 * Represents a query that can be converted to a JOOQ Condition.
 * This interface is used to define the contract for all query types.
 */

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AndQuery.class, name = "AndQuery"),
        @JsonSubTypes.Type(value = OrQuery.class, name = "OrQuery"),
        @JsonSubTypes.Type(value = IntQuery.class, name = "IntQuery"),
        @JsonSubTypes.Type(value = StringQuery.class, name = "StringQuery"),
        @JsonSubTypes.Type(value = DateQuery.class, name = "DateQuery"),
        @JsonSubTypes.Type(value = BoolQuery.class, name = "BoolQuery")
})
public interface Query {

    Condition toCondition(DSLContext dsl, QueryExecutionContext context);

}
