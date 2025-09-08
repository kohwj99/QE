package com.example.qe.queryengine.replaceable;

import com.example.qe.queryengine.query.QueryContextDto;

@FunctionalInterface
public interface Replaceable {
    String resolve(QueryContextDto context);
}
