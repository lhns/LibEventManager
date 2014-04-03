package com.dafttech.tree;

import java.util.Collection;
import java.util.List;

public interface Tree<E> {
    public boolean hasLeaf();

    public E getLeaf();

    public void setLeaf(E leaf);

    public boolean hasRoot();

    public Tree<E> getRoot();

    public boolean hasBranches();

    public int size();

    public List<Tree<E>> getBranches();

    public Tree<E> getBranch(int index);

    public void addBranch(E leaf);

    public void addBranch(Tree<E> branch);

    public void addBranch(int index, E leaf);

    public void addBranch(int index, Tree<E> branch);

    public void addBranchesByLeaves(Collection<E> leaves);

    public void addBranches(Collection<Tree<E>> branches);

    public void removeBranch(int index);

    public void removeBranch(Tree<E> branch);

    public void removeBranch(E leaf);

    public int getTreeSize();
}
