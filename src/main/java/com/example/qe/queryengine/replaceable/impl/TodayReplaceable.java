package com.example.qe.queryengine.replaceable.impl;

import com.example.qe.queryengine.replaceable.Replaceable;
import com.example.qe.queryengine.replaceable.ReplaceableAnnotation;

import java.time.LocalDate;

@ReplaceableAnnotation(value = "[today]", description =  "Replaces with the current LocalDate in ISO format (yyyy-MM-dd)")
public class TodayReplaceable implements Replaceable {
    @Override
    public String resolve(String rawValue) {
        LocalDate today = LocalDate.now();
        return today.toString();
    }
}
