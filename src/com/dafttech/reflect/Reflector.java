package com.dafttech.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dafttech.primitive.Primitive;

public class Reflector {
    protected Class<?> target;
    protected boolean exception = false;

    public Reflector(Class<?> target) {
        this.target = target;
    }

    public Reflector showExceptions(boolean value) {
        exception = value;
        return this;
    }

    public final List<Method> getAnnotatedMethods(Class<? extends Annotation> annotation, Class<?> reqType, Class<?>... reqArgs) {
        List<Method> methods = new ArrayList<Method>();
        for (Method method : getAllDeclaredMethods()) {
            if (method.isAnnotationPresent(annotation)) {
                if ((reqType == null || method.getReturnType() == reqType)
                        && (reqArgs == null || reqArgs.length == 1 && reqArgs[0] == null || Arrays.equals(
                                method.getParameterTypes(), reqArgs))) {
                    methods.add(method);
                } else if (exception) {
                    String errorMessage = "\nat " + target.getName() + " at Annotation " + annotation.getName() + ":";
                    errorMessage += "\nexpected: " + reqType.getName() + " with " + (reqArgs.length == 0 ? "no args" : "args:");
                    for (Class<?> arg : reqArgs)
                        errorMessage += ", " + arg.getName();
                    errorMessage += "\nand got:  " + method.getReturnType() + " with "
                            + (method.getParameterTypes().length == 0 ? "no args" : "args:");
                    for (Class<?> arg : method.getParameterTypes())
                        errorMessage += ", " + arg.getName();
                    errorMessage += ".";
                    throw new IllegalArgumentException(errorMessage);
                }
            }
        }
        return methods;
    }

    public final List<Field> getAnnotatedFields(Class<? extends Annotation> annotation, Class<?> reqType) {
        List<Field> fields = new ArrayList<Field>();
        if (reqType == void.class) return fields;
        for (Field field : getAllDeclaredFields()) {
            if (field.isAnnotationPresent(annotation)) {
                if (reqType == null || field.getType() == reqType) {
                    fields.add(field);
                } else if (exception) {
                    String errorMessage = "\nat " + target.getName() + " at Annotation " + annotation.getName() + ":";
                    errorMessage += "\nexpected: " + reqType.getName();
                    errorMessage += "\nand got:  " + field.getType();
                    errorMessage += ".";
                    throw new IllegalArgumentException(errorMessage);
                }
            }
        }
        return fields;
    }

    public final List<Method> getAllDeclaredMethods() {
        List<Method> methods = new ArrayList<Method>();
        getAllDeclaredMethods(this, target, null);
        return methods;
    }

    private static final void getAllDeclaredMethods(Reflector reflector, Class<?> target, List<Method> methods) {
        if (methods == null) methods = new ArrayList<Method>();
        try {
            for (Method method : target.getDeclaredMethods())
                if (!methods.contains(method)) {
                    method.setAccessible(true);
                    methods.add(method);
                }
        } catch (NoClassDefFoundError e) {
            if (reflector.exception) e.printStackTrace();
        }
        Class<?> superclass = target.getSuperclass();
        if (superclass != null) getAllDeclaredMethods(reflector, superclass, methods);
    }

    public final List<Field> getAllDeclaredFields() {
        List<Field> fields = new ArrayList<Field>();
        getAllDeclaredFields(this, target, fields);
        return fields;
    }

    private static final void getAllDeclaredFields(Reflector reflector, Class<?> target, List<Field> fields) {
        try {
            for (Field field : target.getDeclaredFields())
                if (!fields.contains(field)) {
                    field.setAccessible(true);
                    fields.add(field);
                }
        } catch (NoClassDefFoundError e) {
            if (reflector.exception) e.printStackTrace();
        }
        Class<?> superclass = target.getSuperclass();
        if (superclass != null) getAllDeclaredFields(reflector, superclass, fields);
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
            if (args[i1] == null && argTypes[i1].isPrimitive()) args[i1] = Primitive.get(argTypes[i1]).getNullValue();
        }
        return args;
    }

    public Constructor<?> getConstructor() {
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
}
