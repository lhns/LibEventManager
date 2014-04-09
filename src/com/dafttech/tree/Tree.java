package com.dafttech.tree;

import java.util.Collection;
import java.util.List;

public interface Tree<Leaf> {
    public boolean hasLeaf();

    public Leaf getLeaf();

    public void setLeaf(Leaf leaf);

    public boolean hasRoot();

    public Tree<Leaf> getRoot();

    public boolean hasBranches();

    public int size();

    public List<Tree<Leaf>> getBranches();

    public Tree<Leaf> getBranch(int index);

    public void addBranch(Leaf leaf);

    public void addBranch(Tree<Leaf> branch);

    public void addBranch(int index, Leaf leaf);

    public void addBranch(int index, Tree<Leaf> branch);

    public void addBranchesByLeaves(Collection<Leaf> leaves);

    public void addBranches(Collection<Tree<Leaf>> branches);

    public void removeBranch(int index);

    public void removeBranch(Tree<Leaf> branch);

    public void removeBranch(Leaf leaf);

    public int getTreeSize();

    public List<Leaf> getTreeLeaves();
}
