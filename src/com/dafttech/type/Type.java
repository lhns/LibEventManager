package com.dafttech.type;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Type<ClassType> {
    public static final List<Type<?>> types = new ArrayList<Type<?>>();
    static {
        types.add(new TypeNull());
        types.add(new TypeArray(null));
        types.add(new TypeInteger(null));
        types.add(new TypeLong(null));
        types.add(new TypeFloat(null));
        types.add(new TypeDouble(null));
        types.add(new TypeByte(null));
        types.add(new TypeShort(null));
        types.add(new TypeCharacter(null));
        types.add(new TypeBoolean(null));
        types.add(new TypeClass(null));
        types.add(new TypeObject(null));
    }

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

    public boolean isType(Object obj) {
        return obj != null && getTypeClass().isAssignableFrom(obj.getClass());
    }

    public Type<?> newInstance(Object obj) {
        if (obj == null) return null;
        Class<?> typeClass = getTypeClass();
        if (!typeClass.isAssignableFrom(obj.getClass())) return null;
        try {
            return getClass().getConstructor(typeClass).newInstance(obj);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Type<?> forObject(Object obj) {
        for (Type<?> type : types) {
            if (!type.isType(obj)) continue;
            return type.newInstance(obj);
        }
        return new TypeVoid();
    }
}
