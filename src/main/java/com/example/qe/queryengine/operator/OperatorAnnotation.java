package com.example.qe.queryengine.operator;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface OperatorAnnotation {

    String value();
    Class<?>[] types();
    String description() default "";
}
