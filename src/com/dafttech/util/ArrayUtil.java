package com.dafttech.util;

public class ArrayUtil {
    public static double interpolate(double[] array, int[] translation, double index) {
        int length = translation == null ? array.length : translation.length;
        int floor = (int) index;
        int[] intIndex = new int[] { floor % length, (floor + 1) % length };
        if (translation != null) for (int i = 0; i < intIndex.length; i++)
            intIndex[i] = translation[intIndex[i]];
        double[] value = new double[] { array[intIndex[0]], array[intIndex[1]] };
        double percent = index - floor;
        return value[0] + percent * (value[1] - value[0]);
    }

    public static double interpolate(double[] array, double index) {
        return interpolate(array, null, index);
    }
}
