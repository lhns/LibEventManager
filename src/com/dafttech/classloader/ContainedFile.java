package com.dafttech.classloader;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URI;

@Deprecated
public class ContainedFile extends File {

    private static Field pathField;
    static {
        try {
            pathField = File.class.getDeclaredField("path");
            pathField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private boolean withPackage = false;

    @Deprecated
    public ContainedFile(File file, String string) {
        super(getWithoutProtocol(file.toString()), getWithoutProtocol(string));
    }

    @Deprecated
    public ContainedFile(String string) {
        super(getWithoutProtocol(string));
    }

    @Deprecated
    public ContainedFile(String string1, String string2) {
        super(getWithoutProtocol(string1), getWithoutProtocol(string2));
    }

    @Deprecated
    public ContainedFile(URI uri) {
        super(getWithoutProtocol(uri.toString()));
    }

    @Deprecated
    public ContainedFile(File file) {
        super(getWithoutProtocol(file.toString()));
    }

    @Override
    public String[] list() {
        String[] list = new String[0];
        try {
            String path = toString();
            pathField.set(this, getPath());
            list = super.list();
            pathField.set(this, path);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public ContainedFile[] listFiles() {
        String[] filenames = list();
        if (filenames != null) {
            ContainedFile[] files = new ContainedFile[filenames.length];
            String path = toString();
            for (int i = 0; i < filenames.length; i++)
                files[i] = new ContainedFile(path, filenames[i]);
            return files;
        }
        return new ContainedFile[] {};
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
        String path = toString();
        if (path.contains("|")) {
            path = path.substring(path.lastIndexOf("|") + 1);
            path = path.replace("\\", "/");
            while (path.startsWith("/"))
                path = path.substring(1);
            path = path.replace("/", ".");
            return path;
        }
        return null;
    }

    public String getPackageAsPath() {
        String packageName = getPackage();
        if (packageName != null) return packageName.replace(".", "/");
        return null;
    }

    public String getWithoutPackage() {
        String path = super.getPath();
        String packageName = getPackage();
        if (packageName != null) {
            path = path.substring(0, path.length() - packageName.length() - 1);
        }
        return path;
    }

    @Override
    public boolean isDirectory() {
        try {
            String path = toString();
            pathField.set(this, getPath());
            boolean ret = super.isDirectory();
            pathField.set(this, path);
            return ret;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean containsPackage() {
        return getPackage() != null;
    }

    @Override
    public String getPath() {
        String path = super.getPath();
        if (withPackage) {
            return path;
        }
        return path.replace("|", "");
    }

    @Override
    public String toString() {
        return super.getPath();
    }

    public ContainedFile withPackage() {
        withPackage = true;
        return this;
    }

    public ContainedFile withoutPackage() {
        withPackage = false;
        return this;
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

    public static String transferPackageSeperator(String source, String target) {
        if (source.contains("|"))
            target = target.substring(0, source.indexOf("|")) + "|" + target.substring(source.indexOf("|"));
        return target;
    }
}
