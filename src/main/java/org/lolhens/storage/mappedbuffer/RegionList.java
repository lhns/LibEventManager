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
        if (newRegion.getLength() <= 0) return;

        Region region = null;

        // Get last region with a lower position than newRegion
        ListIterator<Region> i = regions.listIterator();
        while (i.hasNext()) {
            region = i.next();

            if (region.getPosition() - 1 > newRegion.getEnd()) {
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

    public void remove(Region newRegion) {
        if (newRegion.getLength() <= 0) return;

        Region region = null;

        // Get last region with a lower position than newRegion
        ListIterator<Region> i = regions.listIterator();
        while (i.hasNext()) {
            region = i.next();

            if (region.getPosition() > newRegion.getEnd()) {
                break;
            }

            if (region.isIntersecting(newRegion)) {
                i.remove();
                for (Region cutRegion : region.cutWith(newRegion)) i.add(cutRegion);
            }
        }
    }

    public List<Region> getRegions() {
        return regions;
    }

    public void test() {
        for (Region region : regions) System.out.print(region.getPosition() + " - " + region.getEnd() + ", ");
        System.out.println();
    }
}
