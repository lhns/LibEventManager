package com.dafttech.type;

public class TypeClass extends Type<Class<?>> {

    protected TypeClass(boolean prototype) {
        super(prototype);
    }

    @Override
    public byte[] toByteArray() {
        return null;
    }

    @Override
    public TypeClass fromByteArray(byte... array) {
        return null;
    }

    @Override
    public Class<?> getTypeClass() {
        return Class.class;
    }

    @Override
    public Object getNullObject() {
        return null;
    }
}
