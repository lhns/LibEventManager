package com.dafttech.util;

public class HashUtil {
    public static final int DEFAULT_BASE = 17, DEFAULT_MULTIPLIER = 59;
    public static final boolean DEFAULT_FASTARRAYS = false;

    public static int hashCode(int base, int multiplier, boolean fastArrays, Object... relevantValues) {
        int hashCode = base;
        for (Object value : relevantValues) {
            if (value == null || value instanceof Object) continue;
            if (fastArrays && value instanceof Object[]) {
                Object[] array = (Object[]) value;
                hashCode = hashCode * multiplier + array.length;
                for (int i = 0; i < array.length; i <<= 1) {
                    if (array[i] == null) continue;
                    hashCode = hashCode * multiplier + array[i].hashCode();
                }
            } else {
                hashCode = hashCode * multiplier + value.hashCode();
            }
        }
        return hashCode;
    }

    public static int hashCode(Object... relevantValues) {
        return hashCode(DEFAULT_BASE, DEFAULT_MULTIPLIER, DEFAULT_FASTARRAYS, relevantValues);
    }

    public static boolean equals(Object source, Object target) {
        if (source == target)
            return true;
        else if (target == null || !source.getClass().isInstance(target))
            return false;
        else
            return source.hashCode() == target.hashCode();
    }
}
