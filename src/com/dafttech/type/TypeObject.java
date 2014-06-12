package com.dafttech.type;

public class TypeObject extends Type<Object> {

    protected TypeObject(boolean prototype) {
        super(prototype);
    }

    @Override
    public byte[] toByteArray() {
        return null;
    }

    @Override
    public TypeObject fromByteArray(byte... array) {
        return null;
    }

    @Override
    public Class<?> getTypeClass() {
        return Object.class;
    }

    @Override
    public Object getNullObject() {
        return null;
    }

    @Override
    public boolean isType(Object obj) {
        return obj != null;
    }

    @Override
    public TypeObject create(Object obj) {
        return (TypeObject) super.create(obj);
    }
}
