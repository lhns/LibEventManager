package com.dafttech.type;

public class TypeClass extends Type<Class<?>> {

    public TypeClass(Class<?> value) {
        super(value);
    }

    @Override
    public byte[] toByteArray() {
        return null;
    }

    @Override
    public Class<?> fromByteArray(byte... array) {
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
