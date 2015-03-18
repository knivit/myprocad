/**
 * Расчет стропильной системы
 * "Курс лекций" В.Г.Залеского, стр. 415
*/

output.clear();

output.print("*** Расчет крыши ***\n");

output.print("                                  Q");
output.print("                                  |    c");
output.print("                     Q/(2*sin a)  V   /+\\--------> Q/(2*cos a)");
output.print("                          /        /   |   \\");
output.print("                      |/_       /      | Q/2  \\");
output.print("                             /       g |         \\");
output.print(" H=(Q*cos a)/(2*sin a)  b /+-----------+-----------+\\ d");
output.print("    <------------      /   |                       |   \\");
output.print("            P     k /      |                       |      \\");
output.print("            |    /   \\     | (P+Q)/2               |    /     \\");
output.print("            V /        \\   |                       |  /          \\");
output.print("         a /             \\ | h                     |/               \\ e");
output.print("         +-----------------+---------- z ----------+----------------+");
output.print("         |                 |                       |");
output.print("         | P/2             +                       +");
output.print("         V");
output.print("                                  Рис. 1 Ферма\n");

output.print("       x +---------------------------- j ---------------------------+ y");
output.print("         |  \\                                                  /   |");
output.print("       s |         \\+ m               +                 +/         |");
output.print("       t |                 \\+ d                  +/                 |");
output.print("         |                        \\   c   /                         |");
output.print("       a +------------------ b --------+---------+------------------+ e");
output.print("         |                   |         |         |                  |");
output.print("         +-------------------+-------------------+------------------+");
output.print("         |                   |         |         |                  |");
output.print("         +-------------------+-------------------+------------------+");
output.print("         |                   |         |         |                  |");
output.print("         +-------------------+-------------------+------------------+");
output.print("         |                   |         |         |                  |");
output.print("         +-------------------+-------------------+------------------+");
output.print("         |                   |         |         |                  |");
output.print("         +-------------------+-------------------+------------------+");
output.print("         |                   |         |         |                  |");
output.print("         +-------------------+-------------------+------------------+");
output.print("         |                   |         |         |                  |");
output.print("         +-------------------+-------------------+------------------+");
output.print("         |                   |         |         |                  |");
output.print("         +-------------------+---------+---------+------------------+");
output.print("         |                        /    v    \\                       |");
output.print("         |                 /+                      +\\               |");
output.print("         |         /+                  +                   +\\       |");
output.print("         |  /                                                    \\  |");
output.print("       f +----------------------------------------------------------+");
output.print("                               Рис. 2 Вид сверху\n");

output.print("                                  |    c");
output.print("                     Q/(2*sin a)  V   /+");
output.print("                          /        /   |");
output.print("                      |/_       /      |");
output.print("                             /         |");
output.print(" H=(Q*cos a)/(2*sin a)    /b +         |");
output.print("    <------------      /     |         |");
output.print("            P       /        |         |");
output.print("            |    /           |(P+Q)/2  |");
output.print("            V / + m          |         |");
output.print("         x /    | n        h |         |");
output.print("         +------+------------+-------- z");
output.print("         |              |               ");
output.print("         | P/2          +               ");
output.print("         V");
output.print("           Рис. 3 Ребро вальмы\n");

$$1 = "Исходные данные\n";
Длина_крыши_xf$см = 1300;
Высота_конька_cz$см = 300;
Расстояние_до_стойки_ah$см = 320;
Ширина_пролета_ae$см = 1000;
Угол_подкоса_khb$градусов = 40;
Ширина_вальмы_xa$см = 300;

$$2$0 = "\n------------------------------------------";
$$2 = "Расчет геометрии";
$$2$1 = "------------------------------------------";

