package org.lolhens.test.storage.mappedbuffer;

import org.lolhens.storage.mappedbuffer.Region;
import org.lolhens.storage.mappedbuffer.RegionList;

/**
 * Created by LolHens on 10.02.2015.
 */
public class MappedBufferTest {
    public static void main(String[] args) {
        System.out.println(new Region(34, 10).isTouching(new Region(22, 10)));
        
        RegionList regionList = new RegionList();

        regionList.add(new Region(20, 100));
        regionList.test();
        
        regionList.add(new Region(30, 10));
        regionList.test();

        regionList.add(new Region(40, 10));
        regionList.test();
    }
}
