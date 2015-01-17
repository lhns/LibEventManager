package org.lolhens.storage.mappedbuffer;

/**
 * Created by LolHens on 15.01.2015.
 */
public class MappedRegion {
    private final MappedBuffer mappedBuffer;
    private final int position;
    private final int length;

    protected MappedRegion(MappedBuffer mappedBuffer, int position, int length) {
        this.mappedBuffer = mappedBuffer;
        this.position = position;
        this.length = length;

    }

    public void putInt() {

    }
}
