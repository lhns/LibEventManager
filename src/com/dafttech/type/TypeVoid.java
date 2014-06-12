package com.dafttech.type;

public class TypeVoid extends Type<Void> {

    public TypeVoid() {
        super(null);
    }

    @Override
    public byte[] toByteArray() {
        return new byte[0];
    }

    @Override
    public Void fromByteArray(byte... array) {
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

}
