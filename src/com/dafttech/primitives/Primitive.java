package com.dafttech.primitives;

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
        long value = 0;
        if (primitiveClass == float.class || primitiveClass == double.class) {
            value = Double.doubleToLongBits(Double.parseDouble(obj.toString()));
        } else if (primitiveClass == boolean.class) {
            value = (Boolean) obj ? 1 : 0;
        } else {
            value = Long.parseLong(obj.toString());
        }
        byte[] array = new byte[size];
        for (int i = 0; i < size; i++)
            array[i] = (byte) (value >> (size - 1 - i) * 8);
        return array;
    }

    @SuppressWarnings("unchecked")
    public final PrimitiveClass fromByteArray(byte... array) {
        long value = 0;
        for (int i = 0; i < size; i++)
            value = value | (array[i] & 0xFF) << (size - 1 - i) * 8;
        Object obj = null;
        if (primitiveClass == float.class || primitiveClass == double.class) {
            obj = Double.longBitsToDouble(value);
            if (primitiveClass == float.class) obj = Float.parseFloat(obj.toString());
        } else if (primitiveClass == boolean.class) {
            obj = value != 0;
        } else {
            obj = value;
            if (primitiveClass == int.class) {
                obj = Integer.parseInt(obj.toString());
            } else if (primitiveClass == short.class) {
                obj = Short.parseShort(obj.toString());
            } else if (primitiveClass == char.class) {
                obj = (char) Short.parseShort(obj.toString());
            } else if (primitiveClass == byte.class) {
                obj = Byte.parseByte(obj.toString());
            }

        }
        return (PrimitiveClass) obj;
    }

    @Override
    public final boolean equals(Object obj) {
        return obj == this || obj == primitiveClass || obj == objectClass || primitiveClass.isInstance(obj);
    }
}
