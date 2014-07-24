package com.dafttech.classfile.test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.dafttech.classfile.URLClassLocation;

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
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (SecurityException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
