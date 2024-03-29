package com.tsoft.myprocad.util.linealg;

/** 2x2 matrix class useful for simple linear algebra. Representation
 is (as Mat4f) in row major order and assumes multiplication by
 column vectors on the right. */

public class Mat2 {
    private float[] data;

    /** Creates new matrix initialized to the zero matrix */
    public Mat2() {
        this(0, 0, 0, 0);
    }

    public Mat2(float v00, float v01, float v10, float v11) {
        data = new float[4];
        data[0] = v00;
        data[1] = v01;
        data[2] = v10;
        data[3] = v11;
    }

    /** Initialize to the identity matrix. */
    public void makeIdent() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (i == j) {
                    set(i, j, 1.0f);
                } else {
                    set(i, j, 0.0f);
                }
            }
        }
    }

    /** Gets the (i,j)th element of this matrix, where i is the row
     index and j is the column index */
    public float get(int i, int j) {
        return data[2 * i + j];
    }

    /** Sets the (i,j)th element of this matrix, where i is the row
     index and j is the column index */
    public void set(int i, int j, float val) {
        data[2 * i + j] = val;
    }

    /** Set column i (i=[0..1]) to vector v. */
    public void setCol(int i, Vec2 v) {
        set(0, i, v.x());
        set(1, i, v.y());
    }

    /** Set row i (i=[0..1]) to vector v. */
    public void setRow(int i, Vec2 v) {
        set(i, 0, v.x());
        set(i, 1, v.y());
    }

    /** Transpose this matrix in place. */
    public void transpose() {
        float t = get(0, 1);
        set(0, 1, get(1, 0));
        set(1, 0, t);
    }

    /** Return the determinant. */
    public float determinant() {
        return (get(0, 0) * get(1, 1) - get(1, 0) * get(0, 1));
    }

    /** Full matrix inversion in place. If matrix is singular, returns
     false and matrix contents are untouched. If you know the matrix
     is orthonormal, you can call transpose() instead. */
    public boolean invert() {
        float det = determinant();
        if (det == 0.0f)
            return false;

        // Create transpose of cofactor matrix in place
        float t = get(0, 0);
        set(0, 0, get(1, 1));
        set(1, 1, t);
        set(0, 1, -get(0, 1));
        set(1, 0, -get(1, 0));

        // Now divide by determinant
        for (int i = 0; i < 4; i++) {
            data[i] /= det;
        }
        return true;
    }

    /** Multiply a 2D vector by this matrix. NOTE: src and dest must be
     different vectors. */
    public void xformVec(Vec2 src, Vec2 dest) {
        dest.set(get(0, 0) * src.x() + get(0, 1) * src.y(),
                get(1, 0) * src.x() + get(1, 1) * src.y());
    }

    /** Returns this * b; creates new matrix */
    public Mat2 mul(Mat2 b) {
        Mat2 tmp = new Mat2();
        tmp.mul(this, b);
        return tmp;
    }

    /** this = a * b */
    public void mul(Mat2 a, Mat2 b) {
        for (int rc = 0; rc < 2; rc++)
            for (int cc = 0; cc < 2; cc++) {
                float tmp = 0.0f;
                for (int i = 0; i < 2; i++)
                    tmp += a.get(rc, i) * b.get(i, cc);
                set(rc, cc, tmp);
            }
    }

    public String toString() {
        String endl = System.getProperty("line.separator");
        return "(" +
                get(0, 0) + ", " + get(0, 1) + endl +
                get(1, 0) + ", " + get(1, 1) + ")";
    }
}
