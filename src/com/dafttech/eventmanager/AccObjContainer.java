package com.dafttech.eventmanager;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.dafttech.util.ReflectionUtil;

class AccObjContainer<Type extends AccessibleObject> {
    volatile protected Type target;
    volatile protected Class<?> targetClass;
    volatile protected Object targetInstance;
    volatile protected Class<?> type;
    volatile private int typeVal;
    volatile protected boolean isStatic;
    volatile protected Class<?> retType;
    volatile protected Class<?>[] argTypes;
    volatile protected Object[] nullArgs;

    protected AccObjContainer(Type target, Object access) {
        this.target = target;
        if (access == null) {
            return;
        } else if (access.getClass() == Class.class) {
            targetClass = (Class<?>) access;
            targetInstance = null;
        } else {
            targetClass = access.getClass();
            targetInstance = access;
        }

        type = target.getClass();
        int modifiers;
        if (Field.class.isAssignableFrom(type)) {
            Field field = (Field) target;
            modifiers = field.getModifiers();
            typeVal = 0;
            retType = field.getType();
            argTypes = new Class<?>[0];
        } else if (Method.class.isAssignableFrom(type)) {
            Method method = (Method) target;
            modifiers = method.getModifiers();
            typeVal = 1;
            retType = method.getReturnType();
            argTypes = method.getParameterTypes();

        } else if (Constructor.class.isAssignableFrom(type)) {
            Constructor<?> constructor = (Constructor<?>) target;
            modifiers = constructor.getModifiers();
            typeVal = 2;
            retType = constructor.getDeclaringClass();
            argTypes = constructor.getParameterTypes();
        } else {
            modifiers = 0;
        }

        isStatic = Modifier.isStatic(modifiers);

        if (!isStatic && targetInstance == null)
            throw new IllegalArgumentException("Instance of non-static AccessObject cannot be null!");

        nullArgs = ReflectionUtil.buildArgumentArray(argTypes);
    }

    protected AccObjContainer(Type target, Class<?> targetClass, Object targetInstance) {
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

    public Object access(Object... params) {
        return null;
    }
}
