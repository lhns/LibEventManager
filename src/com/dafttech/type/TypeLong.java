package com.dafttech.type;

public class TypeLong extends TypePrimitive<Long> {
    @Override
    public long toLong(Long val) {
        return 0;
    }

    @Override
    public Long fromLong(long val) {
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
