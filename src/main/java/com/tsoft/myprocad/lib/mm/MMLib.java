package com.tsoft.myprocad.lib.mm;

import com.tsoft.myprocad.model.*;
import com.tsoft.myprocad.model.calculation.*;
import com.tsoft.myprocad.util.StringUtil;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Mechanics of Materials Library
 *
 * Sources:
 * 1) http://iglin.exponenta.ru/All/Sopromat/smFiles/Balka2op.js
 * 2) Mechanics of Materials Second Edition
 *    www.me.mtu.edu/~mavable/Book/App-a1.pdf
 */
public class MMLib {
    private static class Profile {
        String name;

        double h;  // = высота
        double b;  // = DwuData[i * wDwuData + 1];
        double s;  // = DwuData[i * wDwuData + 2];
        double tt; // = DwuData[i * wDwuData + 3];
        double Rb; // = DwuData[num*wDwuData + 4];
        double rs; // = DwuData[num*wDwuData + 5];
        double dummy; // = DwuData[num*wDwuData + 6];
        double Jx; // = DwuData[i * wDwuData + 7];
        double Wx; // момент сопротивления = DwuData[i * wDwuData + 8];
        double Sx; //  = DwuData[i * wDwuData + 9];

        // calculable values
        double sigmax; // максимальное нормальное напряжение
        double tqmax;  // макс.кас.напряжение
        double tmmax;  // макс.кас.напряжение в точке с Mmax

        // drawable points
        double[] xx;
        double[] yy;

        public Profile(String name, double h, double b, double s, double tt, double Rb, double rs, double dummy, double Jx, double Wx, double Sx,
                       double notUsed1, double notUsed2) {
            this.name = name;
            this.h = h;
            this.b = b;
            this.s = s;
            this.tt = tt;
            this.Rb = Rb;
            this.rs = rs;
            this.dummy = dummy;
            this.Jx = Jx;
            this.Wx = Wx;
            this.Sx = Sx;

            calcPoints();
        }

        public void calc(double ymmax, double yqmax, double Qmmax) {
            sigmax = ymmax / Wx * 1000;
            tqmax = yqmax * Sx / (Jx * s) * 100;
            tmmax = Qmmax * Sx / (Jx * s) * 100;
        }

        // возвращает точки для рисования двутавра с выбранным номером
        private void calcPoints() {
            double[] ctt = new double[] {
                    1,
                    0.98480775301221,
                    0.93969262078591,
                    0.86602540378444,
                    0.76604444311898,
                    0.64278760968654,
                    0.5,
                    0.34202014332567,
                    0.17364817766693,
                    0
            };

            xx = new double[92];
            yy = new double[92];

            xx[0] = 0;
            yy[0] = 0;
            xx[1] = b;
            yy[1] = 0;
            xx[2] = b;
            yy[2] = tt-rs;
            for (int i=3; i<13; i++) {
                xx[i] = b-rs+rs*ctt[i-3];
                yy[i] = tt-rs+rs*ctt[12-i];
            }
            xx[13] = s/2+b/2+Rb;
            yy[13] = tt;
            for (int i=14; i<24; i++) {
                xx[i] = s/2+b/2+Rb-Rb*ctt[23-i];
                yy[i] = tt+Rb-Rb*ctt[i-14];
            }
            xx[24] = s/2+b/2;
            yy[24] = h-tt-Rb;
            for (int i=25; i<35; i++) {
                xx[i] = s/2+b/2+Rb-Rb*ctt[i-25];
                yy[i] = h-tt-Rb+Rb*ctt[34-i];
            }
            xx[35] = b-rs;
            yy[35] = h-tt;
            for (int i=36; i<46; i++) {
                xx[i] = b-rs+rs*ctt[45-i];
                yy[i] = h-tt+rs-rs*ctt[i-36];
            }
            xx[46] = b;
            yy[46] = h;
            xx[47] = 0;
            yy[47] = h;
            xx[48] = 0;
            yy[48] = h-tt+rs;
            for (int i=49; i<59; i++) {
                xx[i] = rs-rs*ctt[i-49];
                yy[i] = h-tt+rs-rs*ctt[58-i];
            }
            xx[59] = b/2-s/2-Rb;
            yy[59] = h-tt;
            for (int i=60; i<70; i++) {
                xx[i] = b/2-s/2-Rb+Rb*ctt[69-i];
                yy[i] = h-tt-Rb+Rb*ctt[i-60];
            }
            xx[70] = b/2-s/2;
            yy[70] = tt+Rb;
            for (int i=71; i<81; i++) {
                xx[i] = b/2-s/2-Rb+Rb*ctt[i-71];
                yy[i] = tt+Rb-Rb*ctt[80-i];
            }
            xx[81] = rs;
            yy[81] = tt;
            for (int i=82; i<92; i++) {
                xx[i] = rs-rs*ctt[91-i];
                yy[i] = tt-rs+rs*ctt[i-82];
            }

            for (int i=0; i<92; i++) {
                xx[i] = xx[i]*0.1;
                yy[i] = yy[i]*0.1;
            }
        }
    }

    private static ArrayList<Profile> profiles;

    static {
        // Таблица двутавров
        profiles = new ArrayList<>();
        profiles.add(new Profile("10", 100, 55, 4.5, 7.2, 7, 2.5, 12, 198, 39.7, 23, 17.9, 6.49));
        profiles.add(new Profile("12", 120, 64, 4.8, 7.3, 7.5, 3, 14.7, 350, 58.4, 33.7, 27.9, 8.72));
        profiles.add(new Profile("14", 140, 73, 4.9, 7.5, 8, 3, 17.4, 572, 81.7, 46.8, 41.9, 11.5));
        profiles.add(new Profile("16", 160, 81, 5, 7.8, 8.5, 3.5, 20.2, 873, 109, 62.3, 58.6, 14.5));
        profiles.add(new Profile("18", 180, 90, 5.1, 8.1, 9, 3.5, 23.4, 1290, 143, 81.4, 82.6, 18.4));
        profiles.add(new Profile("18a", 180, 100, 5.1, 8.3, 9, 3.5, 25.4, 1430, 159, 89.8, 114, 22.8));
        profiles.add(new Profile("20", 200, 100, 5.2, 8.4, 9.5, 4, 26.8, 1840, 184, 104, 115, 23.1));
        profiles.add(new Profile("20a", 200, 110, 5.2, 8.6, 9.5, 4, 28.9, 2030, 203, 114, 155, 28.2));
        profiles.add(new Profile("22", 220, 110, 5.4, 8.7, 10, 4, 30.6, 2550, 232, 131, 157, 28.6));
        profiles.add(new Profile("22a", 220, 120, 5.4, 8.9, 10, 4, 32.8, 2790, 254, 143, 206, 34.3));
        profiles.add(new Profile("24", 240, 115, 5.6, 9.5, 10.5, 4, 34.8, 3460, 289, 163, 198, 34.5));
        profiles.add(new Profile("24a", 240, 125, 5.6, 9.8, 10.5, 4, 37.5, 3800, 317, 178, 260, 41.6));
        profiles.add(new Profile("27", 270, 125, 6, 9.8, 11, 4.5, 40.2, 5010, 371, 210, 260, 41.5));
        profiles.add(new Profile("27a", 270, 135, 6, 10.2, 11, 4.5, 43.2, 5500, 407, 229, 337, 50));
        profiles.add(new Profile("30", 300, 135, 6.5, 10.2, 12, 5, 46.5, 7080, 472, 268, 337, 49.9));
        profiles.add(new Profile("30a", 300, 145, 6.5, 10.7, 12, 5, 49.9, 7780, 518, 292, 436, 60.1));
        profiles.add(new Profile("33", 330, 140, 7, 11.2, 13, 5, 53.8, 9840, 597, 339, 419, 59.9));
        profiles.add(new Profile("36", 360, 145, 7.5, 12.3, 14, 6, 61.9, 13380, 743, 423, 516, 71.1));
        profiles.add(new Profile("40", 400, 155, 8.3, 13, 15, 6, 72.6, 19062, 953, 545, 667, 86.1));
        profiles.add(new Profile("45", 450, 160, 9, 14.2, 16, 7, 84.7, 27696, 1231, 708, 808, 101));
        profiles.add(new Profile("50", 500, 170, 10, 15.2, 17, 7, 100, 39727, 1589, 919, 1043, 123));
        profiles.add(new Profile("55", 550, 180, 11, 16.5, 18, 7, 118, 55962, 2035, 1181, 1356, 151));
        profiles.add(new Profile("60", 600, 190, 12, 17.8, 20, 8, 138, 76806, 2560, 1491, 1725, 182));
    }

