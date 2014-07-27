package com.dafttech.annotation;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;

public class CachedAnnotatedElementContainer<R, T extends AnnotatedElement> {
    protected final AnnotatedElementCache<R, T> annotatedElementCache;

    public CachedAnnotatedElementContainer(AnnotatedElementCache<R, T> annotatedElementCache) {
        this.annotatedElementCache = annotatedElementCache;
    }

    public T getElement() {
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

    public R get(Object... args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException,
            InstantiationException {
        return annotatedElementCache.get(args);
    }
}
