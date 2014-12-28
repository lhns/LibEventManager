package org.lolhens.math;

/**
 * Created by LolHens on 22.09.2014.
 */
public class TrigonometryCache {
    private static final float cosShift = (float) (Math.PI / 2);

    private final float[] cache;
    private final float multiplier;

    public TrigonometryCache(int resolution) {
        cache = new float[(int) Math.pow(2, resolution)];
        multiplier = (float) ((1 / (Math.PI * 2)) * cache.length);
        for (int i = 0; i < cache.length; i++) cache[i] = (float) Math.sin(i / multiplier);
    }

    public float sin(float val) {
        float index = val * multiplier;
        int floor = (int) index;
        int mod = floor & (cache.length - 1);
        return cache[mod] + (index - floor) * (cache[mod + 1 == cache.length ? 0 : mod + 1] - cache[mod]);
    }

    public float cos(float val) {
        return sin(val + cosShift);
    }
}
