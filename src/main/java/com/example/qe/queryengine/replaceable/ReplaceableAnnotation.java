package com.example.qe.queryengine.replaceable;

import java.lang.annotation.*;
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface ReplaceableAnnotation {
    String value();
    String description() default "";
}
