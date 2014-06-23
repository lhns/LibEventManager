package com.dafttech.type;

import java.lang.reflect.Field;

public class TypeShort extends TypePrimitive<Short> {
    private static Field valueField = Type.getDeclaredField(Short.class, "value");

    protected TypeShort(boolean prototype) {
        super(prototype);
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

    @Override
    @SuppressWarnings("unchecked")
    public <ReturnType extends Type<Short>> ReturnType create(Object obj) {
        if (obj != null && !getTypeClass().isAssignableFrom(obj.getClass())) return null;
        ReturnType type = (ReturnType) new TypeShort(false);
        type.value = (Short) obj;
        return type;
    }
}
