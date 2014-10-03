package com.dafttech.stream;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;

public class ByteArrayInputStreamAccess {
    public static Field FIELD_BUF;
    public static Field FIELD_POS;
    public static Field FIELD_MARK;
    public static Field FIELD_COUNT;

    static {
        try {
            FIELD_BUF = ByteArrayInputStream.class.getDeclaredField("buf");
            FIELD_BUF.setAccessible(true);
            FIELD_POS = ByteArrayInputStream.class.getDeclaredField("pos");
            FIELD_POS.setAccessible(true);
            FIELD_MARK = ByteArrayInputStream.class.getDeclaredField("mark");
            FIELD_MARK.setAccessible(true);
            FIELD_COUNT = ByteArrayInputStream.class.getDeclaredField("count");
            FIELD_COUNT.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public ByteArrayInputStream inputStream;

    public ByteArrayInputStreamAccess(ByteArrayInputStream inputStream) {
        this.inputStream = inputStream;
    }

    public byte[] getBuf() {
        try {
            return (byte[]) FIELD_BUF.get(inputStream);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setBuf(byte[] buf) {
        try {
            FIELD_BUF.set(inputStream, buf);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public int getPos() {
        try {
            return (Integer) FIELD_POS.get(inputStream);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void setPos(int pos) {
        try {
            FIELD_POS.set(inputStream, pos);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public int getMark() {
        try {
            return (Integer) FIELD_MARK.get(inputStream);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void setMark(int mark) {
        try {
            FIELD_MARK.set(inputStream, mark);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public int getCount() {
        try {
            return (Integer) FIELD_COUNT.get(inputStream);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void setCount(int count) {
        try {
            FIELD_COUNT.set(inputStream, count);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
