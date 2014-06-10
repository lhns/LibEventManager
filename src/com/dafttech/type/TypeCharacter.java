package com.dafttech.type;

public class TypeCharacter extends TypePrimitive<Character> {
    @Override
    public long toLong(Character val) {
        return 0;
    }

    @Override
    public Character fromLong(long val) {
        return null;
    }

    @Override
    public int getSize() {
        return 2;
    }

    @Override
    public Object getNullObject() {
        return '\u0000';
    }

}
