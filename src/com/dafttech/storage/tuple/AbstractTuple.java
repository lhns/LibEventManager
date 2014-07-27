package com.dafttech.storage.tuple;

import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import com.dafttech.primitive.PrimitiveUtil;

public abstract class AbstractTuple extends AbstractList<Object> implements Tuple {
    @Override
    public abstract Tuple subList(int paramInt1, int paramInt2);

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(int index, Class<T> cast) throws IndexOutOfBoundsException {
        try {
            return (T) get(index);
        } catch (ClassCastException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getType(Class<T> type, int index) {
        Object[] elementData = toArray();
        Class<?> primitive = type.isPrimitive() ? PrimitiveUtil.get(type).wrapperClass : null;

        Object lastElement = null;
        for (int i = 0; i < elementData.length; i++)
            if (primitive != null && primitive.isInstance(elementData[i]) || type.isInstance(elementData[i])) {
                lastElement = elementData[i];
                if (index-- == 0) break;
            }
        return (T) lastElement;
    }

    @Override
    public <T> T getFirst(Class<T> type) {
        return getType(type, 0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> getType(Class<T> type) {
        List<T> ret = new ArrayList<T>();

        Object[] elementData = toArray();
        Class<?> primitive = type.isPrimitive() ? PrimitiveUtil.get(type).wrapperClass : null;

        for (int i = 0; i < elementData.length; i++)
            if (primitive != null && primitive.isInstance(elementData[i]) || type.isInstance(elementData[i]))
                ret.add((T) elementData[i]);
        return ret;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] getTypeArray(Class<T> type) {
        return getType(type).toArray((T[]) Array.newInstance(type, 0));
    }

    @Override
    public boolean containsType(Class<?> type) {
        Object[] elementData = toArray();
        Class<?> primitive = type.isPrimitive() ? PrimitiveUtil.get(type).wrapperClass : null;

        for (int i = 0; i < elementData.length; i++)
            if (primitive != null && primitive.isInstance(elementData[i]) || type.isInstance(elementData[i])) return true;
        return false;
    }
}
