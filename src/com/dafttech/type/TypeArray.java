package com.dafttech.type;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TypeArray<ArrayType extends Type<?>> extends Type<Object[]> {
    public TypeArray(Object[] value) {
        super(value);
    }

    @Override
    public byte[] toByteArray() {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byte[] tempArray;
        for (Object obj : value) {
            try {
                tempArray = Type.forObject(obj).toByteArray();
                byteStream.write(new TypeInteger(tempArray.length).toByteArray());
                byteStream.write(tempArray);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return byteStream.toByteArray();
    }

    @Override
    public Object[] fromByteArray(byte... array) {
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
}
