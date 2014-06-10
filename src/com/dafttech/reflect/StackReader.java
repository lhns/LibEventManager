package com.dafttech.reflect;

import java.lang.reflect.Method;

public class StackReader {
    @SuppressWarnings("unused")
    private Thread thread;
    private StackTraceElement[] stackTrace;

    public StackReader(Thread thread) {
        this.thread = thread;
        stackTrace = thread.getStackTrace();
    }

    public StackTraceElement[] getStackTrace() {
        return stackTrace;
    }

    public Class<?>[] getClasses() {
        Class<?>[] classes = new Class[stackTrace.length];
        for (int i = 0; i < stackTrace.length; i++)
            classes[i] = stackTrace[i].getClass();
        return classes;
    }

    public String[] getMethodNames() {
        String[] methodNames = new String[stackTrace.length];
        for (int i = 0; i < stackTrace.length; i++)
            methodNames[i] = stackTrace[i].getMethodName();
        return methodNames;
    }

    // TODO: Make it work!
    @Deprecated
    public Method[] getMethods() {
        Method[] methods = new Method[stackTrace.length];
        for (int i = 0; i < stackTrace.length; i++) {
            try {
                methods[i] = stackTrace[i].getClass().getDeclaredMethod(stackTrace[i].getMethodName());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
        return methods;
    }
}
