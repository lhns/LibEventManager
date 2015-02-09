package org.lolhens.storage.mappedbuffer;

/**
 * Created by LolHens on 09.02.2015.
 */
public class Region {
    private final int position;
    private final int length;

    public Region(int position, int length) {
        this.position = position;
        this.length = length;
    }

    public int getPosition() {
        return position;
    }

    public int getLength() {
        return length;
    }

    public int getEnd() {
        return position + length;
    }
}
