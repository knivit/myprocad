Console.clear();

Console.print("*** Расчет крыльца ***");

$$1 = "\nИсходные данные\n";
Длина_крыльца$см = 200;
Ширина_крыльца$см = 120;
Толщина_крыльца$см = 20;
Площадь_крыльца$м2 = (Длина_крыльца$см * Ширина_крыльца$см) / (100 * 100);
Объем_крыльца$м3 = Площадь_крыльца$м2 * (Толщина_крыльца$см / 100);
Удельный_вес_крыльца$кг_на_м3 = 1600;
Вес_крыльца$кг = Удельный_вес_крыльца$кг_на_м3 * Объем_крыльца$м3;

Количество_двутавровых_балок$шт = 3;
Временная_нагрузка_на_конце$кг = 400;

$$2 = "\nРасчет\n";
Ширина_зоны_нагрузки_на_балку$см = Длина_крыльца$см / (Количество_двутавровых_балок$шт - 1);
Объем_зоны_нагрузки_на_балку$м3 = (Ширина_зоны_нагрузки_на_балку$см * Ширина_крыльца$см * Толщина_крыльца$см) / (100 * 100 * 100);
Постоянная_нагрузка_на_балку$кг = Удельный_вес_крыльца$кг_на_м3 * Объем_зоны_нагрузки_на_балку$м3;

Максимальный_сгибающий_момент_консоли_Mmax$кг_см = Рассчитать_Mmax_Максимальный_Сгибающий_Момент_Консоли_Кг_См(Постоянная_нагрузка_на_балку$кг, Ширина_крыльца$см, Временная_нагрузка_на_конце$кг);
$$3 = $$CALC;

Момент_сопротивления_железной_балки_W$см3 = Рассчитать_W_МоментСопротивления_См3_Железной_Балки(Максимальный_сгибающий_момент_консоли_Mmax$кг_см);
$$4 = $$CALC;

Балка = Найти_Балку_Двутавровую(Момент_сопротивления_железной_балки_W$см3);
$$5 = "Результат: Балка №" + Балка.Номер;

Ширина_опоры_балки$см = 60;
Расстояние_от_края_до_точки_вращения_балки$см = 10;
Давление_балки_вверх$кг = Рассчитать_S_Давление_На_Подкладку_В_Консоли_Кг(Максимальный_сгибающий_момент_консоли_Mmax$кг_см, Ширина_опоры_балки$см);
$$6 = $$CALC;

Полная_нагрузка$кг = Постоянная_нагрузка_на_балку$кг + Временная_нагрузка_на_конце$кг;
Давление_балки_вниз$кг = Рассчитать_T_Давление_На_Подкладку_A_Консоли_Кг(Максимальный_сгибающий_момент_консоли_Mmax$кг_см, Ширина_опоры_балки$см, Полная_нагрузка$кг);
$$7 = $$CALC;

Минимальный_давящий_вес_на_конец_балки_должен_быть$кг = 2 * Давление_балки_вверх$кг;

Толщина_железобетонной_плиты$см = Рассчитать_Толщину_Железобетонной_Плиты_См(Длина_крыльца$см, Вес_крыльца$кг + Временная_нагрузка_на_конце$кг);
$$8 = $$CALC;

Console.printVariables();
Console.print("\n*** Конец расчета крыльца ***");
