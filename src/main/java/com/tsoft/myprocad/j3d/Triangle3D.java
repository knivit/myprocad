package com.tsoft.myprocad.j3d;

import com.tsoft.myprocad.util.linealg.Vec3;

import javax.vecmath.Point3f;

/** 3d face (a triangle)
 */
public class Triangle3D {
    public Point3f[] points = new Point3f[3];

    public Triangle3D(Vec3 a, Vec3 b, Vec3 c) {
        points[0] = new Point3f(a.x(), a.y(), a.z());
        points[1] = new Point3f(b.x(), b.y(), b.z());
        points[2] = new Point3f(c.x(), c.y(), c.z());
    }
}
