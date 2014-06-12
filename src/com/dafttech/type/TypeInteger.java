package com.dafttech.type;

import java.lang.reflect.Field;

public class TypeInteger extends TypePrimitive<Integer> {
    private static Field valueField = Type.getDeclaredField(Integer.class, "value");

    protected TypeInteger(boolean prototype) {
        super(prototype);
    }

    @Override
    public long toLong() {
        return value.longValue();
    }

    @Override
    public Integer fromLong(long value) {
        return new Long(value).intValue();
    }

    @Override
    public int getSize() {
        return Integer.SIZE;
    }

    @Override
    public Object getNullObject() {
        return 0;
    }

    @Override
    protected Field getValueField() {
        return valueField;
    }

    @Override
    public Class<?> getTypeClass() {
        return Integer.class;
    }

    @Override
    public Class<?> getPrimitiveClass() {
        return int.class;
    }

}
