package com.tsoft.myprocad.util.linealg;

/** 2-element single-precision vector */

public class Vec2 {
    private float x;
    private float y;

    public Vec2() {}

    public Vec2(Vec2 arg) {
        this(arg.x, arg.y);
    }

    public Vec2(float x, float y) {
        set(x, y);
    }

    public Vec2 copy() {
        return new Vec2(this);
    }

    public void set(Vec2 arg) {
        set(arg.x, arg.y);
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /** Sets the ith component, 0 <= i < 2 */
    public void set(int i, float val) {
        switch (i) {
            case 0: x = val; break;
            case 1: y = val; break;
            default: throw new IndexOutOfBoundsException();
        }
    }

    /** Gets the ith component, 0 <= i < 2 */
    public float get(int i) {
        switch (i) {
            case 0: return x;
            case 1: return y;
            default: throw new IndexOutOfBoundsException();
        }
    }

    public float x() { return x; }

    public float y() { return y; }

    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }

    public float dot(Vec2 arg) {
        return x * arg.x + y * arg.y;
    }

    public float length() {
        return (float) Math.sqrt(lengthSquared());
    }

    public float lengthSquared() {
        return this.dot(this);
    }

    public void normalize() {
        float len = length();
        if (len == 0.0f) return;
        scale(1.0f / len);
    }

    /** Returns this * val; creates new vector */
    public Vec2 times(float val) {
        Vec2 tmp = new Vec2(this);
        tmp.scale(val);
        return tmp;
    }

    /** this = this * val */
    public void scale(float val) {
        x *= val;
        y *= val;
    }

    /** Returns this + arg; creates new vector */
    public Vec2 plus(Vec2 arg) {
        Vec2 tmp = new Vec2();
        tmp.add(this, arg);
        return tmp;
    }

    /** this = this + b */
    public void add(Vec2 b) {
        add(this, b);
    }

    /** this = a + b */
    public void add(Vec2 a, Vec2 b) {
        x = a.x + b.x;
        y = a.y + b.y;
    }

    /** Returns this + s * arg; creates new vector */
    public Vec2 addScaled(float s, Vec2 arg) {
        Vec2 tmp = new Vec2();
        tmp.addScaled(this, s, arg);
        return tmp;
    }

    /** this = a + s * b */
    public void addScaled(Vec2 a, float s, Vec2 b) {
        x = a.x + s * b.x;
        y = a.y + s * b.y;
    }

    /** Returns this - arg; creates new vector */
    public Vec2 minus(Vec2 arg) {
        Vec2 tmp = new Vec2();
        tmp.sub(this, arg);
        return tmp;
    }

    /** this = this - b */
    public void sub(Vec2 b) {
        sub(this, b);
    }

    /** this = a - b */
    public void sub(Vec2 a, Vec2 b) {
        x = a.x - b.x;
        y = a.y - b.y;
    }

    public String toString() {
        return "(x=" + x + ", y=" + y + ")";
    }
}
