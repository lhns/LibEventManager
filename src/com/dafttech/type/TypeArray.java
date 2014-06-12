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
}
