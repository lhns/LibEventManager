package com.dafttech.primitives;

import java.util.ArrayList;
import java.util.List;

public class Primitive {
    private static final List<Primitive> primitives = new ArrayList<Primitive>();

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
        int index = primitives.indexOf(primitiveClass);
        if (index >= 0) return primitives.get(index);
        return null;
    }

    private final Class<?> primitiveClass;
    private final Class<?> objectClass;
    private final int size;
    private final Object nullValue;

    private Primitive(Class<?> primitiveClass, Class<?> objectClass, int size, Object nullValue) {
        this.primitiveClass = primitiveClass;
        this.objectClass = objectClass;
        this.size = size;
        this.nullValue = nullValue;
        primitives.add(this);
    }

    public final Class<?> getPrimitiveClass() {
        return primitiveClass;
    }

    public final Class<?> getObjectClass() {
        return objectClass;
    }

    public final int getSize() {
        return size;
    }

    public final Object getNullValue() {
        return nullValue;
    }

    @Override
    public final boolean equals(Object obj) {
        return obj == this || obj == primitiveClass || obj == objectClass || primitiveClass.isInstance(obj);
    }
}