$$3 = "\nВычисляем размеры фермы\n";
Половина_ширины_пролета_az$см = Ширина_пролета_ae$см / 2;
Длина_стропил_ac$см = Math.ceil(Math.sqrt(Высота_конька_cz$см*Высота_конька_cz$см + Половина_ширины_пролета_az$см*Половина_ширины_пролета_az$см));
Расстояние_между_стойками_bd$см = Ширина_пролета_ae$см - 2 * Расстояние_до_стойки_ah$см;
Высота_нижней_стойки_bh$см = Math.ceil(Высота_конька_cz$см * Расстояние_до_стойки_ah$см / Половина_ширины_пролета_az$см);
Длина_части_стропил_ab$см = Math.ceil(Math.sqrt(Расстояние_до_стойки_ah$см*Расстояние_до_стойки_ah$см + Высота_нижней_стойки_bh$см*Высота_нижней_стойки_bh$см));
Длина_части_стропил_bc$см = Длина_стропил_ac$см - Длина_части_стропил_ab$см;
Половина_ширины_пролета_между_стойками_bg$см = Половина_ширины_пролета_az$см - Расстояние_до_стойки_ah$см;
Высота_верхней_стойки_до_конька_cg$см = Высота_конька_cz$см - Высота_нижней_стойки_bh$см;
Длина_подкоса_kh$см = Math.ceil(Высота_нижней_стойки_bh$см * Math.cos(toRadians(Угол_подкоса_khb$градусов)));
Длина_стропил_от_подкоса_до_нижней_стойки_kb$см =  Math.ceil(Высота_нижней_стойки_bh$см * Math.sin(toRadians(Угол_подкоса_khb$градусов)));
Длина_стропил_ниже_подкоса_ak$см = Длина_части_стропил_ab$см - Длина_стропил_от_подкоса_до_нижней_стойки_kb$см;

$$4 = "\nУгол наклона стропил"
sin_alfa = Высота_конька_cz$см / Длина_стропил_ac$см;
cos_alfa = Половина_ширины_пролета_az$см / Длина_стропил_ac$см;
alfa_Угол_наклона_стропил_градусов = Math.ceil(toDegrees(Math.asin(sin_alfa)));

$$5 = "\nВычисляем вальму";
Длина_конька_cv$см = Длина_крыши_xf$см - 2*Ширина_вальмы_xa$см;
Проекция_ребра_вальмы_xc$см = Math.sqrt(Ширина_вальмы_xa$см*Ширина_вальмы_xa$см + Половина_ширины_пролета_az$см*Половина_ширины_пролета_az$см);
Длина_ребра_вальмы_xc$см = Math.sqrt(Проекция_ребра_вальмы_xc$см*Проекция_ребра_вальмы_xc$см + Высота_конька_cz$см*Высота_конька_cz$см);
Длина_фронтона_вальмы_jc$см = Math.sqrt(Высота_конька_cz$см*Высота_конька_cz$см + Ширина_вальмы_xa$см*Ширина_вальмы_xa$см);
sin_alfa_ребра_вальмы = Высота_конька_cz$см / Длина_ребра_вальмы_xc$см;
alfa_Угол_наклона_ребра_вальмы_градусов = Math.ceil(toDegrees(Math.asin(sin_alfa_ребра_вальмы)));
sin_alfa_фронтона_вальмы = Высота_конька_cz$см / Длина_фронтона_вальмы_jc$см;
alfa_Угол_наклона_фронтона_вальмы_градусов = Math.ceil(toDegrees(Math.asin(sin_alfa_фронтона_вальмы)));

$$6 = "\nКровля";
Площадь_xac$м2 = ((Ширина_вальмы_xa$см * Длина_стропил_ac$см) / 2) / (100 * 100);
Площадь_длинного_ската_вальмы_xcvf$м2 = 2*Площадь_xac$м2 + (Длина_стропил_ac$см*Длина_конька_cv$см) / (100 * 100);
Площадь_половины_фронтона_вальмы_xcj$м2 = ((Длина_фронтона_вальмы_jc$см * Длина_ребра_вальмы_xc$см) / 2) / (100 * 100);
Площадь_фронтона_вальмы_xcy$м2 = 2*Площадь_половины_фронтона_вальмы_xcj$м2;
Площадь_кровли$м2 = Math.ceil(2*Площадь_длинного_ската_вальмы_xcvf$м2 + 2*Площадь_фронтона_вальмы_xcy$м2);

