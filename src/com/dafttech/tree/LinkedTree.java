package com.dafttech.tree;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class LinkedTree<Leaf> implements Tree<Leaf>, Cloneable, Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 6857447299766592010L;
    private Tree<Leaf> root;
    private Leaf leaf;
    private List<Tree<Leaf>> branches = new LinkedList<Tree<Leaf>>();

    public LinkedTree(Tree<Leaf> root, Leaf leaf, Collection<? extends Tree<Leaf>> branches) {
        this.root = root;
        this.leaf = leaf;
        if (branches != null) this.branches.addAll(branches);
    }

    public LinkedTree(Tree<Leaf> tree) {
        this(tree.getRoot(), tree.getLeaf(), tree.getBranches());
    }

    public LinkedTree(Tree<Leaf> root, Leaf leaf) {
        this(root, leaf, null);
    }

    public LinkedTree(Leaf leaf) {
        this(null, leaf, null);
    }

    public LinkedTree() {
        this(null, null, null);
    }

    @Override
    public boolean hasLeaf() {
        return leaf != null;
    }

    @Override
    public Leaf getLeaf() {
        return leaf;
    }

    @Override
    public void setLeaf(Leaf leaf) {
        this.leaf = leaf;
    }

    @Override
    public boolean hasRoot() {
        return root != null;
    }

    @Override
    public Tree<Leaf> getRoot() {
        return root;
    }

    @Override
    public boolean hasBranches() {
        return branches.size() > 0;
    }

    @Override
    public int size() {
        return branches.size();
    }

    @Override
    public List<Tree<Leaf>> getBranches() {
        return branches;
    }

    @Override
    public Tree<Leaf> getBranch(int index) {
        return branches.get(index);
    }

    @Override
    public void addBranch(Leaf leaf) {
        branches.add(new LinkedTree<Leaf>(this, leaf));
    }

    @Override
    public void addBranch(Tree<Leaf> branch) {
        branches.add(branch);
    }

    @Override
    public void addBranch(int index, Leaf leaf) {
        branches.add(index, new LinkedTree<Leaf>(this, leaf));
    }

    @Override
    public void addBranch(int index, Tree<Leaf> branch) {
        branches.add(index, branch);
    }

    @Override
    public void addBranchesByLeaves(Collection<Leaf> leaves) {
        for (Leaf leaf : leaves)
            branches.add(new LinkedTree<Leaf>(leaf));
    }

    @Override
    public void addBranches(Collection<Tree<Leaf>> branches) {
        for (Tree<Leaf> branch : branches)
            branches.add(branch);
    }

    @Override
    public void removeBranch(int index) {
        branches.remove(index);
    }

    @Override
    public void removeBranch(Tree<Leaf> branch) {
        branches.remove(branch);
    }

    @Override
    public void removeBranch(Leaf leaf) {
        branches.remove(leaf);
    }

    @Override
    public int getTreeSize() {
        int size = 1;
        for (Tree<Leaf> branch : branches)
            size += branch.getTreeSize();
        return size;
    }

    @Override
    public List<Leaf> getTreeLeaves() {
        List<Leaf> leaves = new LinkedList<Leaf>();
        leaves.add(leaf);
        for (Tree<Leaf> branch : branches)
            leaves.addAll(branch.getTreeLeaves());
        return leaves;
    }

    @Override
    public Object clone() {
        return new LinkedTree<Leaf>(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj == leaf;
    }

    private void writeObject(ObjectOutputStream objectoutputstream) throws IOException {
        objectoutputstream.defaultWriteObject();
    }

    private void readObject(ObjectInputStream objectinputstream) throws IOException, ClassNotFoundException {
        objectinputstream.defaultReadObject();
    }
}
