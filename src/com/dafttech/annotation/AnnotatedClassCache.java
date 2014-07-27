package com.dafttech.annotation;

public class AnnotatedClassCache extends AnnotatedElementCache<Class<?>, Class<?>> {

    public AnnotatedClassCache(Class<?> element, Object access) {
        super(Class.class, element, true, access);
    }

    @Override
    public Class<?> getReturnType() {
        return Class.class;
    }

    @Override
    public Class<?> get(Object... args) {
        return element;
    }
}
