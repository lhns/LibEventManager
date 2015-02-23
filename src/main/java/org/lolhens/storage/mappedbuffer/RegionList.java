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
        Region region = null;

        // Get last region with a lower position than newRegion
        ListIterator<Region> i = regions.listIterator();
        while (i.hasNext()) {
            //last = region;
            region = i.next();
            if (region.getPosition() > newRegion.getEnd()) {
                i.previous();
                break;
            }
            if (region.isTouching(newRegion)) {
                i.remove();
                newRegion.mergeWith(region);
            }
        }

        i.add(newRegion);
    }

    public void remove(Region newRegion) { // TODO: Fix with new region methods
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