$$7$0 = "\n------------------------------------------"
$$7 = "Собираем нагрузки";
$$7$1 = "------------------------------------------"

$$8 = "\nИсходные данные";
Плотность_дерева$кг_на_м3 = 550;
Плотность_гипсокартона$кг_на_м3 = 850;
Вес_металлочерепицы$кг_на_м2 = 5;
Вес_утеплителя$кг_на_м2 = 13.5 / 1.8; // Утеплитель RockWool Венти Батис Оптима 1000x600x100
Толщина_влагостойкого_гипсокартона$м = 0.0125;
Вес_подшивки$кг_на_м2 = Плотность_гипсокартона$кг_на_м3 * Толщина_влагостойкого_гипсокартона$м;

Шаг_фермы$м = 0.6;
Количество_ферм$шт = Math.ceil((Длина_конька_cv$см/100) / Шаг_фермы$м) + 1;

$$9 = "\nОбрешетка (расстояние 10 см между решетинами, т.е. 5 решетин на метр)";
Обрешетка_ширина_доска_100x25$м = 0.10;
Обрешетка_толщина_доска_100x25$м = 0.025;
Обрешетка_шаг$м = 0.15;
Вес_обрешетки$кг_на_м2 = (100/(10+10)) * (1*0.100*0.025)*Плотность_дерева$кг_на_м3;

$$10 = "\nСтропильная система";
Вес_части_стропил_ab$кг = (Длина_части_стропил_ab$см / 100) * (2 * 0.200*0.050*Плотность_дерева$кг_на_м3);
Вес_части_стропил_bc$кг = (Длина_части_стропил_bc$см / 100) * (1 * 0.200*0.050*Плотность_дерева$кг_на_м3);
Вес_распорки_между_стойками_bd$кг = (Расстояние_между_стойками_bd$см / 100) * (1 * 0.200*0.050*Плотность_дерева$кг_на_м3);
Вес_верхней_стойки_до_конька_cg$кг = (Высота_верхней_стойки_до_конька_cg$см / 100) * (1 * 0.200*0.050*Плотность_дерева$кг_на_м3);
Вес_нижней_стойки_bh$кг = (Высота_нижней_стойки_bh$см / 100) * (1 * 0.200*0.050*Плотность_дерева$кг_на_м3);
Вес_подкоса_kh$кг = (Длина_подкоса_kh$см / 100) * (1 * 0.200*0.050*Плотность_дерева$кг_на_м3);
Вес_фермы$кг = 2*Вес_части_стропил_ab$кг + 2*Вес_части_стропил_bc$кг + Вес_распорки_между_стойками_bd$кг +
    2*Вес_нижней_стойки_bh$кг + 2*Вес_подкоса_kh$кг + Вес_верхней_стойки_до_конька_cg$кг;
Вес_стропильной_системы$кг = Количество_ферм$шт * Вес_фермы$кг;

$$11 = "\nСнежная нагрузка";
Вес_снежного_покрова_3_район$кг_на_м2 = 150;

$$12 = "\nВетровая нагрузка";
W0_Расчетное_значение_ветрового_давления_1_район$кг_на_м2 = 32;
z_Высота_крыши_над_землей$м = 10;
k_Коэффициент_изменения_ветрового_давления_для_высоты_10м_тип_местности_Б = 0.65;
c_Аэродинамический_коэффициент_для_уклона_30_градусов = 0.4;
Ветровая_нагрузка$кг_на_м2 = W0_Расчетное_значение_ветрового_давления_1_район$кг_на_м2 *
    z_Высота_крыши_над_землей$м * k_Коэффициент_изменения_ветрового_давления_для_высоты_10м_тип_местности_Б *
    c_Аэродинамический_коэффициент_для_уклона_30_градусов;
