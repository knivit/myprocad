package com.tsoft.myprocad.model;

import com.tsoft.myprocad.AbstractItemTest;
import org.junit.Test;

public class BeamTest extends AbstractItemTest {
    @Test
    public void shape() {
        Beam beam = plan.createBeam(0, 0, -50000, 0, 0, 50000, 50, 100);

        System.out.println("Rotation");
        beam.setXEnd(0);
        beam.setYEnd(1000);
    }
}
