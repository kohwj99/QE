package com.example.qe.queryengine;

import com.example.qe.queryengine.exception.QueryEngineException;
import com.example.qe.queryengine.operator.ConditionParser;
import com.example.qe.queryengine.query.QueryContextDto;
import com.example.qe.queryengine.replaceable.ReplaceableResolver;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class QueryEngineService {

    private final DSLContext dsl;

    @Autowired
    ConditionParser conditionParser;

    @Autowired
    ReplaceableResolver replaceableResolver;

    public QueryEngineService(DSLContext dsl) {
        this.dsl = dsl;
    }

    public Result<Record> executeQuery(QueryContextDto context) {

        try {
            String replacedJson = replaceableResolver.processJsonPlaceholders(context);
            Condition condition = conditionParser.parseJsonToCondition(replacedJson);

            System.out.println(dsl.renderInlined(dsl.select().from(context.getTableName()).where(condition)));
            return dsl.select()
                    .from(context.getTableName())
                    .where(condition)
                    .fetch();

        } catch (JsonProcessingException e) {
            throw new QueryEngineException(e.getMessage(), e.getCause());
        }
    }

    public List<Map<String, Object>> executeQueryToDisplay(QueryContextDto context)  {

        try {
            String replacedJson = replaceableResolver.processJsonPlaceholders(context);
            Condition condition = conditionParser.parseJsonToCondition(replacedJson);

            System.out.println(dsl.renderInlined(dsl.select().from(context.getTableName()).where(condition)));
            Result<Record> result = dsl.select()
                    .from(context.getTableName())
                    .where(condition)
                    .fetch();

            return result.intoMaps();

        } catch (JsonProcessingException e) {
            throw new QueryEngineException(e.getMessage(), e.getCause());
        }
    }

}
