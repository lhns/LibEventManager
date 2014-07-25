package com.dafttech.list;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class UnmodifiableTupleList implements TupleList {
    final TupleList tuple;

    public UnmodifiableTupleList(TupleList tuple) {
        this.tuple = tuple;
    }

    @Override
    public void add(int i, Object obj) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(Object e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends Object> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int i, Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(Object obj) {
        return tuple.contains(obj);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return tuple.containsAll(collection);
    }

    @Override
    public boolean containsType(Class<?> type) {
        return tuple.containsType(type);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || tuple.equals(obj);
    }

    @Override
    public Object get(int i) {
        return tuple.get(i);
    }

    @Override
    public <T> T get(int index, Class<T> cast) throws IndexOutOfBoundsException {
        return tuple.get(index, cast);
    }

    @Override
    public <T> List<T> getAll(Class<T> type) {
        return tuple.getAll(type);
    }

    @Override
    public <T> T getFirst(Class<T> type) {
        return tuple.getFirst(type);
    }

    @Override
    public <T> T getType(Class<T> type, int index) {
        return tuple.getType(type, index);
    }

    @Override
    public int hashCode() {
        return tuple.hashCode();
    }

    @Override
    public int indexOf(Object obj) {
        return tuple.indexOf(obj);
    }

    @Override
    public boolean isEmpty() {
        return tuple.isEmpty();
    }

    @Override
    public Iterator<Object> iterator() {
        return listIterator();
    }

    @Override
    public int lastIndexOf(Object obj) {
        return tuple.lastIndexOf(obj);
    }

    @Override
    public ListIterator<Object> listIterator() {
        return listIterator(0);
    }

    @Override
    public ListIterator<Object> listIterator(final int index) {
        return new ListIterator<Object>() {
            private final ListIterator<Object> i;

            {
                i = tuple.listIterator(index);
            }

            @Override
            public void add(Object obj) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void forEachRemaining(Consumer<Object> consumer) {
                i.forEachRemaining(consumer);
            }

            @Override
            public boolean hasNext() {
                return i.hasNext();
            }

            @Override
            public boolean hasPrevious() {
                return i.hasPrevious();
            }

            @Override
            public Object next() {
                return i.next();
            }

            @Override
            public int nextIndex() {
                return i.nextIndex();
            }

            @Override
            public Object previous() {
                return i.previous();
            }

            @Override
            public int previousIndex() {
                return i.previousIndex();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void set(Object obj) {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public Object remove(int i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object obj) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void replaceAll(UnaryOperator<Object> unaryoperator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object set(int i, Object obj) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return tuple.size();
    }

    @Override
    public void sort(Comparator<Object> comparator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TupleList subList(int i, int j) {
        return new UnmodifiableTupleList(tuple.subList(i, j));
    }

    @Override
    public Object[] toArray() {
        return tuple.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return tuple.toArray(a);
    }
}