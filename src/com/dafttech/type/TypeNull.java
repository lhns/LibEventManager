package com.dafttech.type;

public class TypeNull extends Type<Void> {

    protected TypeNull(boolean prototype) {
        super(prototype);
    }

    @Override
    public byte[] toByteArray() {
        return new byte[0];
    }

    @Override
    public TypeNull fromByteArray(byte... array) {
        return null;
    }

    @Override
    public Class<?> getTypeClass() {
        return null;
    }

    @Override
    public Object getNullObject() {
        return null;
    }

    @Override
    public boolean isType(Object obj) {
        return obj == null;
    }
}
