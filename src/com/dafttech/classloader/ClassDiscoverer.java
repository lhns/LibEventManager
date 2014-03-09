package com.dafttech.classloader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassDiscoverer {
    public static String[] containers = new String[] { "jar", "zip" };
    public static String classExt = ".class";

    private ContainedFile sourceDir = null;

    public ClassDiscoverer(ContainedFile dir) {
        sourceDir = dir;
    }

    public ClassDiscoverer(Class<?> sourceClass, String packageName) {
        this(getPackagePath(sourceClass, packageName));
    }

    public List<ContainedFile> discover() {
        List<ContainedFile> classes = new LinkedList<ContainedFile>();
        discover(sourceDir, classes);
        return classes;
    }

    private void discover(ContainedFile dir, List<ContainedFile> classes) {
        if (isClass(dir.getPath())) {
            System.out.println(dir.toString());
            classes.add(dir);
        } else if (dir.isDirectory()) {
            discoverDir(dir, classes);
        } else if (dir.isContained(containers)) {
            discoverJar(dir, classes);
        }
    }

    private void discoverDir(ContainedFile dir, List<ContainedFile> classes) {
        for (ContainedFile file : dir.listFiles()) {
            discover(file, classes);
        }
    }

    private void discoverJar(ContainedFile dir, List<ContainedFile> classes) {
        try {
            ContainedFile container = dir.getContainerFile(containers);
            String containedPath = dir.getContainedFile(containers).getPath().replace("\\", "/");
            JarFile jarfile = new JarFile(container);
            Enumeration<JarEntry> entries = jarfile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().startsWith(containedPath) && !entry.isDirectory() && isClass(entry.getName()))
                    classes.add(new ContainedFile(container, entry.getName()));
            }
            jarfile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isClass(String name) {
        return name.endsWith(classExt);
    }

    public static List<Class<?>> loadClasses(List<ContainedFile> classFiles, ClassLoader parentLoader) {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        if (parentLoader == null) parentLoader = ClassDiscoverer.class.getClassLoader();
        String path, file;
        Map<String, List<String>> sortedFiles = new HashMap<String, List<String>>();
        for (ContainedFile classFile : classFiles) {
            if (classFile.isContained(containers)) {
                path = classFile.getContainerPath(containers);
                file = classFile.getContainedPath(containers);
            } else {
                path = classFile.getParent();
                file = classFile.getName();
            }
            if (file != null) {
                if (!sortedFiles.containsKey(path)) sortedFiles.put(path, new ArrayList<String>());
                sortedFiles.get(path).add(file);
            }
        }
        Class<?> loadedClass;
        for (String sortedPath : sortedFiles.keySet()) {
            try {
                URL[] urls = { new ContainedFile(sortedPath).toURI().toURL() };
                URLClassLoader classLoader = URLClassLoader.newInstance(urls, parentLoader);
                for (String sortedFile : sortedFiles.get(sortedPath)) {
                    loadedClass = null;
                    try {
                        loadedClass = classLoader.loadClass(sortedFile);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (loadedClass != null && loadedClass != ClassDiscoverer.class) {
                        classes.add(loadedClass);
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return classes;
    }

    public static ContainedFile getClassPath(Class<?> context) {
        URL url = context.getResource("/" + context.getName().replace(".", "/") + classExt);
        if (url != null) {
            String path = url.toString();
            if (path.startsWith("rsrc:")) {
                path = "jar:file:/" + new File(System.getProperty("java.class.path")).getAbsolutePath().replace("\\", "/") + "!/"
                        + path.substring(5);
            }
            int packageIndex = path.length() - (context.getName() + classExt).length();
            return ContainedFile.fromPackage(path.substring(0, packageIndex), path.substring(packageIndex));
        }
        return null;
    }

    public static ContainedFile getPackagePath(Class<?> context, String packageName) {
        ContainedFile path = getClassPath(context);
        if (path != null) return ContainedFile.fromPackage(path.getWithoutPackage(), packageName);
        return null;
    }
}
