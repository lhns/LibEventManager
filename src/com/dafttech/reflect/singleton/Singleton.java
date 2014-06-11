package com.dafttech.reflect.singleton;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.dafttech.reflect.Reflector;

public class Singleton extends Reflector {
    public Singleton(Class<?> target) {
        super(target);
    }

    /**
     * REQUIRES THE CLASS TO HAVE EITHER A FIELD OR A METHOD WITH THE INSTANCE
     * ANNOTATED WITH THE @INSTANCE ANNOTATION
     * 
     * @param methodArgs
     *            Object... - The arguments if you get the instance from a
     *            method
     * @return T - The instance already casted to the targetClass
     */
    @SuppressWarnings("unchecked")
    public final <ClassType> ClassType getInstance(Object... methodArgs) {
        for (Field field : getAnnotatedFields(SingletonInstance.class, null)) {
            if (!Modifier.isStatic(field.getModifiers())) continue;
            try {
                return (ClassType) field.get(null);
            } catch (Exception e) {
            }
        }
        for (Method method : getAnnotatedMethods(SingletonInstance.class, null)) {
            if (!Modifier.isStatic(method.getModifiers())) continue;
            try {
                return (ClassType) method.invoke(null, methodArgs);
            } catch (Exception e) {
            }
        }
        return null;
    }
}
