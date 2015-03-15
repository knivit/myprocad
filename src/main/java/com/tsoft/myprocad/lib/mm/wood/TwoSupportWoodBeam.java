package com.tsoft.myprocad.lib.mm.wood;

/**
 * Деревянна балка на двух опорах
 *
 * Левая опора с двумя степенями свободы (поворт и скольжение по оси X)
 * Правая опора - жесткая заделка с одной степенью свободы (поворот в вертикальной плоскости)
 * У обоих опор третья степень (смещение вниз) отсутствует
 *
 *                          q (кг/м)
 *    |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |
 *    V  V  V  V  V  V  V  V  V  V  V  V  V  V  V  V  V  V
 *   ------------------------------------------------------
 *   ------------------------------------------------------
 *              o A                             o B
 *             / \                             / \
 *            -----                           -----
 *            -----                             |
 * ->    a      |<------------- L ------------->|      a    <-
 *              |                               |
 *              V Ra                            V Rb
 *                Ra = Rb = q * (a + L/2)
 *                Ma = Mb = (q * a^2)/2
 *                M пролета = q * (L^2/4 - a^2) / 2 (максимальный изгибающий момент)
 *
 * Балка свешивается на а (м) с обоих концов.
 * Если свесов нет, это частный случай с а = 0.
 * Тогда прогиб
 *     f пролета = q * L^4 * (5 - 24 * a^2/L^2) / (384 * E*I)
 *     f консоли = q * L^4 * a * (a^3/L^3 - 2 * a^2/L^2 - 1/3) / (8 * L * E*I)
 * где
 *     Е - модуль упругости древесины, для ели и сосны он составляет 100 000 кг/см^2
 *     I - момент инерции (мера инертности тела при изгибе), для прямоугольного сечения
 *         равный b * h^3/12 (b и h — ширина и высота сечения балки), см^4
 * При использовании спаренных стропильных ног, раздвинутых на толщину применяемого пиломатериала,
 * момент инерции I и момент сопротивления W просто удваиваем относительно одинарной стропилины.
 *
 * q = Q * b / 2
 *     Q - длина балки, м
 *     b - ширина воздействия
 *
 * Сечение прогона подбирается по расчету по первому и второму предельному состоянию -
 * на разрушение и на прогиб. Балка, работающая на изгиб должна отвечать следующим условиям:
 * 1. Внутреннее напряжение, возникающее в ней при изгибе от приложения внешней нагрузки,
 *    не должно превышать расчетного сопротивления древесины на изгиб:
 *    σ = М/W ≤ Rизг,   (1)
 *    где
 *      σ — внутреннее напряжение, кг/см^2;
 *      М — максимальный изгибающий момент, кг×м (кг×100см);
 *      W — момент сопротивления сечения стропильной ноги изгибу W = b*h^2/6, см^3;
 *      Rизг — расчетное сопротивление древесины изгибу, кг/см^2, для ели и сосны Rизг = 130 кг/см^2
 *
 * 2. Величина прогиба балки не должна превышать нормируемого прогиба:
 *    f = 5*q*L^4/(384*E*I) ≤ fнор,  (2)
 *    где
 *       fнор — нормируемый прогиб балки, для всех элементов крыши (стропил, прогонов и брусков обрешетки)
 *           он составляет L/200 (1/200 длины проверяемого пролета балки L), см.
 *
 * Сначала просчитываются изгибающие моменты М (кг×см).
 * Если на расчетной схеме изображено несколько моментов, то просчитываются все и выбирается наибольший.
 * Далее путем несложных математических преобразований формулы (1), получаем, что размеры сечения балки
 * можно найти, задавшись одним из его параметров. Например, произвольно задавая толщину бруса,
 * из которого будет изготовлена балка, находим ее высоту по формуле (3):
 *
 *     h = sqrt(6*W/b) ,    (3)
 * где
 *     b (см) — ширина сечения балки;
 *     W (см^3) — момент сопротивления балки изгибу,
 *        вычисляется по формуле: W = M/Rизг
 *        (где М (кг×см) — максимальный изгибающий момент,
 *        а Rизг — сопротивление древесины изгибу, для ели и сосны Rизг = 130 кг/см^2).
 *
 * После этого балку с вычисленными параметрами ширины и высоты по формуле (2) проверяют на прогиб.
 *
 * Взято с http://ostroykevse.ru/Krisha/Krisha_page_12.html
 */
public class TwoSupportWoodBeam {
}
