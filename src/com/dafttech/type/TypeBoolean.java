package com.dafttech.type;

import java.lang.reflect.Field;

public class TypeBoolean extends TypePrimitive<Boolean> {
    private static Field valueField = Type.getDeclaredField(Boolean.class, "value");

    protected TypeBoolean(boolean prototype) {
        super(prototype);
    }

    @Override
    public int getSize() {
        return Byte.SIZE;
    }

    @Override
    public long toLong() {
        return value ? 1 : 0;
    }

    @Override
    public Boolean fromLong(long val) {
        return val > 0;
    }

    @Override
    protected Field getValueField() {
        return valueField;
    }

    @Override
    public Class<?> getPrimitiveClass() {
        return boolean.class;
    }

    @Override
    public Class<?> getTypeClass() {
        return Boolean.class;
    }

    @Override
    public Object getNullObject() {
        return false;
    }
}
