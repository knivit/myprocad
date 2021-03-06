Console.clear();

Console.print("*** Расчет мансарды ***\n");

$$1 = "\nСтропильная система\n";
Мауэрлат$шт = (1 + 1 + 0.5) + (1 + 1 + 0.5);
Мауэрлат_ширина$м = 0.2;
Мауэрлат_высота$м = 0.1;

Брус$шт = (1 + 1 + 1) + (1 + 1 + 1) + (1 + 1 + 1);
Брус_ширина$м = 0.15;
Брус_высота$м = 0.15;

Стропила$шт = (25 + 25) + (25 + 25);
Стропила_ширина$м = 0.05;
Стропила_высота$м = 0.15;

Лаги$шт = 25 + 7;
Лаги_ширина$м = 0.05;
Лаги_высота$м = 0.15;

Контр_обрешетка$шт = (25 + 25) + (25 + 25);
Контр_обрешетка_ширина$м = 0.05;
Контр_обрешетка_высота$м = 0.05;

Обрешетка$шт = (40 + 40 + 20) + (40 + 40 + 20);
Обрешетка$шт_ширина$м = 0.1;
Обрешетка$шт_высота$м = 0.025;

Итого_200х100$шт = Мауэрлат$шт;
Итого_150х150$шт = Брус$шт;
Итого_150х50$шт = Стропила$шт + Лаги$шт;
Итого_50х50$шт = Контр_обрешетка$шт;
Итого_100х25$шт = Обрешетка$шт;

Итого_дерево$м3 = (Итого_150х100$шт * 6 * 0.15 * 0.1) + (Итого_150х150$шт * 6 * 0.15 * 0.15) +
  (Итого_150х50$шт * 6 * 0.15 * 0.05) + (Итого_50х50$шт * 6 * 0.05 * 0.05) +
  (Итого_100х25$шт * 6 * 0.1 * 0.025);
Цена_дерево$руб_за_м3 = 7200;
Итого_стоимость_дерево$руб = Итого_дерево$м3 * Цена_дерево$руб_за_м3;


$$2 = "\nКладка стен и облицовки\n";
Объем_кладки_периметр$м3 = (13.2 * 0.4 * 1) * 2 + (10.2 * 0.4 * 1) * 2;
Объем_кладки_фронтонов$м3 = (5.4 * 0.4 * 2) * 2 + ((2.4 * 2) / 2 * 0.4) * 4;
Объем_кладки_средней_стены$м3 = (12.4 - 1.2 - 0.9) * 0.4 * 3;
Объем_оконных_проемов$м3 = (1.2 * 0.4 * 2) * 2 + (0.8 * 0.4 * 2) * 2;
Объем_кладки_столбов$м3 = (0.4 * 0.4 * 3) * 4 + (0.6 * 0.4 * 1.6) * 2;
Итого_объем_кладки$м3 = Объем_кладки_периметр$м3 + Объем_кладки_фронтонов$м3 +
  Объем_кладки_средней_стены$м3 + Объем_кладки_столбов$м3 - Объем_оконных_проемов$м3;
Стоимость_кладки$руб = Итого_объем_кладки$м3 * 1700;

Площадь_облицовки_периметр$м2 = (14 * 0.8) * 2 + (11 * 0.8) * 2;
Площадь_облицовки_фронтонов$м2 = (5.4 * 2) * 2 + ((2.6 * 2) / 2) * 4;
Площадь_облицовки_столбов$м2 = (0.6 * 2) * 2;
Итого_площадь_облицовки$м2 = Площадь_облицовки_периметр$м2 + Площадь_облицовки_фронтонов$м2 +
  Площадь_облицовки_столбов$м2;
Стоимость_облицовки$руб = Итого_площадь_облицовки$м2 * 1400;

// Печать
Console.printVariables();
Console.print("*** Конец расчета мансарды ***");
