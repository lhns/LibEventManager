package com.dafttech.primitive;

import java.util.ArrayList;
import java.util.List;

public class Primitive<PrimitiveClass> {
    private static final List<Primitive<?>> primitives = new ArrayList<Primitive<?>>();

    public static final Primitive<Byte> BYTE = new Primitive<Byte>(byte.class, Byte.class, 1, 0);
    public static final Primitive<Short> SHORT = new Primitive<Short>(short.class, Short.class, 2, 0);
    public static final Primitive<Integer> INT = new Primitive<Integer>(int.class, Integer.class, 4, 0);
    public static final Primitive<Long> LONG = new Primitive<Long>(long.class, Long.class, 8, 0);
    public static final Primitive<Float> FLOAT = new Primitive<Float>(float.class, Float.class, 4, 0);
    public static final Primitive<Double> DOUBLE = new Primitive<Double>(double.class, Double.class, 8, 0);
    public static final Primitive<Boolean> BOOLEAN = new Primitive<Boolean>(boolean.class, Boolean.class, 1, false);
    public static final Primitive<Character> CHAR = new Primitive<Character>(char.class, Character.class, 2, '\u0000');
    public static final Primitive<Void> VOID = new Primitive<Void>(void.class, Void.class, 0, null);

    public static final Primitive<?> get(Class<?> classArg) {
        for (Primitive<?> primitive : primitives)
            if (primitive.primitiveClass == classArg || primitive.objectClass == classArg) return primitive;
        return null;
    }

    private final Class<PrimitiveClass> primitiveClass;
    private final Class<PrimitiveClass> objectClass;
    private final int size;
    private final Object nullValue;

    private Primitive(Class<PrimitiveClass> primitiveClass, Class<PrimitiveClass> objectClass, int size, Object nullValue) {
        this.primitiveClass = primitiveClass;
        this.objectClass = objectClass;
        this.size = size;
        this.nullValue = nullValue;
        primitives.add(this);
    }

    public final Class<PrimitiveClass> getPrimitiveClass() {
        return primitiveClass;
    }

    public final Class<PrimitiveClass> getObjectClass() {
        return objectClass;
    }

    public final int getSize() {
        return size;
    }

    public final Object getNullValue() {
        return nullValue;
    }

    public final Object[] toPrimitiveArray(Object[] objectArray) {
        Object[] primitiveArray = new Object[objectArray.length];
        for (int i = 0; i < objectArray.length; i++) {
            primitiveArray[i] = primitiveClass.cast(objectArray[i]);
        }
        return primitiveArray;
    }

    public final Object[] toObjectArray(Object[] primitiveArray) {
        Object[] objectArray = new Object[primitiveArray.length];
        for (int i = 0; i < primitiveArray.length; i++) {
            objectArray[i] = primitiveClass.cast(primitiveArray[i]);
        }
        return objectArray;
    }

    public final byte[] toByteArray(PrimitiveClass obj) {
        long value = toLong(obj);
        byte[] array = new byte[size];
        for (int i = 0; i < size; i++)
            array[i] = (byte) (value >> (size - 1 - i) * 8);
        return array;
    }

    public final PrimitiveClass fromByteArray(byte... array) {
        return fromByteArray(array, 0);
    }

    public final PrimitiveClass fromByteArray(byte[] array, int offset) {
        long value = 0;
        for (int i = 0; i < size; i++)
            value = value | (array[i + offset] & 0xFF) << (size - 1 - i) * 8;
        return fromLong(value);
    }

    public final long toLong(PrimitiveClass obj) {
        long value = 0;
        if (primitiveClass == float.class || primitiveClass == double.class) {
            value = Double.doubleToLongBits(Double.parseDouble(obj.toString()));
        } else if (primitiveClass == boolean.class) {
            value = (Boolean) obj ? 1 : 0;
        } else {
            value = Long.parseLong(obj.toString());
        }
        return value;
    }

    public final PrimitiveClass fromLong(long value) {
        Object obj;
        if (primitiveClass == float.class || primitiveClass == double.class) {
            obj = Double.longBitsToDouble(value);
        } else {
            obj = value;
        }
        return fromObject(obj);
    }

    @SuppressWarnings("unchecked")
    public final PrimitiveClass fromObject(Object obj) {
        String str = obj.toString();
        if (primitiveClass == int.class) {
            obj = Integer.parseInt(str);
        } else if (primitiveClass == long.class) {
            obj = Long.parseLong(str);
        } else if (primitiveClass == float.class) {
            obj = Float.parseFloat(str);
        } else if (primitiveClass == double.class) {
            obj = Double.parseDouble(str);
        } else if (primitiveClass == boolean.class) {
            obj = str.toLowerCase().equals("true") || str.equals(1);
        } else if (primitiveClass == short.class) {
            obj = Short.parseShort(str);
        } else if (primitiveClass == char.class) {
            obj = (char) Short.parseShort(str);
        } else if (primitiveClass == byte.class) {
            obj = Byte.parseByte(str);
        }
        return (PrimitiveClass) obj;
    }

    @Override
    public final boolean equals(Object obj) {
        return obj == this || obj == primitiveClass || obj == objectClass || primitiveClass.isInstance(obj);
    }
}
