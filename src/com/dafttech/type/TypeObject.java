package com.dafttech.type;

public class TypeObject extends Type<Object> {

    public TypeObject(Object value) {
        super(value);
    }

    @Override
    public byte[] toByteArray() {
        return null;
    }

    @Override
    public Object fromByteArray(byte... array) {
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
}
