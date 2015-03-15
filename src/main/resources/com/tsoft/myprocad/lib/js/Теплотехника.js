/**
  * Теплотехнические расчеты
  *
*/

/*
 * Савельев А.А. Конструкции крыш. Стропильные системы. 2009 г.
 * стр. 22
*/
function Нормированное_сопротивление_покрытий_для_Москвы$м2_С_на_Вт() {
    return 4.7;
}

function Рассчитать_толщину_утеплителя$см(Ky) {
    var R = Нормированное_сопротивление_покрытий_для_Москвы$м2_С_на_Вт;
    var T = Math.ceil(R * Ky * 100);

    $$CALC =
        "Требуемая толщина утеплителя T = R * Ky = " + R + " * " + Ky + " = " + T + " см\n" +
        "где\n" +
        "  R - нормированное сопротивление покрытий, для Москвы " + R + " м2*С/Вт\n" +
        "  Ky - теплопроводность утеплителя, " + Ky + " Вт/(м*С)\n";
    return T;
}

function Рассчитать_тепловое_сопротивление$м2_С_на_Вт(T, Ky) {
    var R = (T / 100) / Ky;

    $$CALC =
        "Тепловое сопротивление R = (T / 100) / Ky = (" + T + " / 100)" + " / " + Ky + " = " + R + " м2*С/Вт\n" +
         "где\n" +
         "  T - толщина материала, " + T + " см\n" +
         "  Ky - теплопроводность материала, " + Ky + " Вт/(м*С)\n";
    return R;
}