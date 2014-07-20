package com.dafttech.newclassloader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
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

    public static Set<URLClassLocation> discoverSourceURL(final URL sourceURL) {
        final Set<URLClassLocation> discovered = new HashSet<URLClassLocation>();
        try {
            final Path sourcePath = Paths.get(sourceURL.toURI());
            Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.getFileName().toString().toLowerCase().endsWith(EXT_CLASS)) {
                        String qualifiedName = file.relativize(sourcePath).toString().replace("/", ".");
                        qualifiedName = qualifiedName.substring(0, qualifiedName.length() - EXT_CLASS.length());
                        discovered.add(new URLClassLocation(sourceURL, qualifiedName));
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return discovered;
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
