package com.dafttech.tree;

public interface Tree<E> {
    public Tree<E> get(E object);

    public E getObject();
}
