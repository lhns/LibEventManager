package org.lolhens.storage.mappedbuffer;

import org.lolhens.primitive.PrimitiveUtil;

/**
 * Created by LolHens on 15.01.2015.
 */
public class MappedRegion extends Region {
    private final MappedBuffer mappedBuffer;

    protected MappedRegion(MappedBuffer mappedBuffer, int position, int length) {
        super(position, length);
        this.mappedBuffer = mappedBuffer;
    }


    public byte get(int index) {
        checkBounds(index, PrimitiveUtil.BYTE.size);

        return mappedBuffer.byteBuffer.get(getPosition() + index);
    }

    public MappedRegion put(int index, byte b) {
        checkBounds(index, PrimitiveUtil.BYTE.size);

        mappedBuffer.byteBuffer.put(getPosition() + index, b);
        return this;
    }


    public MappedRegion get(int index, byte[] array, int offset, int length) {
        if (length < 0) length = array.length;

        checkBounds(index, length);

        synchronized (mappedBuffer.byteBuffer) {
            mappedBuffer.byteBuffer.position(getPosition() + index);
            mappedBuffer.byteBuffer.get(array, offset, length);
        }
        return this;
    }

    public MappedRegion put(int index, byte[] array, int offset, int length) {
        if (length < 0) length = array.length;

        checkBounds(index, length);

        synchronized (mappedBuffer.byteBuffer) {
            mappedBuffer.byteBuffer.position(getPosition() + index);
            mappedBuffer.byteBuffer.put(array, offset, length);
        }
        return this;
    }


    public char getChar(int index) {
        checkBounds(index, PrimitiveUtil.CHARACTER.size);

        return mappedBuffer.byteBuffer.getChar(getPosition() + index);
    }

    public MappedRegion putChar(int index, char value) {
        checkBounds(index, PrimitiveUtil.CHARACTER.size);

        mappedBuffer.byteBuffer.putChar(getPosition() + index, value);
        return this;
    }


    public short getShort(int index) {
        checkBounds(index, PrimitiveUtil.SHORT.size);

        return mappedBuffer.byteBuffer.getShort(getPosition() + index);
    }

    public MappedRegion putShort(int index, short value) {
        checkBounds(index, PrimitiveUtil.SHORT.size);

        mappedBuffer.byteBuffer.putShort(getPosition() + index, value);
        return this;
    }


    public int getInt(int index) {
        checkBounds(index, PrimitiveUtil.INTEGER.size);

        return mappedBuffer.byteBuffer.getInt(getPosition() + index);
    }

    public MappedRegion putInt(int index, int value) {
        checkBounds(index, PrimitiveUtil.INTEGER.size);

        mappedBuffer.byteBuffer.putInt(getPosition() + index, value);
        return this;
    }


    public long getLong(int index) {
        checkBounds(index, PrimitiveUtil.LONG.size);

        return mappedBuffer.byteBuffer.getLong(getPosition() + index);
    }

    public MappedRegion putLong(int index, long value) {
        checkBounds(index, PrimitiveUtil.LONG.size);

        mappedBuffer.byteBuffer.putLong(getPosition() + index, value);
        return this;
    }


    public float getFloat(int index) {
        checkBounds(index, PrimitiveUtil.FLOAT.size);

        return mappedBuffer.byteBuffer.getFloat(getPosition() + index);
    }

    public MappedRegion putFloat(int index, float value) {
        checkBounds(index, PrimitiveUtil.FLOAT.size);

        mappedBuffer.byteBuffer.putFloat(getPosition() + index, value);
        return this;
    }


    public double getDouble(int index) {
        checkBounds(index, PrimitiveUtil.DOUBLE.size);

        return mappedBuffer.byteBuffer.getDouble(getPosition() + index);
    }

    public MappedRegion putDouble(int index, double value) {
        checkBounds(index, PrimitiveUtil.DOUBLE.size);

        mappedBuffer.byteBuffer.putDouble(getPosition() + index, value);
        return this;
    }


    private void checkBounds(int index, int size) {
        if (index + size > getLength()) throw new IndexOutOfBoundsException();
    }
}
