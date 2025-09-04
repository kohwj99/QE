package com.example.qe.queryengine;

import com.example.qe.queryengine.exception.QueryEngineException;
import com.example.qe.queryengine.operator.ConditionParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueryEngineService {


    private final DSLContext dsl;
//    private final ReplacementService replacementService;

    @Autowired
    ConditionParser conditionParser;

    public QueryEngineService(DSLContext dsl) {
//                                ReplacementService replacementService) {
        this.dsl = dsl;
    }


    public Result<Record> executeQuery(String tableName, String json) throws QueryEngineException {
        try {
            Condition condition = conditionParser.parseJsonToCondition(json);

            return dsl.select()
                    .from(tableName)
                    .where(condition)
                    .fetch();
        } catch (JsonProcessingException e) {
            throw new QueryEngineException(e.getMessage(), e.getCause());
        }
    }
}
