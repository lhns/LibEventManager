package com.dafttech.type;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Type<ClassType> {
    public static final List<Type<?>> types = new ArrayList<Type<?>>();

    public static final TypeNull NULL = new TypeNull(true);
    public static final TypeArray ARRAY = new TypeArray(true);
    public static final TypeInteger INTEGER = new TypeInteger(true);
    public static final TypeLong LONG = new TypeLong(true);
    public static final TypeFloat FLOAT = new TypeFloat(true);
    public static final TypeDouble DOUBLE = new TypeDouble(true);
    public static final TypeByte BYTE = new TypeByte(true);
    public static final TypeShort SHORT = new TypeShort(true);
    public static final TypeCharacter CHARACTER = new TypeCharacter(true);
    public static final TypeBoolean BOOLEAN = new TypeBoolean(true);
    public static final TypeClass CLASS = new TypeClass(true);
    public static final TypeObject OBJECT = new TypeObject(true);
    public static final TypeVoid VOID = new TypeVoid(true);

    protected ClassType value = null;

    protected Type(boolean prototype) {
        if (prototype) types.add(this);
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

    public abstract Type<ClassType> fromByteArray(byte... array);

    public Type<ClassType> fromByteArray(byte[] array, int offset) {
        return fromByteArray(Arrays.copyOfRange(array, offset, array.length));
    }

    public abstract Class<?> getTypeClass();

    public abstract Object getNullObject();

    public boolean isType(Object obj) {
        return obj != null && getTypeClass().isAssignableFrom(obj.getClass());
    }

    @SuppressWarnings("unchecked")
    public final Type<ClassType> create(Object obj) {
        if (!getTypeClass().isAssignableFrom(obj.getClass())) return null;
        try {
            Type<ClassType> newInstance = getClass().getDeclaredConstructor(boolean.class).newInstance(false);
            newInstance.value = (ClassType) obj;
            return newInstance;
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
            return type.create(obj);
        }
        return VOID.create(null);
    }
}
