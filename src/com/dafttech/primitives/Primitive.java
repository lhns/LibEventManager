package com.dafttech.primitives;

import java.util.HashMap;
import java.util.Map;

public class Primitive {
    private static final Map<Class<?>, Primitive> primitives = new HashMap<Class<?>, Primitive>();

    public static final Primitive BYTE = new Primitive(byte.class, Byte.class, 1, 0);
    public static final Primitive SHORT = new Primitive(short.class, Short.class, 2, 0);
    public static final Primitive INT = new Primitive(int.class, Integer.class, 4, 0);
    public static final Primitive LONG = new Primitive(long.class, Long.class, 8, 0);
    public static final Primitive FLOAT = new Primitive(float.class, Float.class, 4, 0);
    public static final Primitive DOUBLE = new Primitive(double.class, Double.class, 8, 0);
    public static final Primitive BOOLEAN = new Primitive(boolean.class, Boolean.class, 1, false);
    public static final Primitive CHAR = new Primitive(char.class, Character.class, 2, '\u0000');
    public static final Primitive VOID = new Primitive(void.class, Void.class, 0, null);

    public static final Primitive get(Class<?> primitiveClass) {
        return primitives.get(primitiveClass);
    }

    private final Class<?> objectClass;
    private final Object nullValue;
    private final int size;

    private Primitive(Class<?> primitiveClass, Class<?> objectClass, int size, Object nullValue) {
        this.objectClass = objectClass;
        this.nullValue = nullValue;
        this.size = size;
        primitives.put(primitiveClass, this);
    }

    public final Class<?> getObjectClass() {
        return objectClass;
    }

    public final Object getNullValue() {
        return nullValue;
    }

    public final int getSize() {
        return size;
    }
}
