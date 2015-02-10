package org.lolhens.storage.mappedbuffer;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by LolHens on 15.01.2015.
 */
public class RegionList {
    private List<Region> regions = new LinkedList<>();

    public void add(Region newRegion) {
        int position = newRegion.getPosition(), end = newRegion.getEnd();
        Region region = null, last;

        // Get last region with a lower position than newRegion
        ListIterator<Region> i = regions.listIterator();
        while (i.hasNext()) {
            last = region;
            region = i.next();
            if (region.getPosition() > position) {
                i.previous();
                region = last;
                break;
            }
        }

        if (region != null && region.getEnd() >= position) {
            position = region.getPosition();
            i.remove();
        }

        while (i.hasNext()) {
            region = i.next();

            if (region.getPosition() <= end) {
                end = Math.max(end, region.getEnd());
                i.remove();
            } else {
                i.previous();
                break;
            }
        }

        i.add(new Region(position, end - position));
    }

    public void remove(Region newRegion) {
        Region region = null, last;

        ListIterator<Region> i = regions.listIterator();
        while (i.hasNext()) {
            last = region;
            region = i.next();
            if (region.getPosition() >= newRegion.getPosition()) {
                i.previous();
                region = last;
                break;
            }
        }


    }

    public void test() {
        for (Region region : regions) System.out.print(region.getPosition() + " - " + region.getEnd() + ", ");
        System.out.println();
    }
}
