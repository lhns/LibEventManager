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
}
