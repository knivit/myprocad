package com.tsoft.myprocad.util.linealg;

/** A (very incomplete) 4x4 matrix class. Representation assumes
 multiplication by column vectors on the right. */

public class Mat4 {
    private float[] data;

    /** Creates new matrix initialized to the zero matrix */
    public Mat4() {
        data = new float[16];
    }

    /** Creates new matrix initialized to argument's contents */
    public Mat4(Mat4 arg) {
        this();
        set(arg);
    }

    /** Sets this matrix to the identity matrix */
    public void makeIdent() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == j) {
                    set(i, j, 1.0f);
                } else {
                    set(i, j, 0.0f);
                }
            }
        }
    }

    /** Sets this matrix to be equivalent to the given one */
    public void set(Mat4 arg) {
        float[] mine = data;
        float[] yours = arg.data;
        for (int i = 0; i < mine.length; i++) {
            mine[i] = yours[i];
        }
    }

    /** Gets the (i,j)th element of this matrix, where i is the row
     index and j is the column index */
    public float get(int i, int j) {
        return data[4 * i + j];
    }

    /** Sets the (i,j)th element of this matrix, where i is the row
     index and j is the column index */
    public void set(int i, int j, float val) {
        data[4 * i + j] = val;
    }

    /** Sets the translation component of this matrix (i.e., the three
     top elements of the third column) without touching any of the
     other parts of the matrix */
    public void setTranslation(Vec3 trans) {
        set(0, 3, trans.x());
        set(1, 3, trans.y());
        set(2, 3, trans.z());
    }

    /** Sets the rotation component of this matrix (i.e., the upper left
     3x3) without touching any of the other parts of the matrix */
    public void setRotation(Rot rot) {
        rot.toMatrix(this);
    }

    /** Sets the upper-left 3x3 of this matrix assuming that the given
     x, y, and z vectors form an orthonormal basis */
    public void setRotation(Vec3 x, Vec3 y, Vec3 z) {
        set(0, 0, x.x());
        set(1, 0, x.y());
        set(2, 0, x.z());

        set(0, 1, y.x());
        set(1, 1, y.y());
        set(2, 1, y.z());

        set(0, 2, z.x());
        set(1, 2, z.y());
        set(2, 2, z.z());
    }

    /** Gets the upper left 3x3 of this matrix as a rotation. Currently
     does not work if there are scales. Ignores translation
     component. */
    public void getRotation(Rot rot) {
        rot.fromMatrix(this);
    }

    /** Sets the elements (0, 0), (1, 1), and (2, 2) with the
     appropriate elements of the given three-dimensional scale
     vector. Does not perform a full multiplication of the upper-left
     3x3; use this with an identity matrix in conjunction with
     <code>mul</code> for that. */
    public void setScale(Vec3 scale) {
        set(0, 0, scale.x());
        set(1, 1, scale.y());
        set(2, 2, scale.z());
    }

    /** Inverts this matrix assuming that it represents a rigid
     transform (i.e., some combination of rotations and
     translations). Assumes column vectors. Algorithm: transposes
     upper left 3x3; negates translation in rightmost column and
     transforms by inverted rotation. */
    public void invertRigid() {
        float t;
        // Transpose upper left 3x3
        t = get(0, 1);
        set(0, 1, get(1, 0));
        set(1, 0, t);
        t = get(0, 2);
        set(0, 2, get(2, 0));
        set(2, 0, t);
        t = get(1, 2);
        set(1, 2, get(2, 1));
        set(2, 1, t);
        // Transform negative translation by this
        Vec3 negTrans = new Vec3(-get(0, 3), -get(1, 3), -get(2, 3));
        Vec3 trans = new Vec3();
        xformDir(negTrans, trans);
        set(0, 3, trans.x());
        set(1, 3, trans.y());
        set(2, 3, trans.z());
    }

    /** Returns this * b; creates new matrix */
    public Mat4 mul(Mat4 b) {
        Mat4 tmp = new Mat4();
        tmp.mul(this, b);
        return tmp;
    }

    /** this = a * b */
    public void mul(Mat4 a, Mat4 b) {
        for (int rc = 0; rc < 4; rc++)
            for (int cc = 0; cc < 4; cc++) {
                float tmp = 0.0f;
                for (int i = 0; i < 4; i++)
                    tmp += a.get(rc, i) * b.get(i, cc);
                set(rc, cc, tmp);
            }
    }

    /** Transpose this matrix in place. */
    public void transpose() {
        float t;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < i; j++) {
                t = get(i, j);
                set(i, j, get(j, i));
                set(j, i, t);
            }
        }
    }

    /** Transforms a 3D vector as though it had a homogeneous coordinate
     and assuming that this matrix represents only rigid
     transformations; i.e., is not a full transformation. NOTE: src
     and dest must be different vectors. */
    public void xformPt(Vec3 src, Vec3 dest) {
        for (int rc = 0; rc < 3; rc++) {
            float tmp = 0.0f;
            for (int cc = 0; cc < 3; cc++) {
                tmp += get(rc, cc) * src.get(cc);
            }
            tmp += get(rc, 3);
            dest.set(rc, tmp);
        }
    }

    /** Transforms src using only the upper left 3x3. NOTE: src and dest
     must be different vectors. */
    public void xformDir(Vec3 src, Vec3 dest) {
        for (int rc = 0; rc < 3; rc++) {
            float tmp = 0.0f;
            for (int cc = 0; cc < 3; cc++) {
                tmp += get(rc, cc) * src.get(cc);
            }
            dest.set(rc, tmp);
        }
    }

    /** Copies data in column-major (OpenGL format) order into passed
     float array, which must have length 16 or greater. */
    public void getColumnMajorData(float[] out) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                out[4 * j + i] = get(i, j);
            }
        }
    }

    public String toString() {
        String endl = System.getProperty("line.separator");
        return "(" +
                get(0, 0) + ", " + get(0, 1) + ", " + get(0, 2) + ", " + get(0, 3) + endl +
                get(1, 0) + ", " + get(1, 1) + ", " + get(1, 2) + ", " + get(1, 3) + endl +
                get(2, 0) + ", " + get(2, 1) + ", " + get(2, 2) + ", " + get(2, 3) + endl +
                get(3, 0) + ", " + get(3, 1) + ", " + get(3, 2) + ", " + get(3, 3) + ")";
    }
}
