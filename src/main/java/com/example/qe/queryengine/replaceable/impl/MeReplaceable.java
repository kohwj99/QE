package com.example.qe.queryengine.replaceable.impl;

import com.example.qe.queryengine.replaceable.Replaceable;
import com.example.qe.queryengine.replaceable.ReplaceableAnnotation;

@ReplaceableAnnotation(value = "[me]", description =  "Replaces with the current user's id")
public class MeReplaceable implements Replaceable {
    @Override
    public String resolve(String rawValue) {

        // logic to get the current user's id from action
        return "12345";
    }
}
