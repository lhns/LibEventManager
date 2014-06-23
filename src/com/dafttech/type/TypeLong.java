package com.dafttech.type;

import java.lang.reflect.Field;

public class TypeLong extends TypePrimitive<Long> {
    private static Field valueField = Type.getDeclaredField(Long.class, "value");

    protected TypeLong(boolean prototype) {
        super(prototype);
    }

    @Override
    public int getSize() {
        return Long.SIZE;
    }

    @Override
    public Object getNullObject() {
        return 0;
    }

    @Override
    public long toLong() {
        return value;
    }

    @Override
    public Long fromLong(long val) {
        return val;
    }

    @Override
    protected Field getValueField() {
        return valueField;
    }

    @Override
    public Class<?> getTypeClass() {
        return Long.class;
    }

    @Override
    public Class<?> getPrimitiveClass() {
        return long.class;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <ReturnType extends Type<Long>> ReturnType create(Object obj) {
        if (obj != null && !getTypeClass().isAssignableFrom(obj.getClass())) return null;
        ReturnType type = (ReturnType) new TypeLong(false);
        type.value = (Long) obj;
        return type;
    }
}
