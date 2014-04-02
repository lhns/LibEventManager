package com.dafttech.math;

public class Vector {
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

    public int xInt() {
        return (int) x;
    }

    public int yInt() {
        return (int) y;
    }

    public int zInt() {
        return (int) z;
    }

    public Vector rotate(float x, float y, float z, Vector origin) {
        float myX = this.x - origin.x, myY = this.y - origin.y, myZ = this.z - origin.z;
        set((float) (Math.cos(z) * (Math.cos(y) * myX + Math.sin(y) * (Math.sin(x) * myY + Math.cos(x) * myZ)) - Math.sin(z)
                * (Math.cos(x) * myY - Math.sin(x) * myZ)),
                (float) (Math.sin(z) * (Math.cos(y) * myX + Math.sin(y) * (Math.sin(x) * myY + Math.cos(x) * myZ)) + Math.cos(z)
                        * (Math.cos(x) * myY - Math.sin(x) * myZ)), (float) (-Math.sin(y) * myX + Math.cos(y)
                        * (Math.sin(x) * myY + Math.cos(x) * myZ)));
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

}
