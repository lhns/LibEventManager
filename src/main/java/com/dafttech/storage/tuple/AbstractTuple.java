package com.dafttech.storage.tuple;

import com.dafttech.primitive.PrimitiveUtil;

import java.lang.reflect.Array;
import java.util.*;

public abstract class AbstractTuple extends AbstractList<Object> implements Tuple {
    @Override
    public abstract Tuple subList(int paramInt1, int paramInt2);

    // TUPLE

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
            if (primitive != null && primitive.isInstance(elementData[i]) || type.isInstance(elementData[i]))
                return true;
        return false;
    }

    @Override
    protected Tuple clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    // UNMODIFIABLE

    @Override
    public Object set(int paramInt, Object paramE) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int paramInt, Object paramE) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(Object paramE) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends Object> paramCollection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int paramInt, Collection<? extends Object> paramCollection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object paramObject) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object remove(int paramInt) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> paramCollection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> paramCollection) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void removeRange(int paramInt1, int paramInt2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<Object> listIterator(final int i) {
        return new ListItr(i);
    }

    protected class Itr implements Iterator<Object> {
        int cursor;

        public Itr() {
            cursor = 0;
        }

        @Override
        public boolean hasNext() {
            return cursor != size();
        }

        @Override
        public Object next() {
            try {
                int i = cursor;
                Object obj = get(i);
                cursor = i + 1;
                return obj;
            } catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
                throw new NoSuchElementException();
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    protected class ListItr extends Itr implements ListIterator<Object> {
        public ListItr(int i) {
            cursor = i;
        }

        @Override
        public void add(Object e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasPrevious() {
            return cursor != 0;
        }

        @Override
        public Object previous() {
            try {
                int i = cursor - 1;
                Object obj = get(i);
                cursor = i;
                return obj;
            } catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
                throw new NoSuchElementException();
            }
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return cursor - 1;
        }

        @Override
        public void set(Object e) {
            throw new UnsupportedOperationException();
        }
    }
}
