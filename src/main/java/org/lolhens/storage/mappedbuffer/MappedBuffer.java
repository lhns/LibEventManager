package org.lolhens.storage.mappedbuffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by LolHens on 15.01.2015.
 */
public class MappedBuffer {
    protected final ByteBuffer byteBuffer;
    private final RegionList mapped = new RegionList();

    public MappedBuffer(ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;

    }

    public MappedBuffer(int capacity) {
        this(ByteBuffer.allocate(capacity).order(ByteOrder.nativeOrder()));
    }

    public MappedRegion allocate(int length) {
        return null;
    }

    protected void deallocate(MappedRegion mappedRegion) {

    }

    protected void onChange(int position, int length) {

    }
}
