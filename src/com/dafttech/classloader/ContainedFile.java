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

    @Override
    public ContainedFile[] listFiles() {
        String filenames[] = list();
        if (filenames == null) return null;
        ContainedFile files[] = new ContainedFile[filenames.length];
        for (int i = 0; i < filenames.length; i++)
            files[i] = new ContainedFile(getPath(), filenames[i]);
        return files;
    }

    public String getContainerPath(String... typeExts) {
        String path, typeExt, ret = null;
        for (int i = 0; i < typeExts.length; i++) {
            typeExt = typeExts[i];
            path = getAbsolutePath().replace("\\", "/");
            typeExt = typeExt.toLowerCase();
            if (!typeExt.startsWith(".")) typeExt = "." + typeExt;
            do {
                if (path.toLowerCase().endsWith(typeExt) && (ret == null || path.length() < ret.length())) {
                    ret = path;
                    break;
                }
                path = path.substring(0, path.lastIndexOf("/"));
            } while (path.contains("/"));
        }
        return ret;
    }

    public ContainedFile getContainerFile(String... typeExts) {
        String path = getContainerPath(typeExts);
        if (path != null) return new ContainedFile(path);
        return null;
    }

    public String getContainedPath(String... typeExts) {
        String containerPath = getContainerPath(typeExts);
        String path = getAbsolutePath();
        if (containerPath != null) {
            path = path.replace("\\", "/");
            path = path.substring(containerPath.length());
            if (path.startsWith("/")) path = path.substring(1);
            return path;
        }
        return path;
    }

    public ContainedFile getContainedFile(String... typeExts) {
        return new ContainedFile(getContainedPath(typeExts));
    }

    public boolean isContained(String... typeExts) {
        return getContainerPath(typeExts) != null;
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

    public String getPackage() {
        String path = getPath();
        if (path.contains("|")) {
            path = path.substring(path.lastIndexOf("|") + 1);
            path = path.replace("\\", "/").replace("/", ".");
            return path;
        }
        return null;
    }

    public String getPackagePath() {
        String packageName = getPackage();
        String path = getPath();
        if (packageName != null) {
            path = path.substring(0, path.length() - packageName.length() - 1);
        }
        return path;
    }

    public ContainedFile getPackageFile() {
        return new ContainedFile(getPackagePath());
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

    public static ContainedFile fromPackage(String packageName) {
        return fromPackage("", packageName);
    }

    public static ContainedFile fromPackage(File file, String packageName) {
        return fromPackage(file.getPath(), packageName);
    }

    public static ContainedFile fromPackage(String path, String packageName) {
        return new ContainedFile(path + "|" + packageName.replace(".", "/"));
    }
}
