package org.lolhens.classfile;

import org.lolhens.hash.HashUtil;
import org.lolhens.nio.file.PathUtil;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;

public class URLClassLocation {
    public static final String EXT_CLASS = ".class";

    private final URL sourceURL;
    private final String qualifiedName;

    public URLClassLocation(URL sourceURL, String qualifiedName) {
        this.sourceURL = sourceURL;
        this.qualifiedName = qualifiedName;
    }

    public URLClassLocation(Class<?> target) {
        this(getClassSourceURL(target), target.getName());
    }

    public static URL getClassSourceURL(Class<?> target) throws IllegalArgumentException {
        String resource = target.getName().replace(".", "/") + EXT_CLASS;
        String resourceURLString = target.getResource("/" + resource).toString();

        if (resourceURLString.startsWith("rsrc:"))
            resourceURLString = "jar:file:/"
                    + new File(System.getProperty("java.class.path")).getAbsolutePath().replace("\\", "/") + "!/"
                    + resourceURLString.substring(5);

        String sourceURLString = resourceURLString.substring(0, resourceURLString.length() - resource.length());

        try {
            return new URL(sourceURLString);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Unable to get source URL for: " + target.getName(), e);
        }
    }

    public URL getSourceURL() {
        return sourceURL;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public String getClassName() {
        return qualifiedName.substring(qualifiedName.lastIndexOf('.') + 1);
    }

    public Class<?> loadClass() throws ClassNotFoundException {
        return loadClass(null);
    }

    public Class<?> loadClass(ClassLoader parent) throws ClassNotFoundException {
        return loadClassWithClassLoader(URLClassLoader.newInstance(new URL[]{sourceURL}, parent));
    }

    public Class<?> loadClassWithClassLoader(URLClassLoader classLoader) throws ClassNotFoundException {
        if (classLoader == null) return null;

        return classLoader.loadClass(qualifiedName);
    }

    public Set<URLClassLocation> discoverSourceURL() throws IOException {
        return discoverSourceURL(sourceURL);
    }

    @Override
    public String toString() {
        return sourceURL + ", " + qualifiedName;
    }

    @Override
    public int hashCode() {
        return HashUtil.hashCode(sourceURL, qualifiedName);
    }

    @Override
    public boolean equals(Object obj) {
        return HashUtil.equals(this, obj);
    }

    public static Set<URLClassLocation> discoverSourceURL(final URL sourceURL) throws IOException {
        final Set<URLClassLocation> discovered = new HashSet<>();
        final Path sourcePath = PathUtil.get(sourceURL);
        Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.getFileName().toString().toLowerCase().endsWith(EXT_CLASS)) {
                    String qualifiedName = sourcePath.relativize(file).normalize().toString();
                    qualifiedName = qualifiedName.replace("/", ".");
                    qualifiedName = qualifiedName.replace("\\", ".");
                    qualifiedName = qualifiedName.substring(0, qualifiedName.length() - EXT_CLASS.length());
                    discovered.add(new URLClassLocation(sourceURL, qualifiedName));
                }
                return FileVisitResult.CONTINUE;
            }
        });
        return discovered;
    }
}
