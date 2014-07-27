package com.dafttech.eventmanager.cache;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class AnnotatedFieldCache extends AnnotatedElementCache<Object, Field> {
    protected final Class<?> returnType;

    public AnnotatedFieldCache(Field element, Object access) {
        super(Field.class, element, Modifier.isStatic(element.getModifiers()), access);
        returnType = element.getType();
    }

    @Override
    public Class<?> getReturnType() {
        return returnType;
    }

    @Override
    public Object getValue(Object... args) throws IllegalArgumentException, IllegalAccessException {
        return element.get(accessInstance);
    }
}
