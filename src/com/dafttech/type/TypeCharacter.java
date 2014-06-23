package com.dafttech.type;

import java.lang.reflect.Field;

public class TypeCharacter extends TypePrimitive<Character> {

    private static Field valueField = Type.getDeclaredField(Character.class, "value");

    protected TypeCharacter(boolean prototype) {
        super(prototype);
    }

    @Override
    public int getSize() {
        return Character.SIZE;
    }

    @Override
    public Object getNullObject() {
        return '\u0000';
    }

    @Override
    public long toLong() {
        return new Short((short) (char) value).longValue();
    }

    @Override
    public Character fromLong(long val) {
        return (char) new Long(val).shortValue();
    }

    @Override
    protected Field getValueField() {
        return valueField;
    }

    @Override
    public Class<?> getTypeClass() {
        return Character.class;
    }

    @Override
    public Class<?> getPrimitiveClass() {
        return char.class;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <ReturnType extends Type<Character>> ReturnType create(Object obj) {
        if (obj != null && !getTypeClass().isAssignableFrom(obj.getClass())) return null;
        ReturnType type = (ReturnType) new TypeCharacter(false);
        type.value = (Character) obj;
        return type;
    }
}
