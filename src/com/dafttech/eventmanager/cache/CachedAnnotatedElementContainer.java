package com.dafttech.eventmanager.cache;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;

import com.dafttech.hash.HashUtil;

public class CachedAnnotatedElementContainer<T extends AnnotatedElement> {
    protected final AnnotatedElementCache<?, ?> annotatedElementCache;

    public CachedAnnotatedElementContainer(AnnotatedElement element, Object access) {
        this.annotatedElementCache = AnnotatedElementCache.getNewInstance(element, access);
    }

    public Object getElement() {
        return annotatedElementCache.getElement();
    }

    public Class<?> getType() {
        return annotatedElementCache.getType();
    }

    public boolean isStatic() {
        return annotatedElementCache.isStatic();
    }

    public Class<?> getAccessClass() {
        return annotatedElementCache.getAccessClass();
    }

    public Object getAccessInstance() {
        return annotatedElementCache.getAccessInstance();
    }

    public Object[] getNullArgArray() {
        return annotatedElementCache.getNullArgArray();
    }

    public Class<?> getReturnType() {
        return annotatedElementCache.getReturnType();
    }

    public Class<?>[] getParameterTypes() {
        return annotatedElementCache.getParameterTypes();
    }

    public Object getValue(Object... args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException,
            InstantiationException {
        return annotatedElementCache.getValue(args);
    }

    @Override
    public int hashCode() {
        return HashUtil.hashCode(annotatedElementCache);
    }

    @Override
    public boolean equals(Object target) {
        return HashUtil.equals(this, target);
    }
}
