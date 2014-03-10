package com.dafttech.classloader;

import java.io.File;
import java.net.URL;

public class ClassPathFinder {
    private Class<?> context;

    public ClassPathFinder(Class<?> context) {
        this.context = context;
    }

    public ContainedFile getClassPath() {
        URL url = context.getResource("/" + context.getName().replace(".", "/") + ClassDiscoverer.classExt);
        if (url != null) {
            String path = url.toString();
            if (path.startsWith("rsrc:")) {
                path = "jar:file:/" + new File(System.getProperty("java.class.path")).getAbsolutePath().replace("\\", "/") + "!/"
                        + path.substring(5);
            }
            int packageIndex = path.length() - (context.getName() + ClassDiscoverer.classExt).length();
            return ContainedFile.fromPackage(path.substring(0, packageIndex), path.substring(packageIndex));
        }
        return null;
    }

    public ContainedFile getPackagePath(String packageName) {
        ContainedFile path = getClassPath();
        if (path != null) return ContainedFile.fromPackage(path.getWithoutPackage(), packageName);
        return null;
    }
}
