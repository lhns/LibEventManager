package com.dafttech.type;

public class TypeBoolean extends TypePrimitive<Boolean> {

    public TypeBoolean(Boolean value) {
        super(value);
        // TODO Auto-generated constructor stub
    }

    @Override
    public long toLong(Boolean val) {
        return 0;
    }

    @Override
    public Boolean fromLong(long val) {
        return null;
    }
}
