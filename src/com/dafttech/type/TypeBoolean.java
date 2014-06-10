package com.dafttech.type;

public class TypeBoolean extends TypePrimitive<Boolean> {
    @Override
    public long toLong(Boolean val) {
        return 0;
    }

    @Override
    public Boolean fromLong(long val) {
        return null;
    }

    @Override
    public int getSize() {
        return 1;
    }

    @Override
    public Object getNullObject() {
        return false;
    }
}
