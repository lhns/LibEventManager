package org.lolhens.math;

/**
 * Created by LolHens on 22.09.2014.
 */
public class TrigonometryCache {
    private static final float cosShift = (float) (Math.PI / 2);

    private final float[] cache;
    private final float multiplier;
    private final boolean interpolate;

    public TrigonometryCache(int resolutionExp, boolean interpolate) {
        this.interpolate = interpolate;
        cache = new float[(int) Math.pow(2, resolutionExp)];
        multiplier = (float) ((1 / (Math.PI * 2)) * cache.length);
        for (int i = 0; i < cache.length; i++) cache[i] = (float) Math.sin(i / multiplier);
    }

    public TrigonometryCache(int resolutionExp) {
        this(resolutionExp, true);
    }

    public TrigonometryCache(boolean interpolate) {
        this(8, interpolate);
    }

    public TrigonometryCache() {
        this(8, true);
    }

    public float sin(float val) {
        float index = val * multiplier;
        int floor = (int) index;
        int mod = floor & (cache.length - 1);
        if (interpolate)
            return cache[mod] + (index - floor) * (cache[mod + 1 == cache.length ? 0 : mod + 1] - cache[mod]);
        return cache[mod];
    }

    public float cos(float val) {
        return sin(val + cosShift);
    }
}
