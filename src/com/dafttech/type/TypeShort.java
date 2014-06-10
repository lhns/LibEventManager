package com.dafttech.type;

public class TypeShort extends TypePrimitive<Short> {
    @Override
    public long toLong(Short val) {
        return 0;
    }

    @Override
    public Short fromLong(long val) {
        return null;
    }

    @Override
    public int getSize() {
        return 2;
    }

    @Override
    public Object getNullObject() {
        return 0;
    }

}
