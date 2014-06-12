package com.dafttech.type;

public class TypeNull extends Type<Void> {

    public TypeNull() {
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

    @Override
    public Type<?> newInstance(Object obj) {
        return new TypeNull();
    }
}
