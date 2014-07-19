package com.dafttech.newclassloader;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;

import com.dafttech.hash.HashUtil;

public class URLClassLocation {
    public static final String EXT_CLASS = ".class";

    private URL sourceURL;
    private String qualifiedName;

    public URLClassLocation(URL sourceURL, String qualifiedName) {
        this.sourceURL = sourceURL;
        this.qualifiedName = qualifiedName;
    }

    public URLClassLocation(Class<?> target) {
        this(target.getResource("/" + target.getName().replace(".", "/") + EXT_CLASS), target.getName());
    }

    public URL getSourceURL() {
        return sourceURL;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public Class<?> loadClass() {
        return loadClass(null);
    }

    public Class<?> loadClass(ClassLoader parent) {
        return loadClassWithClassLoader(URLClassLoader.newInstance(new URL[] { sourceURL }, parent));
    }

    public Class<?> loadClassWithClassLoader(URLClassLoader classLoader) {
        if (classLoader == null) return null;

        try {
            return classLoader.loadClass(qualifiedName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    // TODO
    public static Set<URLClassLocation> discoverSourceURL(URL sourceURL) {
        return null;
    }

    @Override
    public int hashCode() {
        return HashUtil.hashCode(sourceURL, qualifiedName);
    }

    @Override
    public boolean equals(Object obj) {
        return HashUtil.equals(this, obj);
    }
}
