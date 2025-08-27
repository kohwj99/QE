package com.example.qe.queryengine.operator;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface OperatorAnnotation {

    String value();
    Class<?>[] supportedFieldTypes();
    Class<?>[] supportedValueTypes();
    String description() default "";
}
