package com.dafttech.type;

import java.lang.reflect.Field;

public class TypeDouble extends TypePrimitive<Double> {
    private static Field valueField = Type.getDeclaredField(Double.class, "value");

    public TypeDouble(Double value) {
        super(value);
    }

    @Override
    public int getSize() {
        return Double.SIZE;
    }

    @Override
    public Object getNullObject() {
        return 0;
    }

    @Override
    public long toLong() {
        return Double.doubleToLongBits(value);
    }

    @Override
    public Double fromLong(long val) {
        return Double.longBitsToDouble(val);
    }

    @Override
    protected Field getValueField() {
        return valueField;
    }

    @Override
    public Class<?> getTypeClass() {
        return Double.class;
    }

    @Override
    public Class<?> getPrimitiveClass() {
        return double.class;
    }
}
