package org.lolhens.test.classfile;

import org.lolhens.classfile.URLClassLocation;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ClassLoaderExample {
    public static void main(String[] args) {
        Class<?> loadedClass;
        try {
            for (URLClassLocation ucl : new URLClassLocation(ClassLoaderExample.class).discoverSourceURL()) {
                if (ucl.getQualifiedName().toLowerCase().startsWith("com.dafttech")) {
                    loadedClass = ucl.loadClass(ClassLoaderExample.class.getClassLoader());
                    if (!loadedClass.isInterface() && IRunnableModule.class.isAssignableFrom(loadedClass)) {
                        try {
                            Constructor<?> constructor = loadedClass.getDeclaredConstructor();
                            constructor.setAccessible(true);
                            Object newInstance = constructor.newInstance();
                            IRunnableModule module = (IRunnableModule) newInstance;
                            module.run();
                        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | InstantiationException | IllegalArgumentException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
}
