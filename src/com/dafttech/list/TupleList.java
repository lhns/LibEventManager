package com.dafttech.list;

import java.util.List;

public interface TupleList extends List<Object> {
    public <T> T get(int index, Class<T> cast) throws IndexOutOfBoundsException;

    public <T> T getType(Class<T> type, int index);

    public <T> T getFirst(Class<T> type);

    public <T> List<T> getAll(Class<T> type);

    public boolean containsType(Class<?> type);

    @Override
    public TupleList subList(int i, int j);
}
