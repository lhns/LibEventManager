package com.dafttech.type;

public abstract class TypePrimitive<ClassType> extends Type<ClassType> {
    public TypePrimitive(ClassType value) {
        super(value);
    }

    // public abstract int getSize();

    public abstract long toLong(ClassType val);

    public abstract ClassType fromLong(long val);
}
