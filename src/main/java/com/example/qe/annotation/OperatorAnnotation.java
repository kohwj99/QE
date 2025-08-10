package com.example.qe.annotation;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)   // Keep at runtime for reflection scanning
@Target(ElementType.TYPE)            // Can only annotate classes
@Documented
public @interface OperatorAnnotation {

    /**
     * The operator name, e.g. "equals", "like", "greaterThan".
     */
    String value();

    /**
     * The Java types this operator supports, e.g. String.class, Integer.class.
     */
    Class<?>[] types();

    /**
     * Optional description of what this operator does.
     */
    String description() default "";
}
