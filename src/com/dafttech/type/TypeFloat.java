package com.dafttech.type;

public class TypeFloat extends TypePrimitive<Float> {
    @Override
    public long toLong(Float val) {
        return 0;
    }

    @Override
    public Float fromLong(long val) {
        return null;
    }

    @Override
    public int getSize() {
        return 4;
    }

    @Override
    public Object getNullObject() {
        return 0;
    }

}
