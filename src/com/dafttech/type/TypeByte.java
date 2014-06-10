package com.dafttech.type;

public class TypeByte extends TypePrimitive<Byte> {
    @Override
    public long toLong(Byte val) {
        return 0;
    }

    @Override
    public Byte fromLong(long val) {
        return null;
    }

    @Override
    public int getSize() {
        return 1;
    }

    @Override
    public Object getNullObject() {
        return 0;
    }

}
