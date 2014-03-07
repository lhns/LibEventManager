package com.dafttech.classloader;

import java.io.File;
import java.net.URI;

public class ContainedFile extends File {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ContainedFile(File file, String string) {
        super(file, string);
    }

    public ContainedFile(String string) {
        super(getWithoutProtocol(string));
    }

    public ContainedFile(String string1, String string2) {
        super(getWithoutProtocol(string1), getWithoutProtocol(string2));
    }

    public ContainedFile(URI uri) {
        super(getWithoutProtocol(uri.toString()));
    }

    public ContainedFile(File file) {
        super(getWithoutProtocol(file.toString()));
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
        return getAbsolutePath();
    }

    public File getContainedFile(String typeExt) {
        return new File(getContainedPath(typeExt));
    }

    public boolean isContained(String typeExt) {
        return getContainerPath(typeExt) != null;
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

    public String toPackage() {
        return toString().replace("\\", "/").replace("/", ".");
    }

    public static String getWithoutProtocol(String path) {
        if (path.indexOf(":") > (path.replace("\\", "/").startsWith("/") ? 2 : 1)) {
            path = path.substring(path.indexOf(":") + 1);
            path = path.replace("!", "");
            path = path.replace("%20", " ");
            return getWithoutProtocol(path);
        }
        return path;
    }

    public static String getProtocol(String path) {
        return path.substring(0, path.length() - getWithoutProtocol(path).length());
    }
}
