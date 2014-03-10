package com.dafttech.classloader;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassFileLoader {
    private List<ContainedFile> classFiles;
    private ClassLoader parentLoader;

    public ClassFileLoader(List<ContainedFile> classFiles) {
        this.classFiles = classFiles;
        parentLoader = getClass().getClassLoader();
    }

    public ClassFileLoader(ContainedFile dir) {
        this(new ClassDiscoverer(dir).discover());
    }

    public ClassFileLoader(Class<?> sourceClass, String packageName) {
        this(new ClassDiscoverer(sourceClass, packageName).discover());
    }

    public ClassFileLoader setParentClassLoader(ClassLoader parentLoader) {
        this.parentLoader = parentLoader;
        return this;
    }

    public List<Class<?>> loadClasses() {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        if (parentLoader == null) parentLoader = ClassDiscoverer.class.getClassLoader();
        String path, packageName;
        Map<String, List<String>> sortedFiles = new HashMap<String, List<String>>();
        for (ContainedFile classFile : classFiles) {
            path = classFile.getWithoutPackage();
            packageName = classFile.getPackage();
            if (packageName != null) {
                if (!sortedFiles.containsKey(path)) sortedFiles.put(path, new ArrayList<String>());
                sortedFiles.get(path).add(packageName);
            }
        }
        Class<?> loadedClass;
        for (String sortedPath : sortedFiles.keySet()) {
            try {
                URL[] urls = { new ContainedFile(sortedPath).toURI().toURL() };
                URLClassLoader classLoader = URLClassLoader.newInstance(urls, parentLoader);
                for (String sortedPackageName : sortedFiles.get(sortedPath)) {
                    if (sortedPackageName.toLowerCase().endsWith(ClassDiscoverer.classExt)) {
                        loadedClass = null;
                        try {
                            loadedClass = classLoader.loadClass(sortedPackageName.substring(0, sortedPackageName.length()
                                    - ClassDiscoverer.classExt.length()));
                        } catch (ClassNotFoundException e) {
                            try {
                                loadedClass = classLoader.loadClass(sortedPackageName);
                            } catch (ClassNotFoundException e1) {
                                e1.printStackTrace();
                            }
                        }
                        if (loadedClass != null && loadedClass != ClassDiscoverer.class) classes.add(loadedClass);
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return classes;
    }
}
