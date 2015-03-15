package com.tsoft.myprocad.lib.mm;

import com.tsoft.myprocad.model.calculation.Load1;
import com.tsoft.myprocad.model.calculation.Load2;
import com.tsoft.myprocad.model.calculation.WoodBeam;
import com.tsoft.myprocad.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Based on http://www.stroyka-voprosy.ru/?option=com_content&view=article&id=19:perekrytia-osobennosti-raschet-nesushih-balok&catid=3:perekrytia&Itemid=5
 * Another calculation (Flash) http://vladirom.narod.ru/stoves/beamcalc.html
 * http://ostroykevse.ru/Krisha/Krisha_page_11.html
 */
public class WoodBeamLib {
    // Балка прямоугольного сечения
    private static class WoodRect {
        private double h; // высота в см
        private double w; // ширина в см
        private double W; // момент сопротивления, см^3
        private double J; // момент инерции, см^4

        // Calculated values
        private String name;
        private double gn; // нагрузка на 1 погонный метр балки при заданной ширине зоны сбора нагрузки, кгс/п.м
        private double gn_cm; // тоже самое, но кгс/п.см
        private double beamWeight; // собственный вес 1 пог.м балки
        private String beamWeightStr; // формула
        private double M; // изгибающий момент М
        private double W_calc; // рассчитанный момент сопротивления
        private double f; // прогиб, мм

        private WoodRect(double h, double w, double w1, double j) {
            this.h = h;
            this.w = w;
            W = w1;
            J = j;
        }

        // Often used beams
        boolean isStandard() { return (h % 5) == 0 && (w % 5) == 0; }

        void clear() {
            name = "";
            gn = 0;
            f = 0;
            M = 0;
            W_calc = 0;
            gn_cm = 0;
            beamWeight = 0;
            beamWeightStr = "";
        }
    }

    private static List<WoodRect> woodRects;

