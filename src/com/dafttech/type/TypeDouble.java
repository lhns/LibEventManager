package com.dafttech.type;

public class TypeDouble extends TypePrimitive<Double> {
    @Override
    public long toLong(Double val) {
        return 0;
    }

    @Override
    public Double fromLong(long val) {
        return null;
    }

    @Override
    public int getSize() {
        return 8;
    }

    @Override
    public Object getNullObject() {
        return 0;
    }
}
