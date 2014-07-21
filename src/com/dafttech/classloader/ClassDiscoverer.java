package com.dafttech.classloader;

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Deprecated
public class ClassDiscoverer {
    public static String[] containers = new String[] { "jar", "zip" };
    public static String classExt = ".class";

    private ContainedFile sourceDir = null;

    @Deprecated
    public ClassDiscoverer(ContainedFile dir) {
        sourceDir = dir;
    }

    @Deprecated
    public ClassDiscoverer(Class<?> sourceClass, String packageName) {
        this(new ClassPathFinder(sourceClass).getPackagePath(packageName));
    }

    @Deprecated
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
            dir.withPackage();
            ContainedFile container = dir.getContainerFile(containers);
            String containedPath = dir.getContainedPath(containers).replace("\\", "/");
            dir.withoutPackage();
            JarFile jarfile = new JarFile(container);
            String name;
            Enumeration<JarEntry> entries = jarfile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                name = ContainedFile.transferPackageSeperator(containedPath, entry.getName());
                if (name.startsWith(containedPath) && !entry.isDirectory() && isClass(name))
                    classes.add(new ContainedFile(container, name));
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
