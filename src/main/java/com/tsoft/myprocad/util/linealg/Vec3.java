package com.tsoft.myprocad.util.linealg;

/** 3-element single-precision vector
 * git://github.com/sgothel/jogl-demos.git : src/gleem/linalg
*/
public class Vec3 {
    public static final Vec3 X_AXIS = new Vec3(1,  0,  0);
    public static final Vec3 Y_AXIS = new Vec3(0,  1,  0);
    public static final Vec3 Z_AXIS = new Vec3(0,  0,  1);
    public static final Vec3 NEG_X_AXIS = new Vec3(-1,  0,  0);
    public static final Vec3 NEG_Y_AXIS = new Vec3(0, -1,  0);
    public static final Vec3 NEG_Z_AXIS = new Vec3(0,  0, -1);

    private float x;
    private float y;
    private float z;

    public Vec3() {}

    public Vec3(Vec3 arg) {
        set(arg);
    }

    public Vec3(float x, float y, float z) {
        set(x, y, z);
    }

    public Vec3 copy() {
        return new Vec3(this);
    }

    public void set(Vec3 arg) {
        set(arg.x, arg.y, arg.z);
    }

    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /** Sets the ith component, 0 <= i < 3 */
    public void set(int i, float val) {
        switch (i) {
            case 0: x = val; break;
            case 1: y = val; break;
            case 2: z = val; break;
            default: throw new IndexOutOfBoundsException();
        }
    }

    /** Gets the ith component, 0 <= i < 3 */
    public float get(int i) {
        switch (i) {
            case 0: return x;
            case 1: return y;
            case 2: return z;
            default: throw new IndexOutOfBoundsException();
        }
    }

    public float x() { return x; }
    public float y() { return y; }
    public float z() { return z; }

    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public void setZ(float z) { this.z = z; }

    public float dot(Vec3 arg) {
        return x * arg.x + y * arg.y + z * arg.z;
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
    public Vec3 times(float val) {
        Vec3 tmp = new Vec3(this);
        tmp.scale(val);
        return tmp;
    }

    /** this = this * val */
    public void scale(float val) {
        x *= val;
        y *= val;
        z *= val;
    }

    /** Returns this + arg; creates new vector */
    public Vec3 plus(Vec3 arg) {
        Vec3 tmp = new Vec3();
        tmp.add(this, arg);
        return tmp;
    }

    /** this = this + b */
    public void add(Vec3 b) {
        add(this, b);
    }

    /** this = a + b */
    public void add(Vec3 a, Vec3 b) {
        x = a.x + b.x;
        y = a.y + b.y;
        z = a.z + b.z;
    }

    /** Returns this + s * arg; creates new vector */
    public Vec3 addScaled(float s, Vec3 arg) {
        Vec3 tmp = new Vec3();
        tmp.addScaled(this, s, arg);
        return tmp;
    }

    /** this = a + s * b */
    public void addScaled(Vec3 a, float s, Vec3 b) {
        x = a.x + s * b.x;
        y = a.y + s * b.y;
        z = a.z + s * b.z;
    }
    /** Returns this - arg; creates new vector */
    public Vec3 minus(Vec3 arg) {
        Vec3 tmp = new Vec3();
        tmp.sub(this, arg);
        return tmp;
    }

    /** this = this - b */
    public void sub(Vec3 b) {
        sub(this, b);
    }

    /** this = a - b */
    public void sub(Vec3 a, Vec3 b) {
        x = a.x - b.x;
        y = a.y - b.y;
        z = a.z - b.z;
    }

    /** Returns this cross arg; creates new vector */
    public Vec3 cross(Vec3 arg) {
        Vec3 tmp = new Vec3();
        tmp.cross(this, arg);
        return tmp;
    }

    /** this = a cross b. NOTE: "this" must be a different vector than
     both a and b. */
    public void cross(Vec3 a, Vec3 b) {
        x = a.y * b.z - a.z * b.y;
        y = a.z * b.x - a.x * b.z;
        z = a.x * b.y - a.y * b.x;
    }

    public Vec3 mul(Vec3 arg) {
        Vec3 tmp = new Vec3(this);
        tmp.componentMul(arg);
        return tmp;
    }

    /** Sets each component of this vector to the product of the
     component with the corresponding component of the argument
     vector. */
    public void componentMul(Vec3 arg) {
        x *= arg.x;
        y *= arg.y;
        z *= arg.z;
    }

    @Override
    public String toString() {
        return "(x=" + x + ", y=" + y + ", z=" + z + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vec3 vec3 = (Vec3) o;

        if (Float.compare(vec3.x, x) != 0) return false;
        if (Float.compare(vec3.y, y) != 0) return false;
        if (Float.compare(vec3.z, z) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        result = 31 * result + (z != +0.0f ? Float.floatToIntBits(z) : 0);
        return result;
    }
}
