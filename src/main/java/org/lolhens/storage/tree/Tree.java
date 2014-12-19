package org.lolhens.storage.tree;

import java.util.Collection;
import java.util.List;

public interface Tree<Leaf> {
    // Leaf
    public boolean hasLeaf();

    public Leaf getLeaf();

    public void setLeaf(Leaf leaf);

    public void removeLeaf();

    // Root
    public boolean hasRoot();

    public Tree<Leaf> getRoot();

    public void setRoot(Tree<Leaf> root);

    public void removeRoot();

    // Branch
    public boolean hasBranches();

    public int size();

    public List<Tree<Leaf>> getBranches();

    public boolean hasBranch(int index);

    public boolean hasBranch(Tree<Leaf> branch);

    public boolean hasBranch(Leaf leaf);

    public Tree<Leaf> getBranch(int index);

    public Tree<Leaf> getBranch(Tree<Leaf> branch);

    public Tree<Leaf> getBranch(Leaf leaf);

    public void addBranch(Tree<Leaf> branch);

    public void addBranch(int index, Tree<Leaf> branch);

    public void addBranches(Collection<Tree<Leaf>> branches);

    public void removeBranch(int index);

    public void removeBranch(Leaf leaf);

    public void removeBranch(Tree<Leaf> branch);

    // Tree
    public int getTreeSize();

    public List<Leaf> getTreeLeaves();
}
