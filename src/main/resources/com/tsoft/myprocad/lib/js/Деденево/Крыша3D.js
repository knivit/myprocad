output.clear();

output.print("*** Расчет крыши ***\n");

var plan = project.getActivePlan();

/* Материалы */
$$Бетон = project.addMaterial("Бетон", 2400, 4200);
$$Кирпич = project.addMaterial("Кирпич", 2400, 4200);
$$ДвутаврN20 = project.addMaterial("Двутавр №20", 7500, 3000/(1000/21));
$$Доска200х50 = project.addMaterial("Доска 200х50", 550, 6500);
$$Брус100х100 = project.addMaterial("Брус 100х100", 550, 6500);
$$Брус150х150 = project.addMaterial("Брус 150х150", 550, 6500);

/* Кирпичная облицовка */
plan.setDefaultMaterial($$Кирпич).setDefaultPattern("brick");
plan.addWall(-20, -20, 6000, 10620, 100, 7000);
plan.addWall(-20, 100, 6000, 100, 13500, 7000);
plan.addWall(-20, 13500, 6000, 10620, 13620, 7000);
plan.addWall(10500, -20, 6000, 10620, 13500, 7000);

/* Армопояс */
plan.setDefaultMaterial($$Бетон).setDefaultPattern("hatchUp");
plan.addWall(200, 200, 6800, 10400, 600, 7000);
plan.addWall(200, 600, 6800, 600, 13000, 7000);
plan.addWall(200, 13000, 6800, 10400, 13400, 7000);
plan.addWall(10000, 600, 6800, 10400, 13000, 7000);
plan.addWall(5700, 600, 6800, 6100, 13000, 7000);

/* Балки перекрытия, шаг 1 м */
plan.setDefaultMaterial($$ДвутаврN20).setDefaultPattern("background");
var Балки_перекрытия = [
  plan.addBeam(300, 800, 7100, 10300, 800, 7100, 100, 200),
  plan.addBeam(300, 1800, 7100, 10300, 1800, 7100, 100, 200),
  plan.addBeam(300, 2800, 7100, 10300, 2800, 7100, 100, 200), // 2
  plan.addBeam(300, 3800, 7100, 10300, 3800, 7100, 100, 200),
  plan.addBeam(300, 4800, 7100, 10300, 4800, 7100, 100, 200),
  plan.addBeam(300, 5800, 7100, 10300, 5800, 7100, 100, 200),
  plan.addBeam(300, 6800, 7100, 10300, 6800, 7100, 100, 200),
  plan.addBeam(300, 7800, 7100, 10300, 7800, 7100, 100, 200),
  plan.addBeam(300, 8800, 7100, 10300, 8800, 7100, 100, 200),
  plan.addBeam(300, 9800, 7100, 10300, 9800, 7100, 100, 200),
  plan.addBeam(300, 10800, 7100, 10300, 10800, 7100, 100, 200), // 10
  plan.addBeam(300, 11800, 7100, 10300, 11800, 7100, 100, 200),
  plan.addBeam(300, 12800, 7100, 10300, 12800, 7100, 100, 200)
];

Вес_ферм$кг = 0;
plan.setDefaultPattern("background").setDefaultBackgroundColor(255, 200, 0).setDefaultKeColor(255, 200, 0);
for (var i = 3; i <= 9; i ++) {
  var y = Балки_перекрытия[i].getYStart();
  var Стропило_ab1_1 = plan.addBeam(0, y-75, 7200, 5300, y-75, 10200, 50, 200).setMaterial($$Доска200х50);
  var Стропило_ab1_2 = plan.addBeam(0, y+75, 7200, 5300, y+75, 10200, 50, 200).setMaterial($$Доска200х50);
  var Стропило_ab2_1 = plan.addBeam(5300, y-75, 10200, 10600, y-75, 7200, 50, 200).setMaterial($$Доска200х50);
  var Стропило_ab2_2 = plan.addBeam(5300, y+75, 10200, 10600, y+75, 7200, 50, 200).setMaterial($$Доска200х50);

  var len = Балки_перекрытия[i].getLength();
  var Стойка_bh1 = plan.addConnectBeam(Балки_перекрытия[i], 3200, Стропило_ab1_1, 100, 100).setMaterial($$Брус100х100);
  var Стойка_bh2 = plan.addConnectBeam(Балки_перекрытия[i], len-3200, Стропило_ab2_1, 100, 100).setMaterial($$Брус100х100);

  Длина_стойки_bh$м = Стойка_bh1.getLength();
  Длина_стропила_ab$м = Стропило_ab1_1.getLength();
  Вес_ферм$кг += Стойка_bh1.getWeight() + Стойка_bh2.getWeight() +
              Стропило_ab1_1.getWeight() + Стропило_ab1_2.getWeight() +
              Стропило_ab2_1.getWeight() + Стропило_ab2_2.getWeight();
}

