package com.dafttech.type;

import java.lang.reflect.Field;
import java.util.Arrays;

public abstract class Type<ClassType> {
    protected ClassType value;

    public Type(ClassType value) {
        this.value = value;
    }

    public ClassType getValue() {
        return value;
    }

    protected static Field getDeclaredField(Class<?> targetClass, String fieldName) {
        try {
            Field field = targetClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    public abstract byte[] toByteArray();

    public abstract ClassType fromByteArray(byte... array);

    public ClassType fromByteArray(byte[] array, int offset) {
        return fromByteArray(Arrays.copyOfRange(array, offset, array.length));
    }

    public abstract Class<?> getTypeClass();

    public abstract Object getNullObject();

    public static Type<?> forObject(Object obj) {
        return new TypeObject(obj);
    }
}
