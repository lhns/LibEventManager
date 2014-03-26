package com.dafttech.eventmanager;

import java.util.HashMap;
import java.util.Map;

public class Primitive {
    private static final Map<Class<?>, Primitive> primitives = new HashMap<Class<?>, Primitive>();

    static {
        new Primitive(byte.class, Byte.class, 0);
        new Primitive(short.class, Short.class, 0);
        new Primitive(int.class, Integer.class, 0);
        new Primitive(long.class, Long.class, 0);
        new Primitive(float.class, Float.class, 0);
        new Primitive(double.class, Double.class, 0);
        new Primitive(boolean.class, Boolean.class, false);
        new Primitive(char.class, Character.class, '\u0000');
        new Primitive(void.class, Void.class, null);
    }

    public static final Primitive get(Class<?> primitiveClass) {
        return primitives.get(primitiveClass);
    }

    private final Class<?> objectClass;
    private final Object nullValue;

    private Primitive(Class<?> primitiveClass, Class<?> objectClass, Object nullValue) {
        this.objectClass = objectClass;
        this.nullValue = nullValue;
        primitives.put(primitiveClass, this);
    }

    public final Class<?> getObjectClass() {
        return objectClass;
    }

    public final Object getNullValue() {
        return nullValue;
    }
}
