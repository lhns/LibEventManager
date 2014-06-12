package com.dafttech.type;

import java.lang.reflect.Field;

public class TypeVoid extends TypePrimitive<Void> {

    protected TypeVoid(boolean prototype) {
        super(prototype);
    }

    @Override
    public byte[] toByteArray() {
        return new byte[0];
    }

    @Override
    public TypeVoid fromByteArray(byte... array) {
        return null;
    }

    @Override
    public Class<?> getTypeClass() {
        return Void.class;
    }

    @Override
    public Object getNullObject() {
        return null;
    }

    @Override
    public boolean isType(Object obj) {
        return false;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public long toLong() {
        return 0;
    }

    @Override
    public Void fromLong(long val) {
        return null;
    }

    @Override
    protected Field getValueField() {
        return null;
    }

    @Override
    public void setValue(Void val) {
    }

    @Override
    public Class<?> getPrimitiveClass() {
        return void.class;
    }
}
