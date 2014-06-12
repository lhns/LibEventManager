package com.dafttech.type;

import java.lang.reflect.Field;

public class TypeShort extends TypePrimitive<Short> {
    private static Field valueField = Type.getDeclaredField(Short.class, "value");

    public TypeShort(Short value) {
        super(value);
    }

    @Override
    public int getSize() {
        return Short.SIZE;
    }

    @Override
    public Object getNullObject() {
        return 0;
    }

    @Override
    public long toLong() {
        return value.longValue();
    }

    @Override
    public Short fromLong(long val) {
        return new Long(val).shortValue();
    }

    @Override
    protected Field getValueField() {
        return valueField;
    }

    @Override
    public Class<?> getTypeClass() {
        return Short.class;
    }

    @Override
    public Class<?> getPrimitiveClass() {
        return short.class;
    }

}
