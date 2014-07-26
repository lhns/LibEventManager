package com.dafttech.storage.tuple;

import java.util.List;

public interface Tuple extends List<Object> {
    @Override
    public Tuple subList(int paramInt1, int paramInt2);

    public <T> T get(int index, Class<T> cast) throws IndexOutOfBoundsException;

    public <T> T getType(Class<T> type, int index);

    public <T> T getFirst(Class<T> type);

    public <T> List<T> getAll(Class<T> type);

    public boolean containsType(Class<?> type);
}
