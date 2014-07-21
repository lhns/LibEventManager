package com.dafttech.classfile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
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
        this(getClassSourceURL(target), target.getName());
    }

    private static URL getClassSourceURL(Class<?> target) {
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
            e.printStackTrace();
        }
        return null;
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

    public Set<URLClassLocation> discoverSourceURL() {
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

    public static Set<URLClassLocation> discoverSourceURL(final URL sourceURL) {
        final Set<URLClassLocation> discovered = new HashSet<URLClassLocation>();
        try {
            final Path sourcePath;
            if (!sourceURL.toString().contains("!")) {
                System.out.println(sourceURL);
                System.out.println(sourceURL.toURI());
                sourcePath = Paths.get(sourceURL.toURI());
            } else {
                // JAVA BUG WORKAROUND
                String splitURL[] = sourceURL.toString().split("!");
                FileSystem fs = FileSystems.newFileSystem(URI.create(splitURL[0]), new HashMap<String, String>());
                sourcePath = fs.getPath(splitURL[1]);
            }
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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return discovered;
    }
}
