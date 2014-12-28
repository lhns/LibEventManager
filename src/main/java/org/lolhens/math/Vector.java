package org.lolhens.math;

import org.lolhens.hash.HashUtil;

public class Vector implements Cloneable {
    private static final TrigonometryCache trigonometryCache = new TrigonometryCache(8);

    public float x, y, z;

    public Vector(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector(Vector vec) {
        this(vec.x, vec.y, vec.z);
    }

    public Vector(float x, float y) {
        this(x, y, 0);
    }

    public Vector(float val) {
        this(val, val, val);
    }

    public Vector() {
        this(0, 0, 0);
    }

    public Vector set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vector set(Vector vec) {
        return set(vec.x, vec.y, vec.z);
    }

    public Vector set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Vector add(Vector vec) {
        return add(vec.x, vec.y, vec.z);
    }

    public Vector addNew(float x, float y, float z) {
        return new Vector(this).add(x, y, z);
    }

    public Vector addNew(Vector vec) {
        return add(vec.x, vec.y, vec.z);
    }

    public Vector mul(float x, float y, float z) {
        return new Vector(this.x * x, this.y * y, this.z * z);
    }

    public Vector mul(Vector vec) {
        return mul(vec.x, vec.y, vec.z);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public int xi() {
        return (int) x;
    }

    public int yi() {
        return (int) y;
    }

    public int zi() {
        return (int) z;
    }

    public Vector rotate(float rX, float rY, float rZ, Vector origin) {
        float x = this.x - origin.x, y = this.y - origin.y, z = this.z - origin.z;
        double sX = trigonometryCache.sin(rX), sY = trigonometryCache.sin(rY), sZ = trigonometryCache.sin(rZ);
        double cX = trigonometryCache.cos(rX), cY = trigonometryCache.cos(rY), cZ = trigonometryCache.cos(rZ);
        set((float) (cZ * (cY * x + sY * (sX * y + cX * z)) - sZ * (cX * y - sX * z)),
                (float) (sZ * (cY * x + sY * (sX * y + cX * z)) + cZ * (cX * y - sX * z)),
                (float) (-sY * x + cY * (sX * y + cX * z)));
        return add(origin);
    }

    public Vector rotate(Vector rotation, Vector origin) {
        return rotate(rotation.x, rotation.y, rotation.z, origin);
    }

    public Vector rotateNormal(float x, float y, float z) {
        return rotate(x, y, z, new Vector());
    }

    public Vector rotateNormal(Vector rotation) {
        return rotateNormal(rotation.x, rotation.y, rotation.z);
    }

    @Override
    public Vector clone() {
        return new Vector(this);
    }

    @Override
    public int hashCode() {
        return HashUtil.hashCode(x, y, z);
    }

    @Override
    public boolean equals(Object obj) {
        return HashUtil.equals(this, obj);
    }
}
