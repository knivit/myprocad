/**
 * Двутавровые прокатные железные балки
 *
 * Расчет по Залесский В.Г. "Курс лекций", стр. 300
*/

/**
  * Сортамент прокатной стали
  * http://www.dpva.info/netcat_files/File/FilesForLoading/Matherials/SOPROMAT/BeamsToGost/SortamentSteels.pdf
*/

/**
  * Балки двутавровые по ГОСТ 8239-72 (http://www.toehelp.ru/theory/stroi_meh/sortament.pdf)
  * h - высота балки, мм
  * b - ширина полки, мм
  * d - толщина стенки, мм
  * t - средняя толщина полки, мм
  * A - площадь сечения, см2
  * J - момент инерции, см4
  * W - момент сопротивления, см3
  * i - радиус инерции, см
  * S - статический момент полусечения, см3
  * m - масса 1 м, кг
*/
var Сортамент_Балки_Двутавровые = [
  { Номер:"10",  h:100, b:55,  d:4.5, t:7.2,  A:12.0, Jx:198,  Wx:39.7,  ix:"", Sx:"", Jy:"", Wy:"", iy:"", m:9.46 },
  { Номер:"12",  h:120, b:64,  d:4.8, t:7.3,  A:14.7, Jx:350,  Wx:58.4,  ix:"", Sx:"", Jy:"", Wy:"", iy:"", m:11.5 },
  { Номер:"14",  h:140, b:73,  d:4.9, t:7.5,  A:17.4, Jx:572,  Wx:81.7,  ix:"", Sx:"", Jy:"", Wy:"", iy:"", m:13.7 },
  { Номер:"16",  h:160, b:81,  d:5.0, t:7.8,  A:20.2, Jx:873,  Wx:109.0, ix:"", Sx:"", Jy:"", Wy:"", iy:"", m:15.9 },
  { Номер:"18",  h:180, b:90,  d:5.1, t:8.1,  A:23.4, Jx:1290, Wx:143.0, ix:"", Sx:"", Jy:"", Wy:"", iy:"", m:18.4 },
  { Номер:"18a", h:180, b:100, d:5.1, t:8.3,  A:25.4, Jx:1430, Wx:159.0, ix:"", Sx:"", Jy:"", Wy:"", iy:"", m:19.9 },
  { Номер:"20",  h:200, b:100, d:5.2, t:8.4,  A:26.8, Jx:1840, Wx:184.0, ix:"", Sx:"", Jy:"", Wy:"", iy:"", m:21.0 },
  { Номер:"20a", h:200, b:110, d:5.2, t:8.4,  A:26.8, Jx:2030, Wx:203.0, ix:"", Sx:"", Jy:"", Wy:"", iy:"", m:22.7 },
  { Номер:"22",  h:220, b:110, d:5.4, t:8.7,  A:30.6, Jx:2550, Wx:232.0, ix:"", Sx:"", Jy:"", Wy:"", iy:"", m:24.0 },
  { Номер:"22a", h:220, b:120, d:5.4, t:8.9,  A:32.8, Jx:2790, Wx:254.0, ix:"", Sx:"", Jy:"", Wy:"", iy:"", m:25.8 },
  { Номер:"24",  h:240, b:115, d:5.6, t:9.5,  A:34.8, Jx:3460, Wx:289.0, ix:"", Sx:"", Jy:"", Wy:"", iy:"", m:27.3 },
  { Номер:"24a", h:240, b:125, d:5.6, t:9.8,  A:37.5, Jx:3800, Wx:317.0, ix:"", Sx:"", Jy:"", Wy:"", iy:"", m:29.4 },
  { Номер:"27",  h:270, b:125, d:6.0, t:9.8,  A:40.2, Jx:5010, Wx:371.0, ix:"", Sx:"", Jy:"", Wy:"", iy:"", m:31.5 },
  { Номер:"27a", h:270, b:135, d:6.0, t:10.2, A:43.2, Jx:5500, Wx:371.0, ix:"", Sx:"", Jy:"", Wy:"", iy:"", m:33.9 }
]

