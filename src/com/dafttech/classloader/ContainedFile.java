package com.dafttech.classloader;

import java.io.File;
import java.net.URI;

public class ContainedFile extends File {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    protected static final String seperator = "!";
    protected static final String jar = "jar:file:";
    protected static final String rsrc = "rsrc:";

    protected static final String fakeSpaces = "%20";
    protected static final String classExt = ".class";
    protected static final String jarExt = ".jar";

    public ContainedFile(File file, String string) {
        super(file, string);
    }

    public ContainedFile(String string) {
        super(string);
    }

    public ContainedFile(String string1, String string2) {
        super(string1, string2);
    }

    public ContainedFile(URI uri) {
        super(uri);
    }

    public String getContainerPath(String typeExt) {
        if (!typeExt.startsWith(".")) typeExt = "." + typeExt;
        String path = getAbsolutePath().replace("\\", "/");
        do {
            if (path.endsWith(typeExt)) return path;
            path = path.substring(0, path.lastIndexOf("/") - 1);
        } while (path.contains("/"));
        return null;
    }

    public File getContainerFile(String typeExt) {
        String path = getContainerPath(typeExt);
        if (path != null) return new File(path);
        return null;
    }

    public String getContainedPath(String typeExt) {
        String containerPath = getContainerPath(typeExt);
        if (containerPath != null) {
            return getAbsolutePath().replace("\\", "/").substring(containerPath.length() + 1);
        }
        return null;
    }

    public File getContainedFile(String typeExt) {
        String path = getContainedPath(typeExt);
        if (path != null) return new File(path);
        return null;
    }

    public boolean isContained(String typeExt) {
        return getContainerPath(typeExt) != null;
    }

    public boolean isRsrc() {
        return toString().startsWith(rsrc);
    }

    public String getExtension() {
        String path = getAbsolutePath().replace("\\", "/");
        if (path.contains(".")) {
            String ext = path.substring(path.lastIndexOf(".") + 1);
            if (ext.endsWith("/")) ext = ext.substring(0, ext.length() - 1);
            return ext;
        }
        return null;
    }

    public String asPackage() {
        return toString().replace("\\", "/").replace("/", ".");
    }
}
