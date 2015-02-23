package org.lolhens.test.storage.mappedbuffer;

import org.lolhens.storage.mappedbuffer.Region;
import org.lolhens.storage.mappedbuffer.RegionList;

/**
 * Created by LolHens on 10.02.2015.
 */
public class MappedBufferTest {
    public static void main(String[] args) {
        Region r = new Region(34, 10);
        r.mergeWith(new Region(23, 10));
        System.out.println(r.getPosition() + " - "+r.getEnd());
        
        

        RegionList regionList = new RegionList();

        regionList.add(new Region(20, 100));
        regionList.test();

        regionList.add(new Region(30, 10));
        regionList.test();

        regionList.add(new Region(40, 10));
        regionList.test();
    }
}
