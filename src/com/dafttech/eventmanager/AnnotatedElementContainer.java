package com.dafttech.eventmanager;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.NoSuchElementException;

import com.dafttech.hash.HashUtil;
import com.dafttech.reflect.ReflectionUtil;

class AnnotatedElementContainer<Type extends AnnotatedElement> {
    protected final Type target;
    protected final Class<?> targetClass;
    protected final Object targetInstance;
    protected final Class<?> type;
    protected final int typeVal;
    protected final boolean isStatic;
    protected final Class<?> retType;
    protected final Class<?>[] argTypes;
    protected final Object[] nullArgs;

    protected AnnotatedElementContainer(Type target, Object access) {
        this.target = target;
        if (access == null) {
            throw new NullPointerException();
        } else if (access.getClass() == Class.class) {
            targetClass = (Class<?>) access;
            targetInstance = null;
        } else {
            targetClass = access.getClass();
            targetInstance = access;
        }

        type = target.getClass();
        if (Field.class.isAssignableFrom(type)) {
            Field field = (Field) target;
            isStatic = Modifier.isStatic(field.getModifiers());
            typeVal = 0;
            retType = field.getType();
            argTypes = new Class<?>[0];
        } else if (Method.class.isAssignableFrom(type)) {
            Method method = (Method) target;
            isStatic = Modifier.isStatic(method.getModifiers());
            typeVal = 1;
            retType = method.getReturnType();
            argTypes = method.getParameterTypes();

        } else if (Constructor.class.isAssignableFrom(type)) {
            Constructor<?> constructor = (Constructor<?>) target;
            isStatic = true;
            typeVal = 2;
            retType = constructor.getDeclaringClass();
            argTypes = constructor.getParameterTypes();
        } else if (type == Class.class) {
            isStatic = true;
            typeVal = 3;
            retType = null;
            argTypes = new Class<?>[0];
        } else {
            throw new NoSuchElementException();
        }

        if (!isStatic && targetInstance == null)
            throw new IllegalArgumentException("Instance of non-static AccessObject cannot be null!");

        nullArgs = ReflectionUtil.buildArgumentArray(argTypes);
    }

    protected AnnotatedElementContainer(Type target, Class<?> targetClass, Object targetInstance) {
        this(target, targetInstance == null ? targetClass : targetInstance);
    }

    public boolean isField() {
        return typeVal == 0;
    }

    public boolean isMethod() {
        return typeVal == 1;
    }

    public boolean isConstructor() {
        return typeVal == 2;
    }

    public boolean isClass() {
        return typeVal == 3;
    }

    public Type getTarget() {
        return target;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Object getTargetInstance() {
        return targetInstance;
    }

    public Class<?> getType() {
        return type;
    }

    public boolean isStatic() {
        return isStatic;
    }

    @Override
    public int hashCode() {
        return HashUtil.hashCode(target, isStatic, targetClass, targetInstance);
    }

    @Override
    public boolean equals(Object obj) {
        return HashUtil.equals(this, obj);
    }
}
