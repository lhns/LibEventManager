package com.dafttech.tree;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class HashTree<E> implements Tree<E>, Cloneable, Serializable {
    private List<Tree<E>> childs = new LinkedList<Tree<E>>();
    private E elementData;

    /**
     * 
     */
    private static final long serialVersionUID = 7026883572461978385L;

    @Override
    public Tree<E> get(Object object) {
        for (Tree<E> tree : childs) {
            if (object == tree.getObject() || object.equals(tree.getObject())) return tree;
        }
        return null;
    }

    @Override
    public E getObject() {
        return elementData;
    }

}
