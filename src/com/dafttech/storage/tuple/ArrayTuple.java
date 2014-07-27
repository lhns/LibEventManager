package com.dafttech.storage.tuple;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.RandomAccess;

import com.dafttech.hash.HashUtil;

public class ArrayTuple extends AbstractUnmodifiableTuple implements Tuple, RandomAccess, Cloneable, Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 5291579362104103299L;

    private transient Object[] elementData;

    public ArrayTuple(Object... array) {
        elementData = Arrays.copyOf(array, array.length);
    }

    public ArrayTuple(Collection<?> collection) {
        this(collection.toArray());
    }

    @Override
    public Object get(int paramInt) {
        return elementData[paramInt];
    }

    @Override
    public int size() {
        return elementData.length;
    }

    @Override
    public Tuple subList(int paramInt1, int paramInt2) {
        return new ArrayTuple(Arrays.copyOfRange(elementData, paramInt1, paramInt2));
    }

    @Override
    public boolean contains(Object paramObject) {
        return indexOf(paramObject) >= 0;
    }

    @Override
    public int indexOf(Object paramObject) {
        if (paramObject == null) {
            for (int i = 0; i < elementData.length; i++)
                if (elementData[i] == null) return i;
        } else {
            for (int i = 0; i < elementData.length; i++)
                if (paramObject.equals(elementData[i])) return i;
        }
        return -1;
    }

    @Override
    public boolean isEmpty() {
        return elementData.length == 0;
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elementData, elementData.length);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] paramArrayOfT) {
        if (paramArrayOfT.length < elementData.length) {
            return (T[]) Arrays.copyOf(elementData, elementData.length, paramArrayOfT.getClass());
        }
        System.arraycopy(elementData, 0, paramArrayOfT, 0, elementData.length);
        if (paramArrayOfT.length > elementData.length) paramArrayOfT[elementData.length] = null;
        return paramArrayOfT;
    }

    @Override
    public ArrayTuple clone() throws CloneNotSupportedException {
        return new ArrayTuple(elementData);
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
        paramObjectOutputStream.defaultWriteObject();

        paramObjectOutputStream.writeInt(elementData.length);

        for (int i = 0; i < elementData.length; i++)
            paramObjectOutputStream.writeObject(elementData[i]);
    }

    private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException {
        elementData = null;

        paramObjectInputStream.defaultReadObject();

        elementData = new Object[paramObjectInputStream.readInt()];

        if (elementData.length <= 0) return;

        for (int i = 0; i < elementData.length; i++)
            elementData[i] = paramObjectInputStream.readObject();
    }

    @Override
    public int hashCode() {
        return HashUtil.hashCode(elementData);
    }

    @Override
    public boolean equals(Object target) {
        return HashUtil.equals(this, target);
    }
}
