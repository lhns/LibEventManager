package com.dafttech.type;

import java.lang.reflect.Field;

public class TypeByte extends TypePrimitive<Byte> {
    private static Field valueField = Type.getDeclaredField(Byte.class, "value");

    public TypeByte(Byte value) {
        super(value);
    }

    @Override
    public int getSize() {
        return Byte.SIZE;
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
    public Byte fromLong(long val) {
        return new Long(val).byteValue();
    }

    @Override
    protected Field getValueField() {
        return valueField;
    }

    @Override
    public Class<?> getTypeClass() {
        return Byte.class;
    }

    @Override
    public Class<?> getPrimitiveClass() {
        return byte.class;
    }
}
