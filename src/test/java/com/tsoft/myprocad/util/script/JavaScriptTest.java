package com.tsoft.myprocad.util.script;

import com.tsoft.myprocad.AbstractItemTest;
import org.junit.Test;

public class JavaScriptTest extends AbstractItemTest {
     @Test
    public void calcFirstFloor() throws Exception {
        JavaScript js = new JavaScript();

        js.addBinding("Application", new ApplicationBinding(js));
        //js.executeScript("lib/js/Деденево/3D/Модель3D.js");
        //js.addBinding(new ObjBinding());
        js.executeScript("lib/js/Деденево/РасчетМансарды.js");

        js.loadLibrary("lib/js/БиблиотекаФункций.js");
        js.loadLibrary("lib/js/СопротивлениеМатериалов.js");
        js.loadLibrary("lib/js/ЖелезнаяБалка.js");
        js.loadLibrary("lib/js/Железобетон.js");
        js.loadLibrary("lib/js/Теплотехника.js");

     //   js.executeScript("lib/js/Деденево/ПерекрытиеДвутавр.js");
     //   js.executeScript("lib/js/Деденево/РасчетКрыльца.js");
     //   js.executeScript("lib/js/Деденево/РасчетСтен.js");
     //   js.executeScript("lib/js/Деденево/РасчетКрыши.js");

      //   js.executeScript("lib/js/Деденево/Крыша3D.js");
      //   js.executeScript("lib/js/Деденево/Стены3D.js");
     }
}
