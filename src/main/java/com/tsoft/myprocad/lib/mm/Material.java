package com.tsoft.myprocad.lib.mm;

/** Material properties in metric units */
public class Material {
    private String name;
    private double density;   // Density, mg/m3
    private double E;         // Modulus of elasticity, GPa
    private double v;         // Poisson's Ratio
    private double expansion; // Coefficient of thermal expansion, m/C
    private double es;        // Elastic Strength, MPa
    private double us;        // Ultimate Strength, MPa
    private double ductility; // Ductility (% of elongation)

    public Material(String name, double density, double E, double v, double expansion, double es, double us, double ductility) {
        this.name = name;
        this.density = density;
        this.E = E;
        this.v = v;
        this.expansion = expansion;
        this.es = es;
        this.us = us;
        this.ductility = ductility;
    }

    public String getName() {
        return name;
    }

    public double getDensity() {
        return density;
    }

    public double getE() {
        return E;
    }

    public double getV() {
        return v;
    }

    public double getExpansion() {
        return expansion;
    }

    public double getEs() {
        return es;
    }

    public double getUs() {
        return us;
    }

    public double getDuctility() {
        return ductility;
    }
}
