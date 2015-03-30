package com.tsoft.myprocad.util.script;

import com.tsoft.myprocad.AbstractItemTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JavaScriptTest extends AbstractItemTest {
    class ConsoleOutputBinding extends OutputBinding {
        ConsoleOutputBinding(JavaScript js) { super(js); }

        public void print(String text) {
            System.out.println(text);
        }
    }

    public void testBinding() throws Exception {
        JavaScript js = new JavaScript();
        js.addBinding(new PlanBinding(plan));
        js.execute("plan.addWall(0, 0, 1000, 1000)", new ConsoleOutputBinding(js));
    }

    public void testLib() throws Exception {
        JavaScript js = new JavaScript();
        js.loadLibrary("lib/js/СопротивлениеМатериалов.js");
        js.loadLibrary("lib/js/ЖелезнаяБалка.js");

        js.execute(
            "p_Постоянная_Нагрузка_Кг_На_М = 500;" +
            "l_Длина_Балки_См = 600;" +
            "Рассчитать_Mmax_Максимальный_Сгибающий_Момент_Балки_Кг_См_С_Равномерной_Нагрузкой(p_Постоянная_Нагрузка_Кг_На_М, l_Длина_Балки_См); " +
            "output.print(Mmax_Максимальный_Сгибающий_Момент_Кг_См_Расчет);", new ConsoleOutputBinding(js));
        assertEquals(234038.0, js.getVariable("Mmax_Максимальный_Сгибающий_Момент_Кг_См"));

        js.execute(
            "Рассчитать_W_МоментСопротивления_См3_Железной_Балки(225000); " +
            "output.print(W_МоментСопротивления_См3_Расчет);", new ConsoleOutputBinding(js));
        assertEquals(225.0, js.getVariable("W_МоментСопротивления_См3"));

        js.execute(
            "W_МоментСопротивления_См3 = 225;" +
            "Найти_Балку_Двутавровую(W_МоментСопротивления_См3);", new ConsoleOutputBinding(js));
        assertEquals("22", js.getVariable("Балка.Номер"));
    }

    //@Test
    public void calcPorch() throws Exception {
        JavaScript js = new JavaScript();
        js.loadLibrary("lib/js/БиблиотекаФункций.js");
        js.loadLibrary("lib/js/СопротивлениеМатериалов.js");
        js.loadLibrary("lib/js/ЖелезнаяБалка.js");
        js.loadLibrary("lib/js/Железобетон.js");
        js.executeScript("lib/js/Деденево/РасчетКрыльца.js", new ConsoleOutputBinding(js));
    }

    //@Test
    public void calcWalls() throws Exception {
        JavaScript js = new JavaScript();
        js.loadLibrary("lib/js/БиблиотекаФункций.js");
        js.loadLibrary("lib/js/Теплотехника.js");
        js.executeScript("lib/js/Деденево/РасчетСтен.js", new ConsoleOutputBinding(js));
    }

    @Test
    public void calcRoof() throws Exception {
        JavaScript js = new JavaScript();
        js.addBinding(new ProjectBinding(plan.getProject(), plan));
        js.addBinding(new ObjBinding());
        js.loadLibrary("lib/js/БиблиотекаФункций.js");
        js.loadLibrary("lib/js/СопротивлениеМатериалов.js");
        js.loadLibrary("lib/js/Дерево.js");
        js.executeScript("lib/js/Деденево/РасчетКрыши.js", new ConsoleOutputBinding(js));
    }
}
