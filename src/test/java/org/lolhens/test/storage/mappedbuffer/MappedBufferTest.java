package org.lolhens.test.storage.mappedbuffer;

import org.lolhens.storage.mappedbuffer.MappedBuffer;
import org.lolhens.storage.mappedbuffer.Region;
import org.lolhens.storage.mappedbuffer.RegionList;

/**
 * Created by LolHens on 10.02.2015.
 */
public class MappedBufferTest {
    public static void main(String[] args) {
        RegionList regionList = new RegionList();

        regionList.add(new Region(0, 10));

        regionList.test();

        regionList.add(new Region(10, 10));

        regionList.test();

        regionList.add(new Region(20, 10));

        regionList.test();

        regionList.remove(new Region(7, 1));

        regionList.test();

        MappedBuffer b = new MappedBuffer(null);

        b.mapped.add(new Region(0, 10));
        b.mapped.add(new Region(20, 10));
        b.mapped.test();

        b.allocate(71, false);

        b.mapped.test();

    }
}
