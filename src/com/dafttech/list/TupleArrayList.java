package com.dafttech.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.dafttech.primitive.PrimitiveUtil;

public class TupleArrayList extends ArrayList<Object> implements TupleList {

    /**
     * 
     */
    private static final long serialVersionUID = 4838865189004607435L;

    public TupleArrayList(int capacity) {
        super(capacity);
    }

    public TupleArrayList() {
        super();
    }

    public TupleArrayList(Collection<? extends Object> collection) {
        super(collection);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(int index, Class<T> cast) throws IndexOutOfBoundsException {
        Object obj = get(index);
        if (cast.isInstance(obj)) return (T) obj;
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
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
    public <T> List<T> getAll(Class<T> type) {
        List<T> ret = new ArrayList<T>();

        Object[] elementData = toArray();
        Class<?> primitive = type.isPrimitive() ? PrimitiveUtil.get(type).wrapperClass : null;

        for (int i = 0; i < elementData.length; i++)
            if (primitive != null && primitive.isInstance(elementData[i]) || type.isInstance(elementData[i]))
                ret.add((T) elementData[i]);
        return ret;
    }

    @Override
    public boolean containsType(Class<?> type) {
        Object[] elementData = toArray();
        Class<?> primitive = type.isPrimitive() ? PrimitiveUtil.get(type).wrapperClass : null;

        for (int i = 0; i < elementData.length; i++)
            if (primitive != null && primitive.isInstance(elementData[i]) || type.isInstance(elementData[i])) return true;
        return false;
    }

    @Override
    public TupleList subList(int i, int j) {
        return new TupleArrayList(super.subList(i, j));
    }
}
