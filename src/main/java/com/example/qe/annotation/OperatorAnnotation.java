package com.example.qe.annotation;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface OperatorAnnotation {

    String value();
    Class<?>[] types();
    String description() default "";
}
