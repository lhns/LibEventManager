package com.dafttech.type;

import java.lang.reflect.Field;

public class TypeFloat extends TypePrimitive<Float> {
    private static Field valueField = Type.getDeclaredField(Float.class, "value");

    public TypeFloat(Float value) {
        super(value);
    }

    @Override
    public int getSize() {
        return Float.SIZE;
    }

    @Override
    public Object getNullObject() {
        return 0;
    }

    @Override
    public long toLong() {
        return new Integer(Float.floatToIntBits(value)).longValue();
    }

    @Override
    public Float fromLong(long val) {
        return Float.intBitsToFloat(new Long(val).intValue());
    }

    @Override
    protected Field getValueField() {
        return valueField;
    }

    @Override
    public Class<?> getTypeClass() {
        return Float.class;
    }

    @Override
    public Class<?> getPrimitiveClass() {
        return float.class;
    }

}
