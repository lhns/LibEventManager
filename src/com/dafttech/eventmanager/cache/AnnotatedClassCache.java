package com.dafttech.eventmanager.cache;

public class AnnotatedClassCache extends AnnotatedElementCache<Class<?>, Class<?>> {

    public AnnotatedClassCache(Class<?> element, Object access) {
        super(Class.class, element, true, access);
    }

    @Override
    public Class<?> getReturnType() {
        return Class.class;
    }

    @Override
    public Class<?> getValue(Object... args) {
        return element;
    }
}
