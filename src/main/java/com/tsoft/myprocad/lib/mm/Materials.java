package com.tsoft.myprocad.lib.mm;

/**
 * Source: Mechanics of Materials Second Edition
 * www.me.mtu.edu/~mavable/Book/App-a1.pdf
 *
 * TABLE C.5
 */
public class Materials {
    private Materials() { }

    private static final Material Aluminum = new Material("Aluminum", 2.77, 70, 0.25, 12.5, 280, 315, 17);
    private static final Material Bronze = new Material("Bronze", 8.86, 105, 0.34, 9.4, 140, 350, 20);
    private static final Material Concrete = new Material("Concrete", 2.41, 28, 0.15, 6.0, 0, 14, 0);
    private static final Material Copper = new Material("Copper", 8.75, 105, 0.35, 9.8, 84, 245, 35);
    private static final Material CastIron = new Material("Cast iron", 7.37, 175, 0.25, 6.0, 175, 350, 0);
    private static final Material Glass = new Material("Glass", 2.63, 52.5, 0.20, 4.5, 0, 70, 0);
    private static final Material Plastic = new Material("Plastic", 0.97, 2.8, 0.4, 50, 0, 63, 50);
    private static final Material Rock = new Material("Rock", 2.72, 56, 0.25, 4, 84, 546, 0);
    private static final Material Rubber = new Material("Rubber", 1.14, 2.1, 0.5, 90, 3.5, 14, 300);
    private static final Material Steel = new Material("Steel", 7.87, 210, 0.28, 6.6, 210, 630, 30);
    private static final Material Titanium = new Material("Titanium", 4.49, 98, 0.33, 5.3, 945, 1185, 13);
    private static final Material Wood = new Material("Wood", 0.55, 12.6, 0.30, 0, 0, 35, 0);
}
