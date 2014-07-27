package com.dafttech.storage.tuple;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

import com.dafttech.hash.HashUtil;

public class TupleFacade extends AbstractTuple implements Tuple, RandomAccess, Cloneable, Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -1221937267645102522L;

    private List<Object> parent;

    public TupleFacade(List<Object> list) {
        parent = list;
    }

    @Override
    public Tuple subList(int paramInt1, int paramInt2) {
        return new TupleFacade(parent.subList(paramInt1, paramInt2));
    }

    @Override
    public int size() {
        return parent.size();
    }

    @Override
    public boolean isEmpty() {
        return parent.isEmpty();
    }

    @Override
    public boolean contains(Object paramObject) {
        return parent.contains(paramObject);
    }

    @Override
    public Iterator<Object> iterator() {
        return parent.iterator();
    }

    @Override
    public Object[] toArray() {
        return parent.toArray();
    }

    @Override
    public <T> T[] toArray(T[] paramArrayOfT) {
        return parent.toArray(paramArrayOfT);
    }

    @Override
    public boolean add(Object paramE) {
        return parent.add(paramE);
    }

    @Override
    public boolean remove(Object paramObject) {
        return parent.remove(paramObject);
    }

    @Override
    public boolean containsAll(Collection<?> paramCollection) {
        return parent.containsAll(paramCollection);
    }

    @Override
    public boolean addAll(Collection<? extends Object> paramCollection) {
        return parent.addAll(paramCollection);
    }

    @Override
    public boolean addAll(int paramInt, Collection<? extends Object> paramCollection) {
        return parent.addAll(paramInt, paramCollection);
    }

    @Override
    public boolean removeAll(Collection<?> paramCollection) {
        return parent.removeAll(paramCollection);
    }

    @Override
    public boolean retainAll(Collection<?> paramCollection) {
        return parent.retainAll(paramCollection);
    }

    @Override
    public void clear() {
        parent.clear();
    }

    @Override
    public Object get(int paramInt) {
        return parent.get(paramInt);
    }

    @Override
    public Object set(int paramInt, Object paramE) {
        return parent.set(paramInt, paramE);
    }

    @Override
    public void add(int paramInt, Object paramE) {
        parent.add(paramInt, paramE);
    }

    @Override
    public Object remove(int paramInt) {
        return parent.remove(paramInt);
    }

    @Override
    public int indexOf(Object paramObject) {
        return parent.indexOf(paramObject);
    }

    @Override
    public int lastIndexOf(Object paramObject) {
        return parent.lastIndexOf(paramObject);
    }

    @Override
    public ListIterator<Object> listIterator() {
        return parent.listIterator();
    }

    @Override
    public ListIterator<Object> listIterator(int paramInt) {
        return parent.listIterator(paramInt);
    }

    @Override
    public TupleFacade clone() throws CloneNotSupportedException {
        return new TupleFacade(parent);
    }

    @Override
    public int hashCode() {
        return HashUtil.hashCode(parent);
    }

    @Override
    public boolean equals(Object target) {
        return HashUtil.equals(this, target);
    }
}
