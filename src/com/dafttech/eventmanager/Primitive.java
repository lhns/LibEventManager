package com.dafttech.eventmanager;

import java.util.HashMap;
import java.util.Map;

public class Primitive {
    private static final Map<Class<?>, Primitive> primitives = new HashMap<Class<?>, Primitive>();

    static {
        primitives.put(byte.class, new Primitive(Byte.class));
        primitives.put(short.class, new Primitive(Short.class));
        primitives.put(int.class, new Primitive(Integer.class));
        primitives.put(long.class, new Primitive(Long.class));
        primitives.put(float.class, new Primitive(Float.class));
        primitives.put(double.class, new Primitive(Double.class));
        primitives.put(boolean.class, new Primitive(Boolean.class, false));
        primitives.put(char.class, new Primitive(Character.class, '\u0000'));
    }

    public static final Primitive get(Class<?> primitiveClass) {
        return primitives.get(primitiveClass);
    }

    private final Class<?> objectClass;
    private final Object nullValue;

    private Primitive(Class<?> objectClass, Object nullValue) {
        this.objectClass = objectClass;
        this.nullValue = nullValue;
    }

    private Primitive(Class<?> objectClass) {
        this(objectClass, 0);
    }

    public final Class<?> getObjectClass() {
        return objectClass;
    }

    public final Object getNullValue() {
        return nullValue;
    }
}