/* Ребра вальм со стойками */
plan.setDefaultMaterial($$ДвутаврN20).setDefaultMaterial($$Доска200х50).setDefaultKeColor(255, 100, 30);
var Стропило_xc = plan.addBeam(0, 0, 7200, 5300, Балки_перекрытия[3].getYStart(), 10200, 50, 200);
var Стропило_yc = plan.addBeam(5300, Балки_перекрытия[3].getYStart(), 10200, 10600, 0, 7200, 50, 200);
var Стропило_fv = plan.addBeam(0, 13500, 7200, 5300, Балки_перекрытия[9].getYStart(), 10200, 50, 200);
var Стропило_ve = plan.addBeam(5300, Балки_перекрытия[9].getYStart(), 10200, 10600, 13500, 7200, 50, 200);

plan.setDefaultBackgroundColor(255, 200, 60).setDefaultKeColor(255, 200, 60);
var Стойка_xc1 = plan.addCrossBeam(Стропило_xc, Балки_перекрытия[0], 100, 100).setMaterial($$Брус100х100);
var Стойка_xc2 = plan.addCrossBeam(Стропило_xc, Балки_перекрытия[1], 100, 100).setMaterial($$Брус100х100);
var Стойка_xc3 = plan.addCrossBeam(Стропило_xc, Балки_перекрытия[2], 100, 100).setMaterial($$Брус100х100);
var Стойка_yc1 = plan.addCrossBeam(Стропило_yc, Балки_перекрытия[0], 100, 100).setMaterial($$Брус100х100);
var Стойка_yc2 = plan.addCrossBeam(Стропило_yc, Балки_перекрытия[1], 100, 100).setMaterial($$Брус100х100);
var Стойка_yc3 = plan.addCrossBeam(Стропило_yc, Балки_перекрытия[2], 100, 100).setMaterial($$Брус100х100);
var Стойка_fv1 = plan.addCrossBeam(Стропило_fv, Балки_перекрытия[10], 100, 100).setMaterial($$Брус100х100);
var Стойка_fv2 = plan.addCrossBeam(Стропило_fv, Балки_перекрытия[11], 100, 100).setMaterial($$Брус100х100);
var Стойка_fv3 = plan.addCrossBeam(Стропило_fv, Балки_перекрытия[12], 100, 100).setMaterial($$Брус100х100);
var Стойка_ve1 = plan.addCrossBeam(Стропило_ve, Балки_перекрытия[10], 100, 100).setMaterial($$Брус100х100);
var Стойка_ve2 = plan.addCrossBeam(Стропило_ve, Балки_перекрытия[11], 100, 100).setMaterial($$Брус100х100);
var Стойка_ve3 = plan.addCrossBeam(Стропило_ve, Балки_перекрытия[12], 100, 100).setMaterial($$Брус100х100);

/* Стойки конька */
//var Стойка_1 = plan.addConnectBeam(5900, xa, 7200, 5900, xa, cz+7200, 150, 150).setMaterial($$Брус150х150);
//var Стойка_2 = plan.addConnectBeam(5900, 6800, 7200, 5900, 6800, cz+7200, 150, 150).setMaterial($$Брус150х150);
//var Стойка_3 = plan.addConnectBeam(5900, xf-xa, 7200, 5900, xf-xa, cz+7200, 150, 150).setMaterial($$Брус150х150);

var Труба = plan.addWall(6170, 10300, 6000, 6870, 10600, 11000).setPattern("brick").setKeColor(255, 80, 80);

//plan.addLight("ambient").setCenter(5000, 6000, 10000);
//plan.addLight("directional").setCenter(5000, 6000, 10000).setDirection(1, 1, 1);

project.saveToFile("Модель_крыши.mpc");