    static {
        woodRects = new ArrayList<>();
        woodRects.add(new WoodRect(5, 5, 20.8, 52.1 ));
        woodRects.add(new WoodRect(6, 5, 30, 90 ));
        woodRects.add(new WoodRect(8, 5, 5.3, 213.2 ));
        woodRects.add(new WoodRect(10, 5, 83, 417 ));
        woodRects.add(new WoodRect(12, 5, 120, 720 ));
        woodRects.add(new WoodRect(15, 5, 187, 1406 ));
        woodRects.add(new WoodRect(18, 5, 270, 2430 ));
        woodRects.add(new WoodRect(20, 5, 333, 3333 ));
        woodRects.add(new WoodRect(22, 5, 403, 4435 ));

        woodRects.add(new WoodRect(5, 6, 25, 62.5 ));
        woodRects.add(new WoodRect(8, 6, 64, 256 ));
        woodRects.add(new WoodRect(10, 6, 100, 500 ));
        woodRects.add(new WoodRect(12, 6, 144, 864 ));
        woodRects.add(new WoodRect(15, 6, 225, 1685 ));
        woodRects.add(new WoodRect(18, 6, 324, 2916 ));
        woodRects.add(new WoodRect(20, 6, 400, 4000 ));
        woodRects.add(new WoodRect(22, 6, 484, 5324 ));

        woodRects.add(new WoodRect(5, 8, 33.3, 83.3 ));
        woodRects.add(new WoodRect(6, 8, 48, 144 ));
        woodRects.add(new WoodRect(8, 8, 85, 340 ));
        woodRects.add(new WoodRect(10, 8, 133, 667 ));
        woodRects.add(new WoodRect(12, 8, 192, 1152 ));
        woodRects.add(new WoodRect(15, 8, 300, 2250 ));
        woodRects.add(new WoodRect(18, 8, 432, 3888 ));
        woodRects.add(new WoodRect(20, 8, 533, 5333 ));
        woodRects.add(new WoodRect(22, 8, 645, 7099 ));

        woodRects.add(new WoodRect(5, 10, 42, 104 ));
        woodRects.add(new WoodRect(6, 10, 60, 180 ));
        woodRects.add(new WoodRect(8, 10, 106, 426 ));
        woodRects.add(new WoodRect(10, 10, 167, 833 ));
        woodRects.add(new WoodRect(12, 10, 240, 1144 ));
        woodRects.add(new WoodRect(15, 10, 375, 2812 ));
        woodRects.add(new WoodRect(18, 10, 540, 4860 ));
        woodRects.add(new WoodRect(20, 10, 667, 6670 ));
        woodRects.add(new WoodRect(22, 10, 807, 8873 ));

        woodRects.add(new WoodRect(5, 12, 50, 125 ));
        woodRects.add(new WoodRect(6, 12, 72, 216 ));
        woodRects.add(new WoodRect(8, 12, 128, 512 ));
        woodRects.add(new WoodRect(10, 12, 202, 1000 ));
        woodRects.add(new WoodRect(12, 12, 288, 1728 ));
        woodRects.add(new WoodRect(15, 12, 450, 3345 ));
        woodRects.add(new WoodRect(18, 12, 648, 5830 ));
        woodRects.add(new WoodRect(20, 12, 800, 8000 ));
        woodRects.add(new WoodRect(22, 12, 965, 10640 ));

        woodRects.add(new WoodRect(5, 15, 62.5, 156.25 ));
        woodRects.add(new WoodRect(6, 15, 90, 270 ));
        woodRects.add(new WoodRect(8, 15, 160, 638 ));
        woodRects.add(new WoodRect(10, 15, 250, 1250 ));
        woodRects.add(new WoodRect(12, 15, 360, 2160 ));
        woodRects.add(new WoodRect(15, 15, 563, 4219 ));
        woodRects.add(new WoodRect(18, 15, 810, 7290 ));
        woodRects.add(new WoodRect(20, 15, 1000, 10000 ));
        woodRects.add(new WoodRect(22, 15, 1200, 13300 ));

        woodRects.add(new WoodRect(5, 18, 75, 187.5 ));
        woodRects.add(new WoodRect(6, 18, 108, 324 ));
        woodRects.add(new WoodRect(8, 18, 192, 758 ));
        woodRects.add(new WoodRect(10, 18, 300, 1500 ));
        woodRects.add(new WoodRect(12, 18, 432, 2584 ));
        woodRects.add(new WoodRect(15, 18, 675, 5051 ));
        woodRects.add(new WoodRect(18, 18, 972, 8748 ));
        woodRects.add(new WoodRect(20, 18, 1200, 12000 ));
        woodRects.add(new WoodRect(22, 18, 1450, 15970 ));

        woodRects.add(new WoodRect(5, 20, 83.3, 208.3 ));
        woodRects.add(new WoodRect(6, 20, 120, 360 ));
        woodRects.add(new WoodRect(8, 20, 216, 832 ));
        woodRects.add(new WoodRect(10, 20, 334, 1668 ));
        woodRects.add(new WoodRect(12, 20, 480, 2880 ));
        woodRects.add(new WoodRect(15, 20, 750, 5620 ));
        woodRects.add(new WoodRect(18, 20, 1080, 9700 ));
        woodRects.add(new WoodRect(20, 20, 1333, 13333 ));
        woodRects.add(new WoodRect(22, 20, 1610, 17750 ));

        woodRects.add(new WoodRect(5, 22, 92, 230 ));
        woodRects.add(new WoodRect(6, 22, 132, 296 ));
        woodRects.add(new WoodRect(8, 22, 236, 938 ));
        woodRects.add(new WoodRect(10, 22, 366.5, 1833 ));
        woodRects.add(new WoodRect(12, 22, 528, 3164 ));
        woodRects.add(new WoodRect(15, 22, 825, 6200 ));
        woodRects.add(new WoodRect(18, 22, 1188, 10680 ));
        woodRects.add(new WoodRect(20, 22, 1469, 14665 ));
        woodRects.add(new WoodRect(22, 22, 1775, 19214 ));
    }

