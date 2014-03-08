package com.dafttech.classloader;

import java.util.LinkedList;

public class DiscoveredClasses extends LinkedList<ContainedFile> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ContainedFile sourcePath;

    protected DiscoveredClasses(ContainedFile sourcePath) {
        this.sourcePath = sourcePath;
    }
}
