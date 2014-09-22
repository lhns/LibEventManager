package com.dafttech.math;

/**
 * Created by LolHens on 22.09.2014.
 */
public class TrigonometryCache {
    private float[] cache;

    public TrigonometryCache(int resolution) {
        cache = new float[(int) Math.pow(2, resolution)];
        float multiplier = (float) ((1d / (double) cache.length) * Math.PI * 2);
        for (int i = 0; i < cache.length; i++) cache[i] = (float) Math.cos((float) i * multiplier);
    }

    public float cos(float val) {
        int floor = (int) val;
        int mod = floor & (cache.length - 1);
        float val1 = cache[mod];
        float val2 = cache[mod == cache.length ? 0 : mod];
        float interpolation = val - floor;
        return val1 + interpolation * (val2 - val1);
    }

    public float sin(float val) {
        return cos((float) (val + Math.PI / 2));
    }
}
