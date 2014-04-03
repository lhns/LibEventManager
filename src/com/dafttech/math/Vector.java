package com.dafttech.math;

public class Vector implements Cloneable {
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

    public Vector rotate(float x, float y, float z, Vector origin) {
        float myX = this.x - origin.x, myY = this.y - origin.y, myZ = this.z - origin.z;
        double sinX = Math.sin(x), sinY = Math.sin(y), sinZ = Math.sin(z);
        double cosX = Math.cos(x), cosY = Math.cos(y), cosZ = Math.cos(z);
        set((float) (cosZ * (cosY * myX + sinY * (sinX * myY + cosX * myZ)) - sinZ * (cosX * myY - sinX * myZ)), (float) (sinZ
                * (cosY * myX + sinY * (sinX * myY + cosX * myZ)) + cosZ * (cosX * myY - sinX * myZ)),
                (float) (-sinY * myX + cosY * (sinX * myY + cosX * myZ)));
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
}