/**
  * Швеллеры по ГОСТ 8240-72 (http://www.lador.ru/files/gosty/gost-8240-89.pdf)
  * h - высота швеллера, мм
  * b - ширина полки, мм
  * d - толщина стенки, мм
  * t - средняя толщина полки, мм
  * A - площадь сечения, см2
  * J - момент инерции, см4
  * W - момент сопротивления, см3
  * i - радиус инерции, см
  * S - статический момент полусечения, см3
  * z0 - расстояние от оси y до наружней грани стенки, см
  * m - масса 1 м, кг
*/
var Сортамент_Швеллеры = [
  { Номер:"5",   h:50,  b:32,  d:4.4, t:7.0,  A:6.16, Jx:22.8,  Wx:9.1,  ix:"", Sx:"", Jy:"", Wy:"", iy:"", z0:"", m:4.84 },
  { Номер:"6,5", h:65,  b:36,  d:4.4, t:7.2,  A:7.51, Jx:48.6,  Wx:15.0, ix:"", Sx:"", Jy:"", Wy:"", iy:"", z0:"", m:5.9 },
  { Номер:"8",   h:80,  b:40,  d:4.5, t:7.4,  A:8.98, Jx:89.4,  Wx:22.4, ix:"", Sx:"", Jy:"", Wy:"", iy:"", z0:"", m:7.05 },
  { Номер:"10",  h:100, b:46,  d:4.5, t:7.6,  A:10.9, Jx:174,   Wx:34.8, ix:"", Sx:"", Jy:"", Wy:"", iy:"", z0:"", m:8.59 },
  { Номер:"12",  h:120, b:52,  d:4.8, t:7.8,  A:13.3, Jx:304,   Wx:50.6, ix:"", Sx:"", Jy:"", Wy:"", iy:"", z0:"", m:10.4 },
  { Номер:"14",  h:140, b:58,  d:4.9, t:8.1,  A:15.6, Jx:491,   Wx:70.2, ix:"", Sx:"", Jy:"", Wy:"", iy:"", z0:"", m:12.3 },
  { Номер:"14a", h:140, b:62,  d:4.9, t:8.7,  A:17.0, Jx:545,   Wx:77.8, ix:"", Sx:"", Jy:"", Wy:"", iy:"", z0:"", m:13.3 },
  { Номер:"16",  h:160, b:64,  d:5.0, t:8.4,  A:18.1, Jx:747,   Wx:93.4, ix:"", Sx:"", Jy:"", Wy:"", iy:"", z0:"", m:14.2 },
  { Номер:"16a", h:160, b:68,  d:5.0, t:9.0,  A:19.5, Jx:823,   Wx:103,  ix:"", Sx:"", Jy:"", Wy:"", iy:"", z0:"", m:15.3 },
  { Номер:"18",  h:180, b:70,  d:5.1, t:8.7,  A:20.7, Jx:1090,  Wx:121,  ix:"", Sx:"", Jy:"", Wy:"", iy:"", z0:"", m:16.3 },
  { Номер:"18a", h:180, b:74,  d:5.1, t:9.3,  A:22.2, Jx:1190,  Wx:132,  ix:"", Sx:"", Jy:"", Wy:"", iy:"", z0:"", m:17.4 },
  { Номер:"20",  h:200, b:76,  d:5.2, t:9.0,  A:23.4, Jx:1520,  Wx:152,  ix:"", Sx:"", Jy:"", Wy:"", iy:"", z0:"", m:18.4 },
  { Номер:"20a", h:200, b:80,  d:5.2, t:9.7,  A:25.2, Jx:1670,  Wx:167,  ix:"", Sx:"", Jy:"", Wy:"", iy:"", z0:"", m:19.8 }
]

function Рассчитать_W_МоментСопротивления_См3_Железной_Балки(Mmax_Максимальный_Сгибающий_Момент_Кг_См) {
    var R_ПрочноеСопротивлениеЖелеза_Кг_на_См2 = 1000;
    var W = Рассчитать_W_МоментСопротивления_См3(Mmax_Максимальный_Сгибающий_Момент_Кг_См, R_ПрочноеСопротивлениеЖелеза_Кг_на_См2);
    return W;
}

function Рассчитать_f_Стрела_Прогиба_См_Железной_Балки(p_Постоянная_Нагрузка_Кг_На_М, l_ДлинаБалки_См, J_МоментИнерции_См4) {
    var E_Коэффициент_Упругости_Железа_Кг_На_См2 = 2000000;
    var f = Рассчитать_f_Стрела_Прогиба_См(p_Постоянная_Нагрузка_Кг_На_М, l_ДлинаБалки_См, J_МоментИнерции_См4, E_Коэффициент_Упругости_Железа_Кг_На_См2);
    return f;
}

function Найти_Балку_Двутавровую(W_МоментСопротивления_См3) {
    for (var i = 0; i < Сортамент_Балки_Двутавровые.length; i ++) {
        var Балка = Сортамент_Балки_Двутавровые[i];
        if (Балка.Wx > W_МоментСопротивления_См3) {
            return Балка;
        }
    }
    return null;
}

function Проверить_Зависимость_W_МоментаСопротивления_См3_От_J_МоментаИнерции_См4() {
    output.print(
        "Момент сопротивленния W = J / a (см3)\n" +
        "где\n" +
        "  J - момент инерции, см4\n" +
        "  a - расстояние от центра тяжести до наиболее растянутых или сжатых волокон, см\n" +
        "В симметричном сечении балок a = h / 2, где h - высота балки, см.\n" +
        "Проверим это на таблице сортамента двутавровых балок:\n");

    output.print("| Балка № | Wx по сортаменту | Wx по расчету |\n");
    for (i = 0; i < Сортамент_Балки_Двутавровые.length; i ++) {
        var Балка = Сортамент_Балки_Двутавровые[i];
        var a = (Балка.h / 10) / 2;
        var W = Балка.Jx / a;
        output.print("| %8s | %16.2f | %13.2f |\n", Балка.Номер, Балка.Wx, W);
    }
}
