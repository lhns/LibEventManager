package org.lolhens.storage.mappedbuffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;
import java.util.List;

/**
 * Created by LolHens on 15.01.2015.
 */
public class MappedBuffer {
    private final ByteBuffer byteBuffer;
    private final RegionList mappedRegions = new RegionList();

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
}
