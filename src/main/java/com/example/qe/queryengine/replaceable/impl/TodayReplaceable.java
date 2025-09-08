package com.example.qe.queryengine.replaceable.impl;

import com.example.qe.queryengine.query.QueryContextDto;
import com.example.qe.queryengine.replaceable.Replaceable;
import com.example.qe.queryengine.replaceable.ReplaceableAnnotation;

import java.time.LocalDate;

@ReplaceableAnnotation(value = "[today]", description = "Replaces with the current LocalDate in ISO format (yyyy-MM-dd), or spoof date if provided")
public class TodayReplaceable implements Replaceable {
    @Override
    public String resolve(QueryContextDto context) {
        return (context.getSpoofDate() != null)
                ? context.getSpoofDate()
                : LocalDate.now().toString();
    }
}