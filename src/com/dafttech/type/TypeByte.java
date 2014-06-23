package com.dafttech.type;

import java.lang.reflect.Field;

public class TypeByte extends TypePrimitive<Byte> {
    private static Field valueField = Type.getDeclaredField(Byte.class, "value");

    protected TypeByte(boolean prototype) {
        super(prototype);
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

    @Override
    @SuppressWarnings("unchecked")
    public <ReturnType extends Type<Byte>> ReturnType create(Object obj) {
        if (obj != null && !getTypeClass().isAssignableFrom(obj.getClass())) return null;
        ReturnType type = (ReturnType) new TypeByte(false);
        type.value = (Byte) obj;
        return type;
    }
}