    /**
     * Calculate beam
     * @return Result
     */
    public static String calcStatic(Beam beam) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < beam.solutions.length; i ++) beam.solutions[i] = new BeamSolution();

        String sout = "<center><h2>Расчет</h2></center>" +
                "<b>Таблица нагружения</b><br>";

        // Таблица нагружения
        int n = 0;
        for (Moment moment : beam.getMoments()) {
            sout = sout + (++n) + ") " + moment.getName() + " ";
            sout = sout + str(moment.vm) + " кНм<, " + str(moment.zm) + " м<br>";
        }

        for (Force force : beam.getForces()) {
            sout = sout + (++n) + ") " + force.getName() + " ";
            sout = sout + str(force.vs) + " кН, " + str(force.zs) + " м<br>";
        }

        for (DistributedForce distributedForce : beam.getDistributedForces()) {
            sout = sout + (++n) + ") " + distributedForce.getName() + " ";
            sout = sout + String.format("[%s, %s] кН/м, [%s, %s] м<br>",
                    str(distributedForce.q1),
                    str(distributedForce.q2),
                    str(distributedForce.z1),
                    str(distributedForce.z2));
        }
        result.append(sout);

        double vsc = 200/beam.getVsc(); // масштаб по вертикали
        drawActions(beam, beam.solutions[0].getG2d(), vsc);

        /* Нахождение реакций опор */
        sout = "<br><b>Нахождение реакций опор</b><br>" +
                "Балка является статически определимой, т.к. неизвестные реакции опор могут быть найдены из уравнений статики. " +
                "Всего таких уравнений в плоском случае 3, но одно из них (сумма проекций всех сил на ось Oz равна нулю) обращается в тождество. " +
                "Остаётся 2 уравнения: сумма проекций всех сил на ось Oy равна нулю и сумма моментов всех сил относительно какой-либо точки " +
                "равна нулю:<br>" +
                "∑Fyk = 0; k=1..n\t(5)<br>" +
                "∑M0k = 0.k=1..n\t(6)<br>" +
                "Из этих уравнений можно найти неизвестные реакции в опорах Ra и Rb (рис. 2)<br>";
        result.append(sout);

        String textRb = "<i>R<sub>b</sub></i>(<i>a</i>&minus;<i>b</i>)";
        String textRa = "<i>R<sub>a</sub></i>(<i>b</i>&minus;<i>a</i>)";
        String textRsum = "<p>Проверим полученные результаты по уравнению ∑ Fy = 0<br>" +
                "Составим сумму проекций всех сил на ось <i>Oy</i>, она должна оказаться равной нулю.</p>" +
                "<i>R<sub>a</sub></i>+<i>R<sub>b</sub></i>";
        for (int i = 0; i < beam.getMoments().size(); i++) {
            textRb = textRb + "+<i>M</i><sub>" + (i + 1) + "</sub>";
            textRa = textRa + "+<i>M</i><sub>" + (i + 1) + "</sub>";
        }

        for (int i = 0; i < beam.getForces().size(); i++) {
            textRb = textRb + "+<i>F</i><sub>" + (i + 1) + "</sub>(<i>a</i>&minus;<i>a</i><sub>" + (i + 1) + "</sub>)";
            textRa = textRa + "+<i>F</i><sub>" + (i + 1) + "</sub>(<i>b</i>&minus;<i>a</i><sub>" + (i + 1) + "</sub>)";
            textRsum = textRsum + "+<i>F</i><sub>" + (i + 1) + "</sub>";
        }

        for (int i = 0; i < beam.getDistributedForces().size(); i++) {
            textRb = textRb + "&minus;(<i>b</i><sub>" + (i + 1) +
                    "</sub>&minus;<i>a</i><sub>" + (i + 1) +
                    "</sub>)/6&middot;(<i>q</i><sub><i>a</i>" + (i + 1) +
                    "</sub>(2<i>a</i><sub>" + (i + 1) +
                    "</sub>+<i>b</i><sub>" + (i + 1) +
                    "</sub>)+<i>q</i><sub><i>b</i>" + (i + 1) +
                    "</sub>(<i>a</i><sub>" + (i + 1) +
                    "</sub>+2<i>b</i><sub>" + (i + 1) +
                    "</sub>)&minus;3<i>a</i>(<i>q</i><sub><i>a</i>" + (i + 1) +
                    "</sub>+<i>q</i><sub><i>b</i>" + (i + 1) +
                    "</sub>))";
            textRa = textRa + "&minus;(<i>b</i><sub>" + (i + 1) +
                    "</sub>&minus;<i>a</i><sub>" + (i + 1) +
                    "</sub>)/6&middot;(<i>q</i><sub><i>a</i>" + (i + 1) +
                    "</sub>(2<i>a</i><sub>" + (i + 1) +
                    "</sub>+<i>b</i><sub>" + (i + 1) +
                    "</sub>)+<i>q</i><sub><i>b</i>" + (i + 1) +
                    "</sub>(<i>a</i><sub>" + (i + 1) +
                    "</sub>+2<i>b</i><sub>" + (i + 1) +
                    "</sub>)&minus;3<i>b</i>(<i>q</i><sub><i>a</i>" + (i + 1) +
                    "</sub>+<i>q</i><sub><i>b</i>" + (i + 1) +
                    "</sub>))";
            textRsum = textRsum + "+(<i>q</i><sub><i>a</i>" + (i + 1) +
                    "</sub>+<i>q</i><sub><i>b</i>" + (i + 1) +
                    "</sub>)/2&middot;(<i>b</i><sub>" + (i + 1) +
                    "</sub>&minus;<i>a</i><sub>" + (i + 1) +
                    "</sub>)";
        }

        textRb = textRb + "=0;<br><i>R<sub>b</sub></i>&middot;(" + str(beam.getLeftSupport()) + "&minus;" + str(beam.getRightSupport()) + ")";
        textRa = textRa + "=0;<br><i>R<sub>a</sub></i>&middot;(" + str(beam.getRightSupport()) + "&minus;" + str(beam.getLeftSupport()) + ")";
        textRsum = textRsum + "=";

        double Rb = 0;
        double Ra = 0;
        for (Moment moment : beam.getMoments()) {
            textRb = textRb + signStr(moment.vm);
            textRa = textRa + signStr(moment.vm);
            Rb = Rb + moment.vm;
            Ra = Ra + moment.vm;
        }

        for (Force force : beam.getForces()) {
            textRb = textRb + signStr(force.vs) + "&middot;(" + str(beam.getLeftSupport()) + "&minus;" + str(force.zs) + ")";
            textRa = textRa + signStr(force.vs) + "&middot;(" + str(beam.getRightSupport()) + "&minus;" + str(force.zs) + ")";
            Rb = Rb + force.vs * (beam.getLeftSupport() - force.zs);
            Ra = Ra + force.vs * (beam.getRightSupport() - force.zs);
        }

        for (DistributedForce distributedForce : beam.getDistributedForces()) {
            textRb = textRb + "&minus;(" + str(distributedForce.z2) + "&minus;" + str(distributedForce.z1) + ")/6&middot;(" + str(distributedForce.q1) + "&middot;(2&middot;" + str(distributedForce.z1) + signStr(distributedForce.z2) + ")" + signStr(distributedForce.q2) + "&middot;(" + str(distributedForce.z1) + "+2&middot;" + str(distributedForce.z2) + ")&minus;3&middot;" + str(beam.getLeftSupport()) + "&middot;(" + str(distributedForce.q1) + signStr(distributedForce.q2) + "))";
            textRa = textRa + "&minus;(" + str(distributedForce.z2) + "&minus;" + str(distributedForce.z1) + ")/6&middot;(" + str(distributedForce.q1) + "&middot;(2&middot;" + str(distributedForce.z1) + signStr(distributedForce.z2) + ")" + signStr(distributedForce.q2) + "&middot;(" + str(distributedForce.z1) + "+2&middot;" + str(distributedForce.z2) + ")&minus;3&middot;" + str(beam.getRightSupport()) + "&middot;(" + str(distributedForce.q1) + signStr(distributedForce.q2) + "))";
            Rb = Rb - (distributedForce.z2 - distributedForce.z1) / 6 * (distributedForce.q1 * (2 * distributedForce.z1 + distributedForce.z2) + distributedForce.q2 * (distributedForce.z1 + 2 * distributedForce.z2) - 3 * beam.getLeftSupport() * (distributedForce.q1 + distributedForce.q2));
            Ra = Ra - (distributedForce.z2 - distributedForce.z1) / 6 * (distributedForce.q1 * (2 * distributedForce.z1 + distributedForce.z2) + distributedForce.q2 * (distributedForce.z1 + 2 * distributedForce.z2) - 3 * beam.getRightSupport() * (distributedForce.q1 + distributedForce.q2));
        }
        textRb = textRb + "=0;<br>" + str(beam.getLeftSupport() - beam.getRightSupport()) + "<i>R<sub>b</sub></i>" + signStr(Rb) + "=0;<br>";
        textRa = textRa + "=0;<br>" + str(beam.getRightSupport() - beam.getLeftSupport()) + "<i>R<sub>a</sub></i>" + signStr(Ra) + "=0;<br>";

        Rb = -Rb / (beam.getLeftSupport() - beam.getRightSupport());
        Ra = -Ra / (beam.getRightSupport() - beam.getLeftSupport());
        textRsum = textRsum + str(Ra) + signStr(Rb);
        for (Force force : beam.getForces()) {
            textRsum = textRsum + signStr(force.vs);
        }

        for (DistributedForce distributedForce : beam.getDistributedForces()) {
            textRsum = textRsum + "+(" + str(distributedForce.q1) + signStr(distributedForce.q2) + ")/2&middot;(" + str(distributedForce.z2) + "&minus;" + str(distributedForce.z1) + ")";
        }

        textRb = textRb + "<i>R<sub>b</sub></i>=" + str(Rb) + " кН.<br>";
        textRa = textRa + "<i>R<sub>a</sub></i>=" + str(Ra) + " кН.<br>";
        result.append(textRb + "<br>" + textRa + "<br>" + textRsum + "=0.<br><br>");

        /* Построение эпюр поперечных сил и изгибающих моментов */
        String stext = "<b>Построение эпюр поперечных сил и изгибающих моментов</b>";
        String souteq1 = "<p><i>EJ<sub>x</sub>w</i>(0)+<i>EJ<sub>x</sub></i>&theta;(0)<i>a</i>";
        String souteq2 = "<i>EJ<sub>x</sub>w</i>(0)+<i>EJ<sub>x</sub></i>&theta;(0)<i>b</i>+<i>R<sub>a</sub></i>(<i>b</i>&minus;<i>a</i>)<sup>3</sup>/6";
        String souteq11 = "<p><i>EJ<sub>x</sub>w</i>(0)+<i>EJ<sub>x</sub></i>&theta;(0)&middot;" + str(beam.getLeftSupport());
        String souteq22 = "<i>EJ<sub>x</sub>w</i>(0)+<i>EJ<sub>x</sub></i>&theta;(0)&middot;" + str(beam.getRightSupport()) + signStr(Ra) + "&middot;(" + str(beam.getRightSupport()) + signStr(-beam.getLeftSupport()) + ")<sup>3</sup>/6";

        double b1 = 0;
        double b2 = Ra * Math.pow(beam.getRightSupport() - beam.getLeftSupport(), 3) / 6;

        // просматриваем моменты
        int k = 0;
        for (Moment moment : beam.getMoments()) {
            k++;
            if (moment.zm < beam.getLeftSupport()) {
                souteq1 = souteq1 + "+<i>M</i><sub>" + k + "</sub>(<i>a</i>&minus;<i>a</i><sub>" + k + "</sub>)<sup>2</sup>/2";
                souteq11 = souteq11 + signStr(moment.vm) + "&middot;(" + str(beam.getLeftSupport()) + signStr(-moment.zm) + ")<sup>2</sup>/2";
                b1 = b1 + moment.vm * Math.pow(beam.getLeftSupport() - moment.zm, 2) / 2;
            }
            if (moment.zm < beam.getRightSupport()) {
                souteq2 = souteq2 + "+<i>M</i><sub>" + k + "</sub>(<i>b</i>&minus;<i>a</i><sub>" + k + "</sub>)<sup>2</sup>/2";
                souteq22 = souteq22 + signStr(moment.vm) + "&middot;(" + str(beam.getRightSupport()) + signStr(-moment.zm) + ")<sup>2</sup>/2";
                b2 = b2 + moment.vm * Math.pow(beam.getRightSupport() - moment.zm, 2) / 2;
            }
        }

        // просматриваем силы
        k = 0;
        for (Force force : beam.getForces()) {
            k++;
            if (force.zs < beam.getLeftSupport()) {
                souteq1 = souteq1 + "+<i>F</i><sub>" + k + "</sub>(<i>a</i>&minus;<i>a</i><sub>" + k + "</sub>)<sup>3</sup>/6";
                souteq11 = souteq11 + signStr(force.vs) + "&middot;(" + str(beam.getLeftSupport()) + signStr(-force.zs) + ")<sup>3</sup>/6";
                b1 = b1 + force.vs * Math.pow(beam.getLeftSupport() - force.zs, 3) / 6;
            }
            if (force.zs < beam.getRightSupport()) {
                souteq2 = souteq2 + "+<i>F</i><sub>" + k + "</sub>(<i>b</i>&minus;<i>a</i><sub>" + k + "</sub>)<sup>3</sup>/6";
                souteq22 = souteq22 + signStr(force.vs) + "&middot;(" + str(beam.getRightSupport()) + signStr(-force.zs) + ")<sup>3</sup>/6";
                b2 = b2 + force.vs * Math.pow(beam.getRightSupport() - force.zs, 3) / 6;
            }
        }

        // просматриваем нагрузки
        k = 0;
        for (DistributedForce distributedForce : beam.getDistributedForces()) {
            k++;
            double ck = (distributedForce.q2 - distributedForce.q1) / (distributedForce.z2 - distributedForce.z1);
            if (distributedForce.z1 < beam.getLeftSupport()) {
                souteq1 = souteq1 + "+<i>q</i><sub><i>a</i>" + k + "</sub>(<i>a</i>&minus;<i>a</i><sub>" + k + "</sub>)<sup>4</sup>/24";
                souteq11 = souteq11 + signStr(distributedForce.q1) + "&middot;(" + str(beam.getLeftSupport()) + signStr(-distributedForce.z1) + ")<sup>4</sup>/24";
                if (Math.abs(ck) > 1e-8) {
                    souteq1 = souteq1 + "+<i>c</i><sub>" + k + "</sub>(<i>a</i>&minus;<i>a</i><sub>" + k + "</sub>)<sup>5</sup>/120";
                    souteq11 = souteq11 + signStr(ck) + "&middot;(" + str(beam.getLeftSupport()) + signStr(-distributedForce.z1) + ")<sup>5</sup>/120";
                }
                b1 = b1 + distributedForce.q1 * Math.pow(beam.getLeftSupport() - distributedForce.z1, 4) / 24 + ck * Math.pow(beam.getLeftSupport() - distributedForce.z1, 5) / 120;
            }

            if (distributedForce.z2 < beam.getLeftSupport()) {
                souteq1 = souteq1 + "&minus;<i>q</i><sub><i>b</i>" + k + "</sub>(<i>a</i>&minus;<i>b</i><sub>" + k + "</sub>)<sup>4</sup>/24";
                souteq11 = souteq11 + signStr(-distributedForce.q2) + "&middot;(" + str(beam.getLeftSupport()) + signStr(-distributedForce.z2) + ")<sup>4</sup>/24";
                if (Math.abs(ck) > 1e-8) {
                    souteq1 = souteq1 + "-<i>c</i><sub>" + k + "</sub>(<i>a</i>&minus;<i>b</i><sub>" + k + "</sub>)<sup>5</sup>/120";
                    souteq11 = souteq11 + signStr(-ck) + "&middot;(" + str(beam.getLeftSupport()) + signStr(-distributedForce.z2) + ")<sup>5</sup>/120";
                }
                b1 = b1 - distributedForce.q2 * Math.pow(beam.getLeftSupport() - distributedForce.z2, 4) / 24 - ck * Math.pow(beam.getLeftSupport() - distributedForce.z2, 5) / 120;
            }

            if (distributedForce.z1 < beam.getRightSupport()) {
                souteq2 = souteq2 + "+<i>q</i><sub><i>a</i>" + k + "</sub>(<i>b</i>&minus;<i>a</i><sub>" + k + "</sub>)<sup>4</sup>/24";
                souteq22 = souteq22 + signStr(distributedForce.q1) + "&middot;(" + str(beam.getRightSupport()) + signStr(-distributedForce.z1) + ")<sup>4</sup>/24";
                if (Math.abs(ck) > 1e-8) {
                    souteq2 = souteq2 + "+<i>c</i><sub>" + k + "</sub>(<i>b</i>&minus;<i>a</i><sub>" + k + "</sub>)<sup>5</sup>/120";
                    souteq22 = souteq22 + signStr(ck) + "&middot;(" + str(beam.getRightSupport()) + signStr(-distributedForce.z1) + ")<sup>5</sup>/120";
                }

                b2 = b2 + distributedForce.q1 * Math.pow(beam.getRightSupport() - distributedForce.z1, 4) / 24 + ck * Math.pow(beam.getRightSupport() - distributedForce.z1, 5) / 120;
            }

            if (distributedForce.z2 < beam.getRightSupport()) {
                souteq2 = souteq2 + "&minus;<i>q</i><sub><i>b</i>" + k + "</sub>(<i>b</i>&minus;<i>b</i><sub>" + k + "</sub>)<sup>4</sup>/24";
                souteq22 = souteq22 + signStr(-distributedForce.q2) + "&middot;(" + str(beam.getRightSupport()) + signStr(-distributedForce.z2) + ")<sup>4</sup>/24";
                if (Math.abs(ck) > 1e-8) {
                    souteq2 = souteq2 + "-<i>c</i><sub>" + k + "</sub>(<i>b</i>&minus;<i>b</i><sub>" + k + "</sub>)<sup>5</sup>/120";
                    souteq22 = souteq22 + signStr(-ck) + "&middot;(" + str(beam.getRightSupport()) + signStr(-distributedForce.z2) + ")<sup>5</sup>/120";
                }
                b2 = b2 - distributedForce.q2 * Math.pow(beam.getRightSupport() - distributedForce.z2, 4) / 24 - ck * Math.pow(beam.getRightSupport() - distributedForce.z2, 5) / 120;
            }
        }

        double EJt0 = (b1 - b2) / (beam.getRightSupport() - beam.getLeftSupport());
        double EJw0 = -b1 - EJt0 * beam.getLeftSupport();
        souteq1 = souteq1 + "=0;<br>";
        souteq2 = souteq2 + "=0.</p><p>Подставляем:</p>";
        souteq11 = souteq11 + "=0;<br>";
        souteq22 = souteq22 + "=0.</p><p>Считаем:</p>";
        String souteq111 = "<p><i>EJ<sub>x</sub>w</i>(0)+<i>EJ<sub>x</sub></i>&theta;(0)&middot;" + str(beam.getLeftSupport()) + signStr(b1) + "=0;<br>";
        String souteq222 = "<i>EJ<sub>x</sub>w</i>(0)+<i>EJ<sub>x</sub></i>&theta;(0)&middot;" + str(beam.getRightSupport()) + signStr(b2) + "=0.</p><p>Решаем систему:</p>";
        souteq222 = souteq222 + "<p><i>EJ<sub>x</sub>w</i>(0) = " + str(EJw0) + " кНм<sup>3</sup>;<br><i>EJ<sub>x</sub></i>&theta;(0) = " + str(EJt0) + " кНм<sup>2</sup>.</p>";
        result.append(stext + souteq1 + souteq2 + souteq11 + souteq22 + souteq111 + souteq222);

        // Число и точки интервалов переключения
        ArrayList<Double> Li = new ArrayList<>();
        Li.add(0.0); // начало балочки
        Li.add(beam.getLength()); // длина балочки
        if ((Math.abs(beam.getLeftSupport() - Li.get(0)) > 1e-3) && (Math.abs(beam.getLeftSupport() - Li.get(1)) > 1e-3))
            Li.add(beam.getLeftSupport());
        if ((Math.abs(beam.getRightSupport() - Li.get(0)) > 1e-3) && (Math.abs(beam.getRightSupport() - Li.get(1)) > 1e-3))
            Li.add(beam.getRightSupport());

        // проверяем уникальность
        for (Moment moment : beam.getMoments()) {
            boolean found = false;
            for (int i = 0; i < Li.size(); i ++) {
                if (Math.abs(moment.zm - Li.get(i)) < 1e-8) {
                    found = true;
                    break;
                }
            }
            if (!found) Li.add(moment.zm);
        }

        for (Force force : beam.getForces()) {
            boolean found = false;
            for (int i = 0; i < Li.size(); i ++) {
                if (Math.abs(force.zs - Li.get(i)) < 1e-8) {
                    found = true;
                    break;
                }
            }
            if (!found) Li.add(force.zs);
        }

        for (DistributedForce distributedForce : beam.getDistributedForces()) {
            boolean found = false;
            for (int i = 0; i < Li.size(); i ++) {
                if (Math.abs(distributedForce.z1 - Li.get(i)) < 1e-8) {
                    found = true;
                    break;
                }
            }
            if (!found) Li.add(distributedForce.z1);

            found = false;
            for (int i = 0; i < Li.size(); i ++) {
                if (Math.abs(distributedForce.z2 - Li.get(i)) < 1e-8) {
                    found = true;
                    break;
                }
            }
            if (!found) Li.add(distributedForce.z2);
        }

        Li.sort(Comparator.<Double>naturalOrder());

        int LLi = Li.size(); // текущая длина
        sout = "<p>Число интервалов переключения <i>n<sub>k</sub></i> = " + (LLi - 1) + "<br>Точки переключения:";
        for (int i = 0; i < LLi; i++) {
            if (i > 0) sout = sout + ", ";
            sout = sout + " " + str(Li.get(i)) + " м.";
        }
        result.append(sout);

        /* Эпюры изгибающих сил и перерезывающих момментов */
        String souta = "";
        String sout1 = "";
        String soutm = "";
        String soutt = "";
        String soutw = "";
        String soutm1, soutt1, soutw1;

        double[] xx = new double[1024];
        double[] yq = new double[1024];
        double[] ym = new double[1024];
        double[] yt = new double[1024];
        double[] yw = new double[1024];
        xx[0] = 50;
        yq[0] = 0;
        ym[0] = 0;
        yt[0] = 0;
        yw[0] = 0;

        // строим на участках
        int kpr = 1; // число точек
        int k1, k2;
        for (int j = 1; j < LLi; j++) {
            String t1 = str(Li.get(j - 1));
            String t2 = str(Li.get(j));
            souta = souta + "<p><i>z</i>&isin;[" + t1 + ", " + t2 + "): <i>Q</i><sub>" + j + "</sub>(<i>z</i>) = ";
            soutm = soutm + "<p><i>z</i>&isin;[" + t1 + ", " + t2 + "): <i>M</i><sub>" + j + "</sub>(<i>z</i>) = ";
            soutt = soutt + "<p><i>z</i>&isin;[" + t1 + ", " + t2 + "): <i>EJ<sub>x</sub></i>&theta;<sub>" + j + "</sub>(<i>z</i>) = <i>EJ<sub>x</sub></i>&theta;(0)";
            soutw = soutw + "<p'><i>z</i>&isin;[" + t1 + ", " + t2 + "): <i>EJ<sub>x</sub>w</i><sub>" + j + "</sub>(<i>z</i>) = <i>EJ<sub>x</sub>w</i>(0)+<i>EJ<sub>x</sub></i>&theta;(0)<i>z</i>";

            k1 = (int)Math.round(Li.get(j - 1) / beam.getLength() * 700);
            k2 = (int)Math.round(Li.get(j) / beam.getLength() * 700); // количество точек
            for (int i1 = 0; i1 < k2 - k1 + 1; i1++) {
                xx[kpr + i1] = k1 + i1 + 50;
                yq[kpr + i1] = 0;
                ym[kpr + i1] = 0;
                yt[kpr + i1] = EJt0;
                yw[kpr + i1] = EJw0 + EJt0 * (Li.get(j - 1) + (Li.get(j) - Li.get(j - 1)) / (k2 - k1) * i1);
            }

            sout1 = "";
            soutm1 = "";
            soutt1 = str(EJt0);
            soutw1 = str(EJw0) + signStr(EJt0) + "<i>z</i>";

            // учитываем Ra
            if (beam.getLeftSupport() < Li.get(j)) {
                for (int i1 = 0; i1 < k2 - k1 + 1; i1++) {
                    yq[kpr + i1] = yq[kpr + i1] + Ra;
                    ym[kpr + i1] = ym[kpr + i1] + Ra * ((Li.get(j - 1) + (Li.get(j) - Li.get(j - 1)) / (k2 - k1) * i1) - beam.getLeftSupport());
                    yt[kpr + i1] = yt[kpr + i1] + Ra * Math.pow((Li.get(j - 1) + (Li.get(j) - Li.get(j - 1)) / (k2 - k1) * i1) - beam.getLeftSupport(), 2) / 2;
                    yw[kpr + i1] = yw[kpr + i1] + Ra * Math.pow((Li.get(j - 1) + (Li.get(j) - Li.get(j - 1)) / (k2 - k1) * i1) - beam.getLeftSupport(), 3) / 6;
                }

                souta = souta + "<i>R<sub>a</sub></i>";
                soutm = soutm + "<i>R<sub>a</sub></i>(<i>z</i>&minus;<i>a</i>)";
                soutt = soutt + "+<i>R<sub>a</sub></i>(<i>z</i>&minus;<i>a</i>)<sup>2</sup>/2";
                soutw = soutw + "+<i>R<sub>a</sub></i>(<i>z</i>&minus;<i>a</i>)<sup>3</sup>/6";
                sout1 = sout1 + str(Ra);
                soutm1 = soutm1 + str(Ra) + "&middot;(<i>z</i>&minus;" + str(beam.getLeftSupport()) + ")";
                soutt1 = soutt1 + str(Ra) + "&middot;(<i>z</i>&minus;" + str(beam.getLeftSupport()) + ")<sup>2</sup>/2";
                soutw1 = soutw1 + str(Ra) + "&middot;(<i>z</i>&minus;" + str(beam.getLeftSupport()) + ")<sup>3</sup>/6";
            }

            // учитываем Rb
            if (beam.getRightSupport() < Li.get(j)) {
                for (int i1 = 0; i1 < k2 - k1 + 1; i1++) {
                    yq[kpr + i1] = yq[kpr + i1] + Rb;
                    ym[kpr + i1] = ym[kpr + i1] + Rb * ((Li.get(j - 1) + (Li.get(j) - Li.get(j - 1)) / (k2 - k1) * i1) - beam.getRightSupport());
                    yt[kpr + i1] = yt[kpr + i1] + Rb * Math.pow((Li.get(j - 1) + (Li.get(j) - Li.get(j - 1)) / (k2 - k1) * i1) - beam.getRightSupport(), 2) / 2;
                    yw[kpr + i1] = yw[kpr + i1] + Rb * Math.pow((Li.get(j - 1) + (Li.get(j) - Li.get(j - 1)) / (k2 - k1) * i1) - beam.getRightSupport(), 3) / 6;
                }

                souta = souta + "+<i>R<sub>b</sub></i>";
                soutm = soutm + "+<i>R<sub>b</sub></i>(<i>z</i>&minus;<i>b</i>)";
                soutt = soutt + "+<i>R<sub>b</sub></i>(<i>z</i>&minus;<i>b</i>)<sup>2</sup>/2";
                soutw = soutw + "+<i>R<sub>b</sub></i>(<i>z</i>&minus;<i>b</i>)<sup>3</sup>/6";
                sout1 = sout1 + str(Rb);
                soutm1 = soutm1 + str(Rb) + "&middot;(<i>z</i>&minus;" + str(beam.getRightSupport()) + ")";
                soutt1 = soutt1 + str(Rb) + "&middot;(<i>z</i>&minus;" + str(beam.getRightSupport()) + ")<sup>2</sup>/2";
                soutw1 = soutw1 + str(Rb) + "&middot;(<i>z</i>&minus;" + str(beam.getRightSupport()) + ")<sup>3</sup>/6";
            }

            // просматриваем моменты
            k = 0;
            for (Moment moment : beam.getMoments()) {
                if (moment.zm < Li.get(j)) {
                    k++;
                    soutm = soutm + "+<i>M</i><sub>" + k + "</sub>";
                    soutm1 = soutm1 + signStr(moment.vm);
                    soutt = soutt + "+<i>M</i><sub>" + k + "</sub>(<i>z</i>&minus;" + str(moment.zm) + ")";
                    soutt1 = soutt1 + signStr(moment.vm) + "&middot;(<i>z</i>&minus;" + str(moment.zm) + ")";
                    soutw = soutw + "+<i>M</i><sub>" + k + "</sub>(<i>z</i>&minus;" + str(moment.zm) + ")<sup>2</sup>/2";
                    soutw1 = soutw1 + signStr(moment.vm) + "&middot;(<i>z</i>&minus;" + str(moment.zm) + ")<sup>2</sup>/2";
                    for (int i1 = 0; i1 < k2 - k1 + 1; i1++) {
                        ym[kpr + i1] = ym[kpr + i1] + moment.vm;
                        yt[kpr + i1] = yt[kpr + i1] + moment.vm * (Li.get(j - 1) + (Li.get(j) - Li.get(j - 1)) / (k2 - k1) * i1 - moment.zm);
                        yw[kpr + i1] = yw[kpr + i1] + moment.vm * Math.pow(Li.get(j - 1) + (Li.get(j) - Li.get(j - 1)) / (k2 - k1) * i1 - moment.zm, 2) / 2;
                    }
                }
            }

            // просматриваем сосредоточенные силы
            k = 0;
            for (Force force : beam.getForces()) {
                if (force.zs < Li.get(j)) {
                    k++;
                    souta = souta + "+<i>F</i><sub>" + k + "</sub>";
                    soutm = soutm + "+<i>F</i><sub>" + k + "</sub>(<i>z</i>&minus;" + str(force.zs) + ")";
                    soutt = soutt + "+<i>F</i><sub>" + k + "</sub>(<i>z</i>&minus;" + str(force.zs) + ")<sup>2</sup>/2";
                    soutw = soutw + "+<i>F</i><sub>" + k + "</sub>(<i>z</i>&minus;" + str(force.zs) + ")<sup>3</sup>/6";
                    sout1 = sout1 + signStr(force.vs);
                    soutm1 = soutm1 + signStr(force.vs) + "&middot;(<i>z</i>&minus;" + str(force.zs) + ")";
                    soutt1 = soutt1 + signStr(force.vs) + "&middot;(<i>z</i>&minus;" + str(force.zs) + ")<sup>2</sup>/2";
                    soutw1 = soutw1 + signStr(force.vs) + "&middot;(<i>z</i>&minus;" + str(force.zs) + ")<sup>3</sup>/6";
                    for (int i1 = 0; i1 < k2 - k1 + 1; i1++) {
                        yq[kpr + i1] = yq[kpr + i1] + force.vs;
                        ym[kpr + i1] = ym[kpr + i1] + force.vs * (Li.get(j - 1) + (Li.get(j) - Li.get(j - 1)) / (k2 - k1) * i1 - force.zs);
                        yt[kpr + i1] = yt[kpr + i1] + force.vs * Math.pow(Li.get(j - 1) + (Li.get(j) - Li.get(j - 1)) / (k2 - k1) * i1 - force.zs, 2) / 2;
                        yw[kpr + i1] = yw[kpr + i1] + force.vs * Math.pow(Li.get(j - 1) + (Li.get(j) - Li.get(j - 1)) / (k2 - k1) * i1 - force.zs, 3) / 6;
                    }
                }
            }

            // просматриваем начала распределённых нагрузок
            k = 0;
            for (DistributedForce distributedForce : beam.getDistributedForces()) {
                if (distributedForce.z1 < Li.get(j)) {
                    k++;
                    double ck = (distributedForce.q2 - distributedForce.q1) / (distributedForce.z2 - distributedForce.z1);
                    souta = souta + "+<i>q</i><sub><i>a</i>" + k + "</sub>(<i>z</i>&minus;" + str(distributedForce.z1) + ")";
                    soutm = soutm + "+<i>q</i><sub><i>a</i>" + k + "</sub>(<i>z</i>&minus;" + str(distributedForce.z1) + ")<sup>2</sup>/2";
                    soutt = soutt + "+<i>q</i><sub><i>a</i>" + k + "</sub>(<i>z</i>&minus;" + str(distributedForce.z1) + ")<sup>3</sup>/6";
                    soutw = soutw + "+<i>q</i><sub><i>a</i>" + k + "</sub>(<i>z</i>&minus;" + str(distributedForce.z1) + ")<sup>4</sup>/24";
                    sout1 = sout1 + signStr(distributedForce.q1) + "&middot;(<i>z</i>&minus;" + str(distributedForce.z1) + ")";
                    soutm1 = soutm1 + signStr(distributedForce.q1) + "&middot;(<i>z</i>&minus;" + str(distributedForce.z1) + ")<sup>2</sup>/2";
                    soutt1 = soutt1 + signStr(distributedForce.q1) + "&middot;(<i>z</i>&minus;" + str(distributedForce.z1) + ")<sup>3</sup>/6";
                    soutw1 = soutw1 + signStr(distributedForce.q1) + "&middot;(<i>z</i>&minus;" + str(distributedForce.z1) + ")<sup>4</sup>/24";
                    if (Math.abs(ck) > 1e-8) {
                        souta = souta + "+<i>c</i><sub>" + k + "</sub>(<i>z</i>&minus;" + str(distributedForce.z1) + ")<sup>2</sup>/2";
                        soutm = soutm + "+<i>c</i><sub>" + k + "</sub>(<i>z</i>&minus;" + str(distributedForce.z1) + ")<sup>3</sup>/6";
                        soutt = soutt + "+<i>c</i><sub>" + k + "</sub>(<i>z</i>&minus;" + str(distributedForce.z1) + ")<sup>4</sup>/24";
                        soutw = soutw + "+<i>c</i><sub>" + k + "</sub>(<i>z</i>&minus;" + str(distributedForce.z1) + ")<sup>5</sup>/120";
                        sout1 = sout1 + signStr(ck) + "&middot;(<i>z</i>" + signStr(-distributedForce.z1) + ")<sup>2</sup>/2";
                        soutm1 = soutm1 + signStr(ck) + "&middot;(<i>z</i>" + signStr(-distributedForce.z1) + ")<sup>3</sup>/6";
                        soutt1 = soutt1 + signStr(ck) + "&middot;(<i>z</i>" + signStr(-distributedForce.z1) + ")<sup>4</sup>/24";
                        soutw1 = soutw1 + signStr(ck) + "&middot;(<i>z</i>" + signStr(-distributedForce.z1) + ")<sup>5</sup>/120";
                    }

                    for (int i1 = 0; i1 < k2 - k1 + 1; i1++) {
                        yq[kpr + i1] = yq[kpr + i1] + distributedForce.q1 * (Li.get(j - 1) + (Li.get(j) - Li.get(j - 1)) / (k2 - k1) * i1 - distributedForce.z1) + ck * Math.pow(Li.get(j - 1) + (Li.get(j) - Li.get(j - 1)) / (k2 - k1) * i1 - distributedForce.z1, 2) / 2;
                        ym[kpr + i1] = ym[kpr + i1] + distributedForce.q1 * Math.pow(Li.get(j - 1) + (Li.get(j) - Li.get(j - 1)) / (k2 - k1) * i1 - distributedForce.z1, 2) / 2 + ck * Math.pow(Li.get(j - 1) + (Li.get(j) - Li.get(j - 1)) / (k2 - k1) * i1 - distributedForce.z1, 3) / 6;
                        yt[kpr + i1] = yt[kpr + i1] + distributedForce.q1 * Math.pow(Li.get(j - 1) + (Li.get(j) - Li.get(j - 1)) / (k2 - k1) * i1 - distributedForce.z1, 3) / 6 + ck * Math.pow(Li.get(j - 1) + (Li.get(j) - Li.get(j - 1)) / (k2 - k1) * i1 - distributedForce.z1, 4) / 24;
                        yw[kpr + i1] = yw[kpr + i1] + distributedForce.q1 * Math.pow(Li.get(j - 1) + (Li.get(j) - Li.get(j - 1)) / (k2 - k1) * i1 - distributedForce.z1, 4) / 24 + ck * Math.pow(Li.get(j - 1) + (Li.get(j) - Li.get(j - 1)) / (k2 - k1) * i1 - distributedForce.z1, 5) / 120;
                    }
                }
            }

            // просматриваем концы распределённых нагрузок
            k = 0;
            for (DistributedForce distributedForce : beam.getDistributedForces()) {
                if (distributedForce.z2 < Li.get(j)) {
                    k++;
                    double ck = (distributedForce.q2 - distributedForce.q1) / (distributedForce.z2 - distributedForce.z1);
                    souta = souta + "&minus;<i>q</i><sub><i>b</i>" + k + "</sub>(<i>z</i>&minus;" + str(distributedForce.z2) + ")";
                    soutm = soutm + "&minus;<i>q</i><sub><i>b</i>" + k + "</sub>(<i>z</i>&minus;" + str(distributedForce.z2) + ")<sup>2</sup>/2";
                    soutt = soutt + "&minus;<i>q</i><sub><i>b</i>" + k + "</sub>(<i>z</i>&minus;" + str(distributedForce.z2) + ")<sup>3</sup>/6";
                    soutw = soutw + "&minus;<i>q</i><sub><i>b</i>" + k + "</sub>(<i>z</i>&minus;" + str(distributedForce.z2) + ")<sup>4</sup>/24";
                    sout1 = sout1 + signStr(-distributedForce.q2) + "&middot;(<i>z</i>&minus;" + str(distributedForce.z2) + ")";
                    soutm1 = soutm1 + signStr(-distributedForce.q2) + "&middot;(<i>z</i>&minus;" + str(distributedForce.z2) + ")<sup>2</sup>/2";
                    soutt1 = soutt1 + signStr(-distributedForce.q2) + "&middot;(<i>z</i>&minus;" + str(distributedForce.z2) + ")<sup>3</sup>/6";
                    soutw1 = soutw1 + signStr(-distributedForce.q2) + "&middot;(<i>z</i>&minus;" + str(distributedForce.z2) + ")<sup>4</sup>/24";
                    if (Math.abs(ck) > 1e-8) {
                        souta = souta + "&minus;<i>c</i><sub>" + k + "</sub>(<i>z</i>&minus;" + str(distributedForce.z2) + ")<sup>2</sup>/2";
                        soutm = soutm + "&minus;<i>c</i><sub>" + k + "</sub>(<i>z</i>&minus;" + str(distributedForce.z2) + ")<sup>3</sup>/6";
                        soutt = soutt + "&minus;<i>c</i><sub>" + k + "</sub>(<i>z</i>&minus;" + str(distributedForce.z2) + ")<sup>4</sup>/24";
                        soutw = soutw + "&minus;<i>c</i><sub>" + k + "</sub>(<i>z</i>&minus;" + str(distributedForce.z2) + ")<sup>5</sup>/120";
                        sout1 = sout1 + signStr(-ck) + "&middot;(<i>z</i>" + signStr(-distributedForce.z2) + ")<sup>2</sup>/2";
                        soutm1 = soutm1 + signStr(-ck) + "&middot;(<i>z</i>" + signStr(-distributedForce.z2) + ")<sup>3</sup>/6";
                        soutt1 = soutt1 + signStr(-ck) + "&middot;(<i>z</i>" + signStr(-distributedForce.z2) + ")<sup>4</sup>/24";
                        soutw1 = soutw1 + signStr(-ck) + "&middot;(<i>z</i>" + signStr(-distributedForce.z2) + ")<sup>5</sup>/120";
                    }

                    for (int i1 = 0; i1 < k2 - k1 + 1; i1++) {
                        yq[kpr + i1] = yq[kpr + i1] - distributedForce.q2 * (Li.get(j - 1) + (Li.get(j) - Li.get(j - 1)) / (k2 - k1) * i1 - distributedForce.z2) - ck * Math.pow(Li.get(j - 1) + (Li.get(j) - Li.get(j - 1)) / (k2 - k1) * i1 - distributedForce.z2, 2) / 2;
                        ym[kpr + i1] = ym[kpr + i1] - distributedForce.q2 * Math.pow(Li.get(j - 1) + (Li.get(j) - Li.get(j - 1)) / (k2 - k1) * i1 - distributedForce.z2, 2) / 2 - ck * Math.pow(Li.get(j - 1) + (Li.get(j) - Li.get(j - 1)) / (k2 - k1) * i1 - distributedForce.z2, 3) / 6;
                        yt[kpr + i1] = yt[kpr + i1] - distributedForce.q2 * Math.pow(Li.get(j - 1) + (Li.get(j) - Li.get(j - 1)) / (k2 - k1) * i1 - distributedForce.z2, 3) / 6 - ck * Math.pow(Li.get(j - 1) + (Li.get(j) - Li.get(j - 1)) / (k2 - k1) * i1 - distributedForce.z2, 4) / 24;
                        yw[kpr + i1] = yw[kpr + i1] - distributedForce.q2 * Math.pow(Li.get(j - 1) + (Li.get(j) - Li.get(j - 1)) / (k2 - k1) * i1 - distributedForce.z2, 4) / 24 - ck * Math.pow(Li.get(j - 1) + (Li.get(j) - Li.get(j - 1)) / (k2 - k1) * i1 - distributedForce.z2, 5) / 120;
                    }
                }
            }

            souta = souta + " = " + sout1 + "<br";
            soutm = soutm + " = " + soutm1 + "<br>";
            soutt = soutt + " = " + soutt1 + "<br>";
            soutw = soutw + " = " + soutw1 + "<br>";

            kpr = kpr + (k2 - k1 + 1);
        }
        result.append(souta);
        result.append(soutm);

        double zqmax = 0; // точка с Q=max
        double zmmax = 0; // точка с M=max
        double zwmax = 0; // точка с w=max
        double wmax = 0;
        double tmax = 0;
        double Qmmax = 0; // Q в точке, где M=max
        double yqmax = 0;
        double ymmax = 0;
        for (int i = 0; i < kpr; i++) {
            if (Math.abs(yq[i]) > yqmax) {
                yqmax = Math.abs(yq[i]);
                zqmax = (xx[i] - 50) / 700 * beam.getLength();
            }

            if (Math.abs(ym[i]) > ymmax) {
                ymmax = Math.abs(ym[i]);
                zmmax = (xx[i] - 50) / 700 * beam.getLength();
                Qmmax = Math.abs(yq[i]);
            }

            if (Math.abs(yw[i]) > wmax) {
                wmax = Math.abs(yw[i]);
                zwmax = (xx[i] - 50) / 700 * beam.getLength();
            }

            if (Math.abs(yt[i]) > tmax)
                tmax = Math.abs(yt[i]);
        }

        double vscq = 1;
        if (yqmax > 0) vscq = 200 / yqmax; // масштаб Q

        double vscm = 1;
        if (ymmax > 0) vscm = 200 / ymmax; // масштаб M

        double vsct = 1;
        if (tmax > 0) vsct = 200 / tmax; // масштаб theta

        double vscw = 1;
        if (wmax > 0) vscw = 200 / wmax; // масштаб w

        // Эпюра перерезывающих сил
        drawShearingForces(beam, beam.solutions[1].getG2d(), kpr, yqmax, xx, yq, vscq);

        drawBendingMoments(beam, beam.solutions[2].getG2d(), kpr, ymmax, xx, ym, vscm);

        sout = "<p>Максимальный изгибающий момент |<i>M</i><sub>max</sub>| = " + str(ymmax) + " кНм, он достигается при <i>z</i> = " + str(zmmax) + " м.</p>";
        result.append(sout);

        // ищем нужный профиль
        double Wmin = ymmax / beam.getAllowableStress() * 1000; // минимальный момент сопротивления
        Profile profile = null;
        for (int i = 0; i < profiles.size(); i++) {
            profile = profiles.get(i);
            if (profile.Wx >= Wmin) {
                profile.calc(ymmax, yqmax, Qmmax);
                break;
            }
        }
        if (profile == null) return "<center><b>Слишком большие нагрузки - балку подобрать не удаётся</b></center>";

        // Подбор сечения по условиям прочности
        sout = "Минимально допустимый момент сопротивления <i>W</i><sub><i>x</i> min</sub> = |<i>M</i><sub>max</sub>|/[&sigma;] = " +
                str(ymmax) + "/" + str(beam.getAllowableStress()) +
                "&middot;10<sup>3</sup> = " + str(Wmin) + " см<sup>3</sup>.<br>";
        sout = sout + "<b>Выбираем двутавр №" + profile.name + "</b>. Его характеристики:";
        sout = sout + "<ul><li>Момент инерции <i>J<sub>x</sub></i> = " + str(profile.Jx) + " см<sup>4</sup>;</li>";
        sout = sout + "<li>Момент сопротивления <i>W<sub>x</sub></i> = " + str(profile.Wx) + " см<sup>3</sup>;</li>";
        sout = sout + "<li>Статический момент <i>S<sub>x</sub></i> = " + str(profile.Sx) + " см<sup>3</sup>;</li>";
        sout = sout + "<li>Максимальное нормальное напряжение в опасном сечении &sigma;<sub>max</sub> = |<i>M</i><sub>max</sub>|/<i>W</i><sub>x</sub>&middot;10<sup>3</sup> = " + str(profile.sigmax) + " МПа.</li></ul>";
        result.append(sout);

        // касательные напряжения в двух сечениях: в том, где максимален изгибающий момент, и в том, где максимальна перерезывающая сила
        sout = "Найдём касательные напряжения в двух сечениях: в том, где максимален изгибающий момент, и в том, где максимальна перерезывающая сила.<br>";
        sout = sout + "Максимальная перерезывающая сила |<i>Q</i><sub>max</sub>| = " + str(yqmax) + " кН, она достигается при <i>z</i> = " + str(zqmax) + " м. ";
        sout = sout + "При <i>z</i> = " + str(zmmax) + " м: |<i>Q</i><sub>max</sub>| = " + str(Qmmax) + " кН.<br>";
        sout = sout + "Касательные напряжения:";
        sout = sout + "<ul><li>при <i>z</i> = " + str(zqmax) + " м: &tau;<sub>max</sub> = " + str(profile.tqmax) + " МПа;</li><li>при <i>z</i> = " + str(zmmax) + " м: &tau;<sub>max</sub> = " + str(profile.tmmax) + " МПа.</li></ul>";
        sout = sout + "Как правило, касательные напряжения значительно меньше нормальных в одном и том же сечении, к тому же они достигаются на разных волокнах: нормальные − на крайних, а касательные − в середине сечения. Поэтому обычно опасными являются нормальные напряжения.<br><br>";
        result.append(sout);

        result.append("<b>Построение эпюры углов поворота</b>");
        result.append(soutt);

        result.append("<b>Построение эпюры прогибов (перемещений)</b>");
        soutw = soutw + "<p>Максимальный по модулю прогиб |<i>w</i><sub>max</sub>| = " +
                str(wmax / beam.getElasticStrength() / profile.Jx * 1e8) +
                " мм, он достигается при <i>z</i> = " + str(zwmax) + " м.</p>";
        result.append(soutw);

        // Распределение напряжений в опасном сечении
        drawNapr(beam, beam.solutions[3].getG2d(), profile);

        // Эпюра углов поворота
        drawRotationCurve(beam, beam.solutions[4].getG2d(), kpr, tmax, profile.Jx, xx, yt, vsct);

        // Эпюра перемещений
        drawMovementsCurve(beam, beam.solutions[5].getG2d(), kpr, wmax, profile.Jx, xx, yw, vscw);

        if (beam.getMoments().isEmpty() &&
                beam.getForces().isEmpty() &&
                beam.getDistributedForces().isEmpty()) return "";
        return result.toString();
    }

    // Рис "Балочка и нагрузка на неё"
    private static void drawActions(Beam beam, Graphics2D g2d, double vsc) {
        drawBalka(beam, g2d);

        double[] y = new double[11];
        for (int i = 0 ; i < 11; i ++) y[i] = beam.getLength() / 10 * i;
        drawScale(beam, g2d, y, "м");

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1));

        int nm = 0;
        for (Moment moment : beam.getMoments()) {
            int x1 = (int)Math.round(moment.zm / beam.getLength() * 700) + 50; // точка приложения в pxl
            int v1 = (int)Math.round(moment.vm * vsc); // величина нагрузки в pxl
            GeneralPath shape = new GeneralPath();
            shape.moveTo(x1-40, v1+3+250);
            shape.lineTo(x1-50, v1+250);
            shape.lineTo(x1-40, v1-3+250);
            shape.lineTo(x1-50, v1+250);
            shape.lineTo(x1, v1+250);
            shape.lineTo(x1, -v1+250);
            shape.lineTo(x1+50, -v1+250);
            shape.lineTo(x1+40, -v1-3+250);
            shape.lineTo(x1+50, -v1+250);
            shape.lineTo(x1+40, -v1+3+250);
            g2d.draw(shape);

            g2d.setFont(new Font("Times New Roman", Font.PLAIN, 20));
            g2d.drawString("M", x1 + Math.round(25 * (v1 / Math.abs(v1))) - 45, 230 - Math.abs(v1));

            g2d.setFont(new Font("Times New Roman", Font.PLAIN, 14));
            g2d.drawString(Integer.toString(++nm), x1 + Math.round(25 * (v1 / Math.abs(v1))) - 28, 234 - Math.abs(v1));

            g2d.setFont(new Font("Times New Roman", Font.PLAIN, 20));
            g2d.drawString("=" + str(moment.vm) + " кН*м", x1 + Math.round(25 * (v1 / Math.abs(v1))) - 20, 230 - Math.abs(v1));
        }

        int nf = 0;
        for (Force force : beam.getForces()) {
            int x1 = (int)Math.round(force.zs / beam.getLength() * 700) + 50; // точка приложения в pxl
            int v1 = (int)Math.round(force.vs * vsc); // величина нагрузки в pxl
            GeneralPath shape = new GeneralPath();
            shape.moveTo(x1,v1 + 250);
            shape.lineTo(x1, Math.round(v1 / Math.abs(v1)) + 250);
            shape.lineTo(x1 - 3, 11 * Math.round(v1 / Math.abs(v1)) + 250);
            shape.lineTo(x1, Math.round(v1 / Math.abs(v1)) + 250);
            shape.lineTo(x1 + 3, 11 * Math.round(v1 / Math.abs(v1)) + 250);
            g2d.draw(shape);

            g2d.setFont(new Font("Times New Roman", Font.PLAIN, 20));
            g2d.drawString("F", x1 - 40, v1 + Math.round(20 * (v1 / Math.abs(v1))) + 250);

            g2d.setFont(new Font("Times New Roman", Font.PLAIN, 14));
            g2d.drawString(Integer.toString(++nf), x1 - 30, v1 + Math.round(20 * (v1 / Math.abs(v1))) + 256);

            g2d.setFont(new Font("Times New Roman", Font.PLAIN, 20));
            g2d.drawString("=" + str(force.vs) + " кН", x1 - 20, v1 + Math.round(20 * (v1 / Math.abs(v1))) + 250);
        }

        int nq = 0;
        for (DistributedForce distributedForce : beam.getDistributedForces()) {
            int x1 = (int)Math.round(distributedForce.z1 / beam.getLength() * 700) + 50; // точка приложения в pxl
            int x2 = (int)Math.round(distributedForce.z2 / beam.getLength() * 700) + 50; // точка приложения в pxl
            int v1 = (int)Math.round(distributedForce.q1 * vsc); // величина нагрузки в pxl
            int v2 = (int)Math.round(distributedForce.q2 * vsc); // величина нагрузки в pxl

            g2d.drawLine(x1, 250, x1, v1 + 250);
            g2d.drawLine(x1, v1 + 250, x2, v2 + 250);
            g2d.drawLine(x2, v2 + 250, x2, 250);

            int ccc;
            int n2 = (int)Math.ceil((x2 - x1) * 0.1);
            for (int j2 = 0; j2 < n2; j2++) {
                double dx = x2 - x1; // switch from int to double
                double tmp = v1 + (v2 - v1) / dx * 10 * j2;
                if (tmp == 0) ccc = 1;
                else ccc = (int)Math.round(tmp / Math.abs(tmp));

                GeneralPath shape = new GeneralPath();
                shape.moveTo(x1+10*j2, Math.round(tmp)+250);
                shape.lineTo(x1+10*j2, 250);
                shape.lineTo(x1+10*j2-3, 11*ccc+250);
                shape.lineTo(x1+10*j2, 250);
                shape.lineTo(x1+10*j2+3, 11*ccc+250);
                g2d.draw(shape);
            }

            g2d.setFont(new Font("Times New Roman", Font.PLAIN, 20));
            g2d.drawString("q", Math.round((x1 + x2) / 2) - 40, 220 + 250);
            g2d.setFont(new Font("Times New Roman", Font.PLAIN, 14));
            g2d.drawString(Integer.toString(++nq), Math.round((x1 + x2) / 2) - 30, 228 + 250);
            g2d.setFont(new Font("Times New Roman", Font.PLAIN, 20));
            g2d.drawString("=[" + str(distributedForce.q1) + ", " + str(distributedForce.q2) +
                    "] кН/м", Math.round((x1+x2)/2)-24, 220+250);
        }
    }

    // Эпюра перерезывающих сил
    private static void drawShearingForces(Beam beam, Graphics2D g2d, int kpr, double yqmax, double[] xx, double[] yq, double vscq) {
        drawBalka(beam, g2d);

        double[] y = new double[11];
        for (int i = 0 ; i < 11; i ++) y[i] = yqmax - yqmax / 5 * i;
        drawScale(beam, g2d, y, "кН");

        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(3));
        GeneralPath shape = new GeneralPath();
        shape.moveTo(50, 250);
        for (int i = 1; i < kpr; i++) {
            shape.lineTo(xx[i], 250 - (int)Math.round(yq[i] * vscq));
        }
        g2d.draw(shape);
    }

    // Эпюра изгибающих моментов
    private static void drawBendingMoments(Beam beam, Graphics2D g2d, int kpr, double ymmax, double[] xx, double[] ym, double vscm) {
        drawBalka(beam, g2d);

        double[] y = new double[11];
        for (int i = 0 ; i < 11; i ++) y[i] = ymmax - ymmax / 5 * i;
        drawScale(beam, g2d, y, "кН*м");

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1));
        GeneralPath shape = new GeneralPath();
        shape.moveTo(50, 250);
        for (int i = 1; i < kpr; i++) {
            shape.lineTo(xx[i], 250 - (int)Math.round(ym[i] * vscm));
        }
        g2d.draw(shape);
    }

    // Распределение напряжений в опасном сечении
    private static void drawNapr(Beam beam, Graphics2D g2d, Profile profile) {
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Times New Roman", Font.PLAIN, 20));

        g2d.setColor(Color.BLACK);
        for (int i = 0; i < 5; i++) {
            g2d.setStroke(new BasicStroke(1));
            g2d.drawLine(50 + i * 175, 500, 50 + i * 175, 495);
            g2d.drawString(str(beam.getAllowableStress() / 2 * (i - 2)), 40 + i * 175, 494);
            //g2d.drawLine(0, 50 + i * 100, 5, 50 + i * 100);
            g2d.drawString(str(profile.h / 4 * (2 - i)), 0, 55 + i * 100);

            g2d.setStroke(dashed);
            g2d.drawLine(50 + i * 175, 30, 50 + i * 175, 470);
            g2d.drawLine(60, 50 + i * 100, 800, 50 + i * 100);
        }

        g2d.setStroke(new BasicStroke(1));
        g2d.drawString("Па", 774, 494);
        g2d.drawString("мм", 4, 16);

        double[] xxx = Arrays.copyOf(profile.xx, profile.xx.length);
        double[] yy = Arrays.copyOf(profile.yy, profile.yy.length);
        double dwusc = 400 / profile.h;
        for (int i = 0; i < 92; i++) {
            xxx[i] = Math.round((10 * xxx[i] - profile.b / 2) * dwusc) + 400;
            yy[i] = 250 - Math.round((10 * yy[i] - profile.h / 2) * dwusc);
        }

        g2d.setColor(Color.BLUE);
        g2d.setStroke(new BasicStroke(3));
        GeneralPath shape = new GeneralPath();
        shape.moveTo((int)Math.round(xxx[0]), (int)Math.round(yy[0]));
        for (int i = 1; i < 92; i++) {
            shape.lineTo((int)Math.round(xxx[i]), (int)Math.round(yy[i]));
        }
        shape.closePath();
        g2d.draw(shape);

        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawLine(400, 50, 400 + (int)Math.round(profile.sigmax / beam.getAllowableStress() * 350), 50);
        g2d.drawLine(400 + (int)Math.round(profile.sigmax / beam.getAllowableStress() * 350), 50, 400 - (int)Math.round(profile.sigmax / beam.getAllowableStress() * 350), 450);
        g2d.drawLine(400 - (int)Math.round(profile.sigmax / beam.getAllowableStress() * 350), 450, 400, 450);

        double[] ydwut = new double[205];
        double[] xdwut = new double[205];
        ydwut[0] = 0;
        xdwut[0] = 0;
        ydwut[1] = profile.tt;
        xdwut[1] = 0.1 * profile.tmmax;
        for (int i = 2; i < 203; i++) {
            ydwut[i] = profile.tt + (i - 2) * (profile.h - 2 * profile.tt) / 200;
            xdwut[i] = profile.tmmax - Math.pow((i - 102) / 100, 2) * 0.2 * profile.tmmax;
        }

        ydwut[203] = profile.h - profile.tt;
        xdwut[203] = 0.1 * profile.tmmax;
        ydwut[204] = profile.h;
        xdwut[204] = 0;

        int x1 = 400 + (int)Math.round(xdwut[0] / beam.getAllowableStress() * 350);
        int y1 = 250 - (int)Math.round((ydwut[0] - profile.h / 2) / profile.h * 400);
        for (int i = 1; i < 205; i++) {
            int x2 = 400 + (int)Math.round(xdwut[i] / beam.getAllowableStress() / profile.tqmax * profile.tmmax * 350);
            int y2 = 250 - (int)Math.round((ydwut[i] - profile.h / 2) / profile.h * 400);
            g2d.drawLine(x1, y1, x2, y2);
            x1 = x2;
            y1 = y2;
        }

        g2d.setColor(Color.GREEN);
        g2d.setStroke(new BasicStroke(2));
        x1 = 400 + (int)Math.round(xdwut[0] / beam.getAllowableStress() * 350);
        y1 = 250 - (int)Math.round((ydwut[0] - profile.h / 2) / profile.h * 400);
        for (int i = 1; i < 205; i ++) {
            int x2 = 400 + (int)Math.round(xdwut[i] / beam.getAllowableStress() * 350);
            int y2 = 250 - (int)Math.round((ydwut[i] - profile.h / 2) / profile.h * 400);
            g2d.drawLine(x1, y1, x2, y2);
            x1 = x2;
            y1 = y2;
        }
    }

    // Эпюра углов поворота
    private static void drawRotationCurve(Beam beam, Graphics2D g2d, int kpr, double tmax, double Jx, double[] xx, double[] yt, double vsct) {
        double[] y = new double[11];
        for (int i = 0 ; i < 11; i ++) y[i] =  tmax / beam.getElasticStrength() / Jx*1e5 - tmax / beam.getElasticStrength() / Jx*1e5 / 5 * i;
        drawCurve(beam, g2d, kpr, y, "радиан", xx, yt, vsct);
    }

    // Эпюра перемещений
    private static void drawMovementsCurve(Beam beam, Graphics2D g2d, int kpr, double wmax, double Jx, double[] xx, double[] yw, double vscw) {
        double[] y = new double[11];
        for (int i = 0 ; i < 11; i ++) y[i] = wmax / beam.getElasticStrength() / Jx*1e8 - wmax / beam.getElasticStrength() / Jx*1e8 / 5 * i;
        drawCurve(beam, g2d, kpr, y, "мм", xx, yw, vscw);
    }

    private static void drawCurve(Beam beam, Graphics2D g2d, int kpr, double[] y, String ei, double[] xx, double[] yw, double v) {
        drawBalka(beam, g2d);
        drawScale(beam, g2d, y, ei);

        GeneralPath shape = new GeneralPath();
        shape.moveTo(50, 250);
        for (int i = 1; i < kpr; i ++) {
            shape.lineTo(xx[i], 250 - (int)Math.round(yw[i] * v));
        }
        g2d.draw(shape);
    }

    private static final float dash1[] = {2.0f};
    private static final BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);

    private static void drawScale(Beam beam, Graphics2D g2d, double[] y, String ei) {
        g2d.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        int a = (int)Math.round(beam.getLeftSupport() / beam.getLength() * 700);
        int b = (int)Math.round(beam.getRightSupport() / beam.getLength() * 700);
        for (int i = 0; i < 11; i++) {
            g2d.setStroke(new BasicStroke(1));
            g2d.drawLine(a + 30 + 4*i, 278, a + 20 + 4*i, 288);
            g2d.drawLine(b + 30 + 4*i, 283, b + 20 + 4*i, 293);
            g2d.drawLine(50 + i*70, 500, 50 + i*70, 495);
            g2d.drawString(str(beam.getLength() / 10 * i), 40 + i*70, 494);
            //g2d.drawLine(0, 50 + i*40, 5, 50 + i*40);
            g2d.drawString(str(y[i]), 0, 55 + i*40);

            g2d.setStroke(dashed);
            g2d.drawLine(50 + i * 70, 30, 50 + i * 70, 470);
            g2d.drawLine(60, 50 + i * 40, 800, 50 + i * 40);
        }

        g2d.setStroke(new BasicStroke(1));
        g2d.drawString("м", 782, 494);
        g2d.drawString(ei, 4, 16);
    }

    // Балка
    private static void drawBalka(Beam beam, Graphics2D g2d) {
        g2d.setColor(Color.BLACK);

        int lx = (int)Math.round(beam.getLeftSupport()/beam.getLength()*700);
        int rx = (int)Math.round(beam.getRightSupport()/beam.getLength()*700);

        g2d.setStroke(new BasicStroke(5));
        g2d.drawLine(50, 250, 750, 250);

        g2d.setStroke(new BasicStroke(1));
        g2d.drawArc(lx + 50 - 4, 255 - 4, 8, 8, 0, 360);
        g2d.drawArc(rx + 50 - 4, 255 - 4, 8, 8, 0, 360);
        g2d.drawArc(rx + 50 - 4, 278 - 4, 8, 8, 0, 360);

        g2d.setStroke(new BasicStroke(3));
        GeneralPath shape = new GeneralPath();
        shape.moveTo(lx + 50, 259);
        shape.lineTo(lx + 40, 278);
        shape.lineTo(lx + 60, 278);
        shape.closePath();
        g2d.draw(shape);

        shape = new GeneralPath();
        shape.moveTo(rx + 50, 259);
        shape.lineTo(rx + 50, 274);
        shape.moveTo(lx + 30, 278);
        shape.lineTo(lx + 70, 278);
        shape.moveTo(rx + 30, 283);
        shape.lineTo(rx + 70, 283);
        g2d.draw(shape);
    }
    
    private static String str(double val) {
        return StringUtil.toString(val, 3);
    }

    private static String signStr(double val) {
        String res = str(Math.abs(val));
        if (val < 0) return "&minus;" + res;
        return "+" + res;
    }
}
