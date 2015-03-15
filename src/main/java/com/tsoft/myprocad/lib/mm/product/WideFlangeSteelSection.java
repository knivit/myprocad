package com.tsoft.myprocad.lib.mm.product;

/**              /|\
 *   |          Y |
 *  \|/           |
 *  ---  ---------|---------- ---
 *  tF   |        |         | /|\
 *  ---  -------| | |--------  |
 *  /|\         | | |          |
 *   |          | | |          |
 *        Z <-----+ |          | d
 *              |   |          |
 *           -->|   |<-- tW    |
 *              |   |          |
 *       -------|   |-------   |
 *       |                 |  \|/
 *       -------------------  ---
 *
 *       |<--------------->|
 *               bF
 *
 */
public class WideFlangeSteelSection {
    private double d; // Depth d, mm
    private double A; // Area, mm2
    private double tW; // Web Thickness, mm
    private double bF; // Flange, Width, mm
    private double tF; // Flange, Thickness, mm
    private double Izz; // z Axis, Area moments of inertia (second area moments), 10^6 mm4)
    private double Sz;  // z Axis, Section modulus, 10^3 mm3
    private double rz;  // z Axis, mm
    private double Iyy; // y Axis, Area moments of inertia (second area moments), 10^6 mm4)
    private double Sy;  // y Axis, Section modulus, 10^3 mm3
    private double ry;  // y Axis, mm

}
