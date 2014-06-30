package com.dafttech.util;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectionUtil {
    public static final List<Method> getAnnotatedMethods(Class<?> target, Class<? extends Annotation> annotation,
            Class<?> reqType, Class<?>... reqArgs) {
        List<Method> methods = new ArrayList<Method>();
        for (Method method : getAllDeclaredMethods(target)) {
            if (!method.isAnnotationPresent(annotation)) continue;
            if ((reqType == null || method.getReturnType() == reqType)
                    && (reqArgs == null || reqArgs.length == 1 && reqArgs[0] == null || Arrays.equals(method.getParameterTypes(),
                            reqArgs))) {
                methods.add(method);
            }
        }
        return methods;
    }

    public static final List<Field> getAnnotatedFields(Class<?> target, Class<? extends Annotation> annotation, Class<?> reqType) {
        List<Field> fields = new ArrayList<Field>();
        if (reqType == void.class) return fields;
        for (Field field : getAllDeclaredFields(target)) {
            if (!field.isAnnotationPresent(annotation)) continue;
            if (reqType == null || field.getType() == reqType) {
                fields.add(field);
            }
        }
        return fields;
    }

    public static final List<Method> getAllDeclaredMethods(Class<?> target) {
        List<Method> methods = new ArrayList<Method>();
        getAllDeclaredMethods(target, methods);
        return methods;
    }

    private static final void getAllDeclaredMethods(Class<?> target, List<Method> methods) {
        if (methods == null) methods = new ArrayList<Method>();
        try {
            for (Method method : target.getDeclaredMethods())
                if (!methods.contains(method)) {
                    method.setAccessible(true);
                    methods.add(method);
                }
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
        }
        Class<?> superclass = target.getSuperclass();
        if (superclass != null) getAllDeclaredMethods(superclass, methods);
    }

    public static final List<Field> getAllDeclaredFields(Class<?> target) {
        List<Field> fields = new ArrayList<Field>();
        getAllDeclaredFields(target, fields);
        return fields;
    }

    private static final void getAllDeclaredFields(Class<?> target, List<Field> fields) {
        try {
            for (Field field : target.getDeclaredFields())
                if (!fields.contains(field)) {
                    field.setAccessible(true);
                    fields.add(field);
                }
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
        }
        Class<?> superclass = target.getSuperclass();
        if (superclass != null) getAllDeclaredFields(superclass, fields);
    }

    /**
     * Used to get an array of null objects (or false for boolean, 0 for
     * numbers, etc.) for dynamic instantiation. You can also insert objects for
     * specific types instead of null.
     * 
     * @param argTypes
     *            Class<?>[] - the argument Types
     * @param appliedArgs
     *            Object... - The arg types that are replaced with objects
     *            Example: ([Class<?>[] argTypes], String.class, "test")
     * @return Object[] - Array with null or objects (look above)
     */
    public static final Object[] buildArgumentArray(Class<?>[] argTypes, Object... appliedArgs) {
        Object[] args = new Object[argTypes.length];
        for (int i1 = 0; i1 < args.length; i1++) {
            for (int i2 = 0; i2 + 1 < appliedArgs.length; i2 += 2) {
                if (argTypes[i1] == appliedArgs[i2]) {
                    args[i1] = appliedArgs[i2 + 1];
                    break;
                }
            }
            if (args[i1] == null && argTypes[i1].isPrimitive()) args[i1] = PrimitiveUtil.get(argTypes[i1]).nullObj;

        }
        return args;
    }

    public Constructor<?> getConstructor(Class<?> target) {
        try {
            Constructor<?> constructor = target.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Target({ METHOD, FIELD })
    @Retention(RUNTIME)
    @Documented
    public static @interface SingletonInstance {
    }

    @SuppressWarnings("unchecked")
    public static final <ClassType> ClassType getSingletonInstance(Class<?> target, Object... appliedArgs) {
        for (Field field : getAnnotatedFields(target, SingletonInstance.class, null)) {
            if (!Modifier.isStatic(field.getModifiers())) continue;
            try {
                return (ClassType) field.get(null);
            } catch (Exception e) {
            }
        }
        for (Method method : getAnnotatedMethods(target, SingletonInstance.class, null, (Class<?>[]) null)) {
            if (!Modifier.isStatic(method.getModifiers())) continue;
            try {
                return (ClassType) method.invoke(null, buildArgumentArray(method.getParameterTypes(), appliedArgs));
            } catch (Exception e) {
            }
        }
        return null;
    }
}
