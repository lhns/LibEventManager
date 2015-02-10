package org.lolhens.storage.mappedbuffer;

/**
 * Created by LolHens on 09.02.2015.
 */
public class Region {
    private volatile int position;
    private volatile int length;

    public Region(int position, int length) {
        this.position = position;
        this.length = length;
    }

    public boolean isTouching(Region region) {
        return region.getEnd() + 1 >= getPosition() && region.getPosition() - 1 <= getEnd();
    }

    public boolean isIntersecting(Region region) {
        return region.getEnd() >= getPosition() && region.getPosition() <= getEnd();
    }

    public void mergeWith(Region region) {
        setEnd(Math.max(getEnd(), region.getEnd()));
        setPosition(Math.min(getPosition(), region.getPosition()));
    }

    // Setters

    public void setPosition(int position) {
        this.position = position;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setEnd(int end) {
        this.length = end - position;
    }

    // Getters

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
