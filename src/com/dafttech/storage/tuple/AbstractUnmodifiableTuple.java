package com.dafttech.storage.tuple;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public abstract class AbstractUnmodifiableTuple extends AbstractTuple implements Tuple {
    @Override
    public abstract Tuple subList(int paramInt1, int paramInt2);

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
    public void sort(Comparator<? super Object> paramComparator) {
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
