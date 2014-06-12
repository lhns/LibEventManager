package com.dafttech.type;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TypeArray extends Type<Object[]> {
    protected TypeArray(boolean prototype) {
        super(prototype);
    }

    @Override
    public byte[] toByteArray() {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byte[] tempArray;
        for (Object obj : value) {
            try {
                tempArray = Type.forObject(obj).toByteArray();
                byteStream.write(Type.INTEGER.create(tempArray.length).toByteArray());
                byteStream.write(tempArray);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return byteStream.toByteArray();
    }

    @Override
    public TypeArray fromByteArray(byte... array) {
        return null;
    }

    @Override
    public Class<?> getTypeClass() {
        return Object[].class;
    }

    @Override
    public Object getNullObject() {
        return null;
    }

    @Override
    public boolean isType(Object obj) {
        return obj != null && obj.getClass().isArray();
    }

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
