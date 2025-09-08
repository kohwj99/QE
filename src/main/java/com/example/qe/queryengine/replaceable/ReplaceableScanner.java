package com.example.qe.queryengine.replaceable;

import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import java.util.Set;

@Slf4j
@Component
public class ReplaceableScanner {

    final static String REPLACEABLE_BASE_PACKAGE = "com.example.qe.queryengine.replaceable.impl";

    private final ReplaceableRegistry registry;

    public ReplaceableScanner(ReplaceableRegistry registry) {
        this.registry = registry;
    }

    @PostConstruct
    public void scanAndRegister() {
        Reflections reflections = new Reflections(REPLACEABLE_BASE_PACKAGE);
        Set<Class<?>> replaceableClasses = reflections.getTypesAnnotatedWith(ReplaceableAnnotation.class);

        for (Class<?> clazz : replaceableClasses) {
            ReplaceableAnnotation ann = clazz.getAnnotation(ReplaceableAnnotation.class);
            if (Replaceable.class.isAssignableFrom(clazz)) {
                registerReplaceable(clazz, ann.value());
            }
        }
        log.debug("Completed replaceable scanning and registration. Registered replaceables: {}", registry.getAll().size());
    }

    private <T extends Replaceable> void registerReplaceable(Class<?> clazz, String placeholder) {
        registry.register(placeholder, clazz.asSubclass(Replaceable.class));
    }
}
