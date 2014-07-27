package com.dafttech.eventmanager.cache;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import com.dafttech.reflect.ReflectionUtil;

public class AnnotatedConstructorCache extends AnnotatedElementCache<Object, Constructor<?>> {
    protected final Class<?> returnType;
    protected final Class<?>[] parameterTypes;
    protected final Object[] nullArgArray;

    public AnnotatedConstructorCache(Constructor<?> element, Object access) {
        super(Constructor.class, element, Modifier.isStatic(element.getModifiers()), access);
        returnType = element.getDeclaringClass();
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

    @Override
    public Class<?> getReturnType() {
        return returnType;
    }

    @Override
    public Object getValue(Object... args) throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        return element.newInstance(args);
    }
}
