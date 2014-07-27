package com.dafttech.annotation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.dafttech.reflect.ReflectionUtil;

public class AnnotatedMethodCache<R> extends AnnotatedElementCache<R, Method> {
    protected final Class<?> returnType;
    protected final Class<?>[] parameterTypes;
    protected final Object[] nullArgArray;

    public AnnotatedMethodCache(Method element, Object access) {
        super(Method.class, element, Modifier.isStatic(element.getModifiers()), access);
        returnType = element.getReturnType();
        parameterTypes = element.getParameterTypes();
        nullArgArray = ReflectionUtil.buildArgumentArray(parameterTypes);
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    @Override
    public Object[] getNullArgArray() {
        return nullArgArray;
    }

    @SuppressWarnings("unchecked")
    @Override
    public R get(Object... args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return (R) element.invoke(accessInstance, args);
    }

    @Override
    public Class<?> getReturnType() {
        return returnType;
    }
}
