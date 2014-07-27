package com.dafttech.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class AnnotatedFieldCache<R> extends AnnotatedElementCache<R, Field> {
    protected final Class<?> returnType;

    public AnnotatedFieldCache(Field element, Object access) {
        super(Field.class, element, Modifier.isStatic(element.getModifiers()), access);
        returnType = element.getType();
    }

    @Override
    public Class<?> getReturnType() {
        return returnType;
    }

    @SuppressWarnings("unchecked")
    @Override
    public R get(Object... args) throws IllegalArgumentException, IllegalAccessException {
        return (R) element.get(accessInstance);
    }
}
