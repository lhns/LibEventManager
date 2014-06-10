package com.dafttech.type;

public class TypeInteger extends TypePrimitive<Integer> {
    @Override
    public long toLong(Integer val) {
        return 0;
    }

    @Override
    public Integer fromLong(long val) {
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
