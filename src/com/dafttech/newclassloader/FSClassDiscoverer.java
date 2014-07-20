package com.dafttech.newclassloader;

import static com.dafttech.newclassloader.URLClassLocation.EXT_CLASS;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FSClassDiscoverer {
    private URL target;
    private Set<URLClassLocation> discoveredClasses = new HashSet<URLClassLocation>();

    public FSClassDiscoverer(URL target) {
        this.target = target;
    }

    private void discover(URL url) {
        DiscoverStrategy.forURLProtocol(url.getProtocol()).discover(this, url);
    }

    private void addClassLocation(URL classURL) {
        String qualifiedName = classURL.getPath().replaceFirst(target.getPath(), "").replace(EXT_CLASS, "").replace("/", ".");
        // TODO: replacelast EXT_CLASS ""
        discoveredClasses.add(new URLClassLocation(target, qualifiedName));
    }

    private static abstract class DiscoverStrategy {
        public abstract void discover(FSClassDiscoverer classDiscoverer, URL url);

        public static final DiscoverStrategy discoverDir = new DiscoverDir();
        public static final DiscoverStrategy discoverJar = new DiscoverJar();
        public static final DiscoverStrategy NullDiscoverStrategy = new NullDiscoverStrategy();

        public static DiscoverStrategy forURLProtocol(String protocol) {
            switch (protocol) {
            case "file":
                return discoverDir;
            case "jar":
                return discoverJar;
            case "rsrc":
                return discoverJar;
            }
            return NullDiscoverStrategy;
        }
    }

    private static class DiscoverDir extends DiscoverStrategy {
        @Override
        public void discover(FSClassDiscoverer classDiscoverer, URL url) {
            File file = new File(url.getFile());
            for (File child : file.listFiles()) {
                if (child.isFile()) {
                    if (child.getName().endsWith(EXT_CLASS)) {
                        try {
                            classDiscoverer.addClassLocation(child.toURI().toURL());
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    classDiscoverer.discover(url);
                }
            }
        }
    }

    private static class DiscoverJar extends DiscoverStrategy {
        @Override
        public void discover(FSClassDiscoverer classDiscoverer, URL url) {
            File file = new File(url.getFile());
            try {
                JarFile jarfile = new JarFile(file);

                Enumeration<JarEntry> entries = jarfile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    if (!entry.isDirectory() && entry.getName().endsWith(EXT_CLASS)) {
                        try {
                            classDiscoverer.addClassLocation(new URL(url.getPath() + entry.getName()));
                        } catch (MalformedURLException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                jarfile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class NullDiscoverStrategy extends DiscoverStrategy {
        @Override
        public void discover(FSClassDiscoverer classDiscoverer, URL url) {
        }
    }
}
