package org.lolhens.storage.mappedbuffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by LolHens on 15.01.2015.
 */
public class MappedBuffer {
    protected final ByteBuffer byteBuffer;
    private int size;
    public final RegionList mapped = new RegionList();

    public MappedBuffer(ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
        this.size = 100;
    }

    public MappedBuffer(int capacity) {
        this(ByteBuffer.allocate(capacity).order(ByteOrder.nativeOrder()));
    }

    public MappedRegion allocate(int length) {
        return allocate(length, true);
    }

    public MappedRegion allocate(int length, boolean defrag) {
        int lastEnd = 0;
        for (Region region : mapped.getRegions()) {
            if (region.getPosition() - (lastEnd + 1) >= length) break;
            lastEnd = region.getEnd();
        }

        if (lastEnd + length < size) {
            MappedRegion mappedRegion = new MappedRegion(this, lastEnd + 1, length);
            System.out.println(mappedRegion.getPosition() + " " + mappedRegion.getLength());
            mapped.add(new Region(mappedRegion.getPosition(), mappedRegion.getLength()));
            onChange(mappedRegion.getPosition(), mappedRegion.getLength());
            return mappedRegion;
        } else if (defrag) {
            defrag();
            return allocate(length, false);
        } else {
            return null;
        }
    }

    protected void deallocate(MappedRegion mappedRegion) {
        mapped.remove(mappedRegion);
        onChange(mappedRegion.getPosition(), mappedRegion.getLength());
    }

    private void defrag() {
        // TODO: deallocate regions and reallocate them
    }

    protected void onChange(int position, int length) {

    }
}