    public static String calcStatic(WoodBeam woodBeam) {
        for (WoodRect wr : woodRects) wr.clear();

        String buf = "";
        buf += "<center><b>Расчёт деревянных несущих балок на двух опорах</b></center><br>";
        buf += "Определяем нагрузку от перекрытия, передающуюся на балку. Она состоит из собственного веса перекрытия ";
        buf += "(постоянная нагрузка) и нагрузки на перекрытие (временная нагрузка).<br><br>";

        // Part 1

        buf += "<b>Постоянная нагрузка</b><br>";
        buf += "Собственный вес 1 кв.м. перекрытия равен сумме составляющих:<br>";
        double permanentLoad = 0;
        String formula = "";
        int i = 1;
        for (Load1 load : woodBeam.getPermanentLoad()) {
            buf += (i++) + ") " + load.name + " " + str(load.density) + " кгс/м<sup>3</sup>, толщина слоя " + str(load.h*1000) + " мм.<br>";
            formula += (i > 2 ? "+" : "") + str(load.density) + "*" + str(load.h);

            permanentLoad += load.density * load.h;
        }
        buf += "Постоянная нагрузка: " + formula + "=" + str(permanentLoad, 1) + " кг/м<sup>2</sup><br><br>";

        buf += "<b>Временная нагрузка</b><br>";
        double temporaryLoad = 0;
        formula = "";
        i = 1;
        for (Load2 load : woodBeam.getTemporaryLoad()) {
            buf += (i++) + ") " + load.name + " " + str(load.value) + " кгс/м<sup>2</sup><br>";
            formula += (i > 2 ? "+" : "") + str(load.value);

            temporaryLoad += load.value;
        }
        buf += "Временная нагрузка: " + formula + "=" + str(temporaryLoad, 1) + " кг/м<sup>2</sup><br>";

        double load = temporaryLoad + permanentLoad;
        formula = str(permanentLoad, 1) + "+" + str(temporaryLoad, 1);
        buf += "<b>Итого нагрузка на 1 кв.м. перекрытия: " + formula + "=" + str(load, 0) + " кг/м<sup>2</sup></b><br><br>";

        // Part 2

        buf += "Использование лиственных пород дерева в качестве балок перекрытия недопустимо, так как они плохо работают на изгиб. ";
        buf += "Поэтому в качестве материала для изготовления деревянных балок перекрытия применяют хвойные породы древесины, ";
        buf += "очищенные от коры и антисептированные в обязательном порядке.<br>";
        buf += "Длина опорных концов балки должна быть не менее 15 см. Укладку балок ведут \"маячковым\" способом - вначале устанавливают ";
        buf += "крайние балки, а затем промежуточные. Правильность положения крайних балок проверяют уровнем или ватерпасом, а промежуточных ";
        buf += "- рейкой и шаблоном. Балки выравнивают, подкладывая под их концы просмоленные обрезки досок разной толщины. Подкладывать щепки ";
        buf += "или подтесывать концы балок не рекомендуется.<br>";
        buf += "Деревянные балки перекрытий укладывают как правило, по короткому сечению пролета по возможности параллельно друг другу и с ";
        buf += "одинаковым расстоянием между ними. Концы балок, опирающиеся на наружные стены, срезают наискось под углом 60 град., антисептируют, ";
        buf += "обжигают или обертывают двумя слоями толя или рубероида. При заделке деревянных балок в гнезда кирпичных стен рекомендуется концы ";
        buf += "балок обработать битумом и просушить, чтобы снизить вероятность гниения от увлажнения. Торцы балок обязательно оставляют открытыми.<br><br>";

        // Part 3

        // определяем минимально допустимое сечение
        double l_cm = woodBeam.getL() / 10.0; // длина балки в см
        double limitF = l_cm * woodBeam.getSag().getF() * 10.0;
        buf += "Для заданного элемента здания \"" + woodBeam.getSag().toString() + "\" при ширине пролета=" +
                str(l_cm) + " см, <b>максимально допустимый прогиб равен</b> " + str(limitF, 0) + " мм.<br><br>";

        // рассчитываем прогибы всех сечений балок
        for (WoodRect wr : woodRects) {
            wr.name = str(wr.h*10) + "x" + str(wr.w*10) + " мм";

            wr.beamWeight = wr.h/100 * wr.w/100 * 600;
            wr.beamWeightStr = str(wr.h/100) + "*" + str(wr.w/100) + "*600=" + str(wr.beamWeight) + " кг/м<sup>2</sup>";
            wr.gn = load * woodBeam.getB()/1000 + wr.beamWeight; // нагрузка на 1 погонный метр балки при ширине зоны сбора нагрузки, кгс/п.м

            wr.gn_cm = wr.gn / 100.0; // кгс/п.см
            wr.M = wr.gn * Math.pow(woodBeam.getL()/1000.0, 2) / 8;
            wr.W_calc = wr.M * 100.0 / 130.0;
            wr.f = (5.0/384.0) * (wr.gn_cm * Math.pow(l_cm, 4)) / (100000.0 * wr.J) * 10.0; // переводим в мм
        }

        WoodRect res = findOptimalBeam(limitF);
        if (res == null) {
            buf += "<b>Для заданных условий деревянную балку подобрать невозможно - прогиб слишком большой !</b><br>";
        } else {
            // Part 4
            buf += "<b>Возьмем несущую балку сечением " + res.name + "</b>, тогда вес одного погонного метра балки будет равен:<br>";
            buf += res.beamWeightStr + "<br>";
            buf += "Нагрузка на 1 погонный метр балки при ширине зоны сбора нагрузки b=" + str(woodBeam.getB()) + " мм будет равна:<br>";
            buf += "g<sub>н</sub>=" + str(load, 1) + "*" + str(woodBeam.getB() / 1000) + "+" + str(res.beamWeight) + "=" + str(res.gn, 0) + " кг/п.м<br><br>";

            // Part 5
            buf += "Определяем изгибающий момент балки<br>";
            buf += "M=(g<sub>н</sub>*l<sup>2</sup>)/8,<br>";
            buf += "где<br>";
            buf += "&nbsp; l - длина пролета однопролетной балки, м<br>";
            buf += "&nbsp; g<sub>н</sub> - нагрузка на балку, кгс/м<br>";
            buf += "М=" + str(res.gn) + "*" + str(woodBeam.getL()/1000.0) + "<sup>2</sup>/8=" + str(res.M, 0) + " кг*м<br><br>";

            // Part 6
            buf += "Проверяем сечение балки на прочность по расчетным нагрузкам.<br>";
            buf += "W=M/R<sub>д</sub>,<br>";
            buf += "где<br>";
            buf += "&nbsp; R<sub>д</sub>=130 кгс/м<sup>2</sup> - расчетное сопротивление на изгиб для древесины (ель, сосна)<br><br>";
            buf += "Изгибающий момент М в кг*м, а момент сопротивлений W имеет размерность см<sup>3</sup>, поэтому перед его определением ";
            buf += "нужно привести размерность изгибающего момента к кг*см, то есть умножить на 100.<br>";
            buf += "W=" + str(res.M) + "*100/130=<b>" + str(res.W_calc, 0) + " см<sup>3</sup></b><br>";
            buf += "По справочнику, для балки " + res.name + ", W=" + str(res.W) + " см<sup>3</sup><br><br>";

            // Part 7
            buf += "<b>Определяем прогиб балки</b><br>";
            buf += "f=5/(384)+g<sub>н</sub>*l<sup>4</sup>/(E*J),<br>";
            buf += "где<br>";
            buf += "&nbsp; g<sub>н</sub> - равномерно распределенная нормативная нагрузка на балку, кгс/м<br>";
            buf += "&nbsp; l - длина балки, см<br>";
            buf += "&nbsp; E - модуль упругости материала балки, кгс/м<sup>2</sup> (для древесины 100000 кгс/м<sup>2</sup>)<br>";
            buf += "&nbsp; J - момент инерции балки, см<sup>4</sup> (для балки " + res.name + " равен " + str(res.J) + ")<br><br>";
            buf += "Перед расчетом приводим размерности в соответствие<br>";
            buf += "&nbsp; g<sub>н</sub>=" + str(res.gn) + " кг/п.м = " + str(res.gn_cm) + " кгс/см<br>";
            buf += "&nbsp; l=" + str(woodBeam.getL()/1000.0) + " м = " + str(l_cm) + " см<br>";
            buf += "f=(5/384) * (" + str(res.gn_cm) + "*" + str(l_cm) + "<sup>4</sup>) / (100000*" + str(res.J) + ") = <b>" + str(res.f, 0) + " мм</b><br>";
            buf += "Запас по прогибу: " + str(100- res.f*100.0/limitF, 0) + " %<br>";
        }

        // Part 8 Выводим расчет по всем балкам
        if (woodBeam.isCalcAll()) {
            buf += "<br>Расчет по всем типоразмерам:";
            double w = 0;
            for (WoodRect wr : woodRects) {
                if (wr.w != w) buf += "<br>";
                w = wr.w;

                buf += wr.name + " (W=" + str(wr.W) + "см<sup>3</sup>" + ", J=" + str(wr.J) + "см<sup>4</sup>" + "): ";
                buf += "M=" + str(wr.M) + " кг*м, " + "W=" + str(wr.W_calc) + " см<sup>3</sup>";
                buf += ", f=" + (wr.f < limitF ? "<b>" : "") + str(wr.f, 0) + (wr.f < limitF ? "</b>" : "") + " мм<br>";
            }
        }

        return buf;
    }

    // Находим балку, выдерживающую указанное ограничение по прогибу в мм
    private static WoodRect findOptimalBeam(double limitF) {
        // сначала ищем среди типовых размеров
        for (WoodRect wr : woodRects) {
            if (wr.isStandard() && wr.f < limitF) return wr;
        }

        // потом среди остальных
        for (WoodRect wr : woodRects) {
            if (wr.f < limitF) return wr;
        }

        // возвращаем null, если ни одна балка не проходит
        return null;
    }

    private static String str(double val) {
        return StringUtil.toString(val, 3);
    }

    private static String str(double val, int dec) {
        return StringUtil.toString(val, dec);
    }
}
