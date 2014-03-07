package com.dafttech.classloader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassLoader {
    private static final String jar = "jar";
    private static final String classExt = ".class";

    private File sourceDir = null;
    private String sourcePackage = "";
    private boolean isJarfile = false;
    private List<Class<?>> loaded = new ArrayList<Class<?>>();
    private boolean canLoadItself = false;

    public ClassLoader(ContainedFile dir, String packageFilter) {
        String path = dir.toString().replace("\\", "/");
        sourcePackage = packageFilter.replace("\\", "/").replace("/", ".");
        if (dir.isContained("jar")) {
            isJarfile = true;
            sourceDir = dir.getContainerFile(jar);
        } else {
            sourceDir = new File(path);
        }
    }

    public ClassLoader(Class<?> sourceClass, String packageFilter) {
        this(getPackagePath(sourceClass, ""), packageFilter);
    }

    public ClassLoader load() {
        if (isJarfile) {
            loadJar(sourceDir);
        } else {
            loadDir(sourceDir);
        }
        return this;
    }

    public List<Class<?>> getLoaded() {
        return loaded;
    }

    private void loadDir(File dir) {
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) return;
        for (File file : files) {
            if (file.isDirectory()) {
                loadDir(file);
            } else if (file.getName().endsWith(classExt)) {
                loadClass(file);
            }
        }
    }

    private void loadJar(File dir) {
        try {
            JarFile jarfile = new JarFile(dir);
            Enumeration<JarEntry> entries = jarfile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (!entry.isDirectory() && entry.getName().endsWith(classExt)) {
                    loadClass(new File(dir.getAbsolutePath() + "/" + entry.getName()));
                }
            }
            jarfile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadClass(File dir) {
        boolean isClass = dir.toString().endsWith(classExt);
        String currPackage = getCurrentPackage(dir);
        if (currPackage.startsWith(sourcePackage)) {
            if (isClass) currPackage = currPackage.substring(0, currPackage.length() - 6);
            try {
                URL[] urls = { sourceDir.toURI().toURL() };
                URLClassLoader classloader = URLClassLoader.newInstance(urls, getClass().getClassLoader());
                Class<?> tmpClass = classloader.loadClass(currPackage);
                if (tmpClass != null && (canLoadItself || tmpClass != getClass())) loaded.add(tmpClass);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (NoClassDefFoundError e) {
            }
        }
    }

    private String getCurrentPackage(File dir) {
        if (!dir.getAbsolutePath().startsWith(sourceDir.getAbsolutePath())
                || dir.getAbsolutePath().length() <= sourceDir.getAbsolutePath().length()) return "";
        return dir.getAbsolutePath().substring(sourceDir.getAbsolutePath().length() + 1).replace("\\", "/").replace("/", ".");
    }

    public static ContainedFile getClassPath(Class<?> context) {
        URL url = context.getResource("/" + context.getName().replace(".", "/") + classExt);
        if (url != null) {
            String path = url.toString();
            if (path.startsWith("rsrc:")) {
                path = "jar:file:/" + new File(System.getProperty("java.class.path")).getAbsolutePath().replace("\\", "/") + "!/"
                        + path.substring(5);
            }
            return new ContainedFile(path);
        }
        return null;
    }

    public static ContainedFile getPackagePath(Class<?> context, String packageName) {
        File path = getClassPath(context);
        if (path != null) {
            String packagePath = path.toString();
            packagePath = packagePath.substring(0, packagePath.length() - (context.getName() + classExt).length());
            return new ContainedFile(packagePath, packageName.replace(".", "/"));
        }
        return null;
    }
}
