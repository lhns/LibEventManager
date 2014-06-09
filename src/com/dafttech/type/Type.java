package com.dafttech.type;

public abstract class Type<ClassType> {
    /*
     * public static final TypeInteger INTEGER = new TypeInteger(); public
     * static final TypeLong LONG = new TypeLong(); public static final
     * TypeFloat FLOAT = new TypeFloat(); public static final TypeDouble DOUBLE
     * = new TypeDouble(); public static final TypeShort SHORT = new
     * TypeShort(); public static final TypeCharacter CHARACTER = new
     * TypeCharacter(); public static final TypeByte BYTE = new TypeByte();
     * public static final TypeBoolean BOOLEAN = new TypeBoolean(); public
     * static final TypeObject OBJECT = new TypeObject();
     */
    // public static final TypeArray ARRAY = new TypeArray();
    // public static final TypeVoid VOID = new TypeVoid(); // TODO: add TypeVoid

    @SuppressWarnings("unused")
    private boolean singleton = false;
    @SuppressWarnings("unused")
    private ClassType value;

    @SuppressWarnings("unused")
    private Type() {
        this(null);
        singleton = true;
    }

    public Type(ClassType value) {
        this.value = value;
    }
}