$$CALC =
    "Ветровая нагрузка W = W0 * k * z * c = " + toStr(Ветровая_нагрузка$кг_на_м2, 1) + " кг/м2, где\n" +
    "  W0 - расчетное значение ветрового давления, 1 район = " + W0_Расчетное_значение_ветрового_давления_1_район$кг_на_м2 + " кг/м2\n" +
    "  k - коэффициент изменения ветрового давления для высоты 10м, тип местности Б = " + k_Коэффициент_изменения_ветрового_давления_для_высоты_10м_тип_местности_Б + "\n" +
    "  z - высота крыши над землей = " + z_Высота_крыши_над_землей$м + " м\n" +
    "  c - аэродинамический коэффициент для уклона 30 градусов = " + c_Аэродинамический_коэффициент_для_уклона_30_градусов + "\n";

$$13 = "\nИтого:\n";
Постоянная_нагрузка$кг_на_м2 = Math.ceil(Вес_металлочерепицы$кг_на_м2 + Вес_обрешетки$кг_на_м2 + Вес_утеплителя$кг_на_м2 + Вес_подшивки$кг_на_м2);
Временная_нагрузка$кг_на_м2 = Math.ceil(Вес_снежного_покрова_3_район$кг_на_м2 + Ветровая_нагрузка$кг_на_м2);
Всего_вес_кровли_при_наилучших_условиях$кг = (Постоянная_нагрузка$кг_на_м2) * Площадь_кровли$м2;
Всего_вес_кровли_при_наихудших_условиях$кг = (Постоянная_нагрузка$кг_на_м2 + Временная_нагрузка$кг_на_м2) * Площадь_кровли$м2;
Всего_вес_крыши_при_наилучших_условиях$кг = Всего_вес_кровли_при_наилучших_условиях$кг + Вес_стропильной_системы$кг;
Всего_вес_крыши_при_наихудших_условиях$кг = Всего_вес_кровли_при_наихудших_условиях$кг + Вес_стропильной_системы$кг;

$$14 = "\nВычисляем силы P и Q как пропорции от общей нагрузки на стропило к длинам отрезков ab и bc";
Нагрузка_P$кг = Math.ceil((Постоянная_нагрузка$кг_на_м2 + Временная_нагрузка$кг_на_м2) * (Длина_части_стропил_ab$см / 100) * Шаг_фермы$м);
Нагрузка_Q$кг = Math.ceil((Постоянная_нагрузка$кг_на_м2 + Временная_нагрузка$кг_на_м2) * (Длина_части_стропил_bc$см / 100) * Шаг_фермы$м);
Вертикальная_сила_a$кг = Нагрузка_P$кг / 2;
Наклонный_распор_a$кг = Math.ceil(Нагрузка_P$кг / 2 * sin_alfa);
Горизонтальное_растяжение_ah$кг = Math.ceil(Нагрузка_P$кг / 2 * cos_alfa / sin_alfa);
Вертикальная_сила_b$кг = Math.ceil(Нагрузка_P$кг / 2 + Нагрузка_Q$кг / 2);
Наклонный_распор_b$кг = Math.ceil(Нагрузка_Q$кг / 2 * sin_alfa);
Горизонтальное_растяжение_bd$кг = Math.ceil(Нагрузка_Q$кг / 2 * cos_alfa / sin_alfa - Нагрузка_P$кг / 2 * cos_alfa);
Вертикальная_сила_c$кг = Нагрузка_Q$кг / 2;
Наклонный_распор_c$кг = Math.ceil(Нагрузка_Q$кг / 2 * sin_alfa);
Горизонтальный_распор_c$кг = Math.ceil(Нагрузка_Q$кг / 2 * cos_alfa);

