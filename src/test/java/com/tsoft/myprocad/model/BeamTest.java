package com.tsoft.myprocad.model;

import com.tsoft.myprocad.AbstractItemTest;
import com.tsoft.myprocad.util.linealg.Vec3;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BeamTest extends AbstractItemTest {
    @Test
    public void shape() {
        Beam beam = plan.addBeam(0, 0, 0, 0, 0, 1000, 50, 100);

        for (int i = 0; i < beam.vertexes.length; i ++) {
            System.out.println(i + ") " + beam.vertexes[i].toString());
        }
    }
}
