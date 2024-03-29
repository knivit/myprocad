/**
  * Основано на Курсе лекций Залесского В.Г.
  *
*/

/** Стр. 353 */
function Рассчитать_Толщину_Железобетонной_Плиты_См(l_Длина_Пролета_См, p_Постоянная_Нагрузка_Кг_На_М) {
    var Длина_Пролете_М = l_Длина_Пролета_См / 100;
    var d_Толщина_Плиты = Math.ceil(Длина_Пролете_М / 11 * Math.sqrt(p_Постоянная_Нагрузка_Кг_На_М));
    var F_Площадь_Арматуры_См2 = (9 * d_Толщина_Плиты) / 10;
    var n_Число_Прутьев_d10мм = Math.ceil(F_Площадь_Арматуры_См2 / 0.79);
    var e_Расстояние_Между_Прутьями_См = Math.ceil(100 / n_Число_Прутьев_d10мм);
    $$CALC =
        "Толщина железобетонной плиты d = (l / 11) * sqrt(P) = " + d_Толщина_Плиты + " см\n" +
        "где\n" +
        "  l - длина пролета, " + l_Длина_Пролета_См + " см\n" +
        "  p - постоянная нагрузка вместе с собственным весом, " + p_Постоянная_Нагрузка_Кг_На_М + " кг/м2\n" +
        "Тогда площадь всей арматуры на 100 см покрытия F = (9 * d) / 10 = " + F_Площадь_Арматуры_См2 + " см2.\n" +
        "Принимая толщину арматуры y = 10 мм, площадь сечения f = 3.14 * y*y / 4 = 0.79 см2,\n" +
        "число прутьев n = F / f = " + n_Число_Прутьев_d10мм + " шт.\n" +
        "Расстояние между прутьями e = 100 см / n = " + e_Расстояние_Между_Прутьями_См + " см.\n";
    return d_Толщина_Плиты;
}

function Рассчитать_W_МоментСопротивления_См3_Железобетонной_Балки(Mmax_Максимальный_Сгибающий_Момент_Кг_См) {
    // Для бетона состава 1:3
    var R_ПрочноеСопротивлениеБетона_Кг_на_См2 = 30;
    var W = Рассчитать_W_МоментСопротивления_См3(Mmax_Максимальный_Сгибающий_Момент_Кг_См, R_ПрочноеСопротивлениеБетона_Кг_на_См2);
    return W;
}