$$15 = "\nСопротивление материалов";
Максимальный_сгибающий_момент_ab$кг_на_см = Рассчитать_Mmax_Максимальный_Сгибающий_Момент_Балки_Кг_См_С_Равномерной_Нагрузкой(Нагрузка_P$кг, Расстояние_до_стойки_ah$см);
Максимальный_сгибающий_момент_bc$кг_на_см = Рассчитать_Mmax_Максимальный_Сгибающий_Момент_Балки_Кг_См_С_Равномерной_Нагрузкой(Нагрузка_Q$кг, Половина_ширины_пролета_между_стойками_bg$см);

$$16 = "\nПодбираем балки";
Момент_сопротивления_ab$см3 = Рассчитать_W_МоментСопротивления_См3_Деревянной_Балки(Максимальный_сгибающий_момент_ab$кг_на_см);
Момент_сопротивления_bc$см3 = Рассчитать_W_МоментСопротивления_См3_Деревянной_Балки(Максимальный_сгибающий_момент_bc$кг_на_см);
Bab = Найти_Деревянную_Балку(Момент_сопротивления_ab$см3);
Bbc = Найти_Деревянную_Балку(Момент_сопротивления_bc$см3);
Стропила_ab = Описание_Балки(Bab);
Стропила_bc = Описание_Балки(Bbc);

Стрела_прогиба_стропил_ab$мм = (Bab == null) ? "Нет данных" : Рассчитать_f_Стрела_Прогиба_См_Деревянной_Балки(Постоянная_нагрузка$кг_на_м2, Расстояние_до_стойки_ah$см, Bab.J) * 10;
Стрела_прогиба_стропил_bc$мм = (Bbc == null) ? "Нет данных" : Рассчитать_f_Стрела_Прогиба_См_Деревянной_Балки(Постоянная_нагрузка$кг_на_м2, Половина_ширины_пролета_между_стойками_bg$см, Bbc.J) * 10;

// 3d
var vertexes = [
    [ 0, 0, 0 ], [ 0, Длина_крыши_xf$см, 0 ], [ Ширина_пролета_ae$см, 0, 0 ], [ Ширина_пролета_ae$см, Длина_крыши_xf$см, 0 ],
    [ Половина_ширины_пролета_az$см, Ширина_вальмы_xa$см, Высота_конька_cz$см ],
    [ Половина_ширины_пролета_az$см, Длина_крыши_xf$см - Ширина_вальмы_xa$см, Высота_конька_cz$см ],
    [ 40, 40, -760 ],
    [ 40, 40, 0 ],
    [ 40, Длина_крыши_xf$см-40, -760 ],
    [ 40, Длина_крыши_xf$см-40, 0 ],
    [ Ширина_пролета_ae$см-40, 40, -760 ],
    [ Ширина_пролета_ae$см-40, 40, 0 ],
    [ Ширина_пролета_ae$см-40, Длина_крыши_xf$см-40, -760 ],
    [ Ширина_пролета_ae$см-40, Длина_крыши_xf$см-40, 0 ]
];

var faces = [
    [ 1, 4, 3 ],
    [ 1, 2, 4 ],
    [ 1, 5, 3 ],
    [ 1, 2, 6 ],
    [ 1, 6, 5 ],
    [ 5, 6, 4 ],
    [ 5, 4, 3 ],
    [ 2, 6, 4 ],

    [ 7, 13, 11 ],
    [ 7, 9, 13 ],
    [ 7, 10, 9 ],
    [ 7, 8, 10 ],
    [ 9, 14, 14 ],
    [ 9, 10, 14 ],
    [ 11, 13, 14 ],
    [ 11, 14, 12 ],
    [ 7, 11, 12 ],
    [ 7, 12, 8 ],
    [ 8, 12, 14 ],
    [ 8, 14, 10 ]
];

Файл_3D_модели_крыши = obj.generate("Модель_крыши.obj", vertexes, faces);

// Печать
output.printVariables();
output.print("*** Конец расчета крыши ***");
