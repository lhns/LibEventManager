package com.dafttech.classloader;

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
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
        this(new ClassPathFinder(sourceClass).getPackagePath(packageName));
    }

    public List<ContainedFile> discover() {
        List<ContainedFile> classes = new LinkedList<ContainedFile>();
        discover(sourceDir, classes);
        return classes;
    }

    private void discover(ContainedFile dir, List<ContainedFile> classes) {
        if (isClass(dir.getPath())) {
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
}
