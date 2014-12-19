package org.lolhens.storage.tree;

import org.lolhens.hash.HashUtil;

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

    // Leaf
    @Override
    public boolean hasLeaf() {
        return getLeaf() != null;
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
    public void removeLeaf() {
        setLeaf(null);
    }

    // Root
    @Override
    public boolean hasRoot() {
        return getRoot() != null;
    }

    @Override
    public Tree<Leaf> getRoot() {
        return root;
    }

    @Override
    public void setRoot(Tree<Leaf> root) {
        if (hasRoot()) getRoot().removeBranch(this);
        this.root = root;
    }

    @Override
    public void removeRoot() {
        setRoot(null);
    }

    // Branch
    @Override
    public boolean hasBranches() {
        return !branches.isEmpty();
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
    public boolean hasBranch(int index) {
        return getBranch(index) != null;
    }

    @Override
    public boolean hasBranch(Tree<Leaf> branch) {
        return getBranch(branch) != null;
    }

    @Override
    public boolean hasBranch(Leaf leaf) {
        return getBranch(leaf) != null;
    }

    @Override
    public Tree<Leaf> getBranch(int index) {
        if (index < 0 || index >= branches.size()) return null;
        return branches.get(index);
    }

    @Override
    public Tree<Leaf> getBranch(Tree<Leaf> branch) {
        return branches.contains(branch) ? branch : null;
    }

    @Override
    public Tree<Leaf> getBranch(Leaf leaf) {
        Leaf currLeaf;
        for (Tree<Leaf> branch : branches) {
            currLeaf = branch.getLeaf();
            if (leaf == currLeaf || leaf.equals(currLeaf)) return branch;
        }
        return null;
    }

    @Override
    public void addBranch(Tree<Leaf> branch) {
        branches.add(branch);
    }

    @Override
    public void addBranch(int index, Tree<Leaf> branch) {
        branches.add(index, branch);
    }

    @Override
    public void addBranches(Collection<Tree<Leaf>> branches) {
        this.branches.addAll(branches);
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
        Leaf currLeaf;
        for (int i = branches.size() - 1; i >= 0; i--) {
            currLeaf = branches.get(i).getLeaf();
            if (leaf == currLeaf || leaf.equals(currLeaf)) branches.remove(i);
        }
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

    private void writeObject(ObjectOutputStream objectoutputstream) throws IOException {
        objectoutputstream.defaultWriteObject();
    }

    private void readObject(ObjectInputStream objectinputstream) throws IOException, ClassNotFoundException {
        objectinputstream.defaultReadObject();
    }

    @Override
    public int hashCode() {
        return HashUtil.hashCode(leaf, branches);
    }

    @Override
    public boolean equals(Object obj) {
        return HashUtil.equals(this, obj);
    }
}
