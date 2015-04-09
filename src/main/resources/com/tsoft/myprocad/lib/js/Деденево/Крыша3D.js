output.clear();

output.print("*** Расчет крыши ***\n");

var plan = project.getActivePlan();

/* Материалы */
$$Бетон = project.addMaterial("Бетон", 2.400, 4200);
$$Кирпич = project.addMaterial("Кирпич", 2.400, 4200);
$$ДвутаврN20 = project.addMaterial("Двутавр №20", 1.050, 60000);
$$Доска200х50 = project.addMaterial("Доска 200х50", 0.550, 6500);
$$Брус100х100 = project.addMaterial("Брус 100х100", 0.550, 6500);
$$Брус150х150 = project.addMaterial("Брус 150х150", 0.550, 6500);

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
  plan.addBeam(300, 2800, 7100, 10300, 2800, 7100, 100, 200),
  plan.addBeam(300, 3800, 7100, 10300, 3800, 7100, 100, 200),
  plan.addBeam(300, 4800, 7100, 10300, 4800, 7100, 100, 200),
  plan.addBeam(300, 5800, 7100, 10300, 5800, 7100, 100, 200),
  plan.addBeam(300, 6800, 7100, 10300, 6800, 7100, 100, 200),
  plan.addBeam(300, 7800, 7100, 10300, 7800, 7100, 100, 200),
  plan.addBeam(300, 8800, 7100, 10300, 8800, 7100, 100, 200),
  plan.addBeam(300, 9800, 7100, 10300, 9800, 7100, 100, 200),
  plan.addBeam(300, 10800, 7100, 10300, 10800, 7100, 100, 200),
  plan.addBeam(300, 11800, 7100, 10300, 11800, 7100, 100, 200),
  plan.addBeam(300, 12800, 7100, 10300, 12800, 7100, 100, 200)
];
Вес_балок_перекрытия$тн = 0;
Цена_балок_перекрытия$руб = 0;
for (var i = 0; i < 12; i ++) {
  Вес_балок_перекрытия$тн += Балки_перекрытия[i].getWeight();
  Цена_балок_перекрытия$руб += Балки_перекрытия[i].getPrice();
}

Вес_ферм$тн = 0;
Объем_ферм$м3 = 0;
Цена_ферм$руб = 0;
plan.setDefaultPattern("background").setDefaultBackgroundColor(255, 200, 0).setDefaultKeColor(255, 200, 0);
for (var i = 3; i <= 9; i ++) {
  var y = Балки_перекрытия[i].getYStart();
  var Стропило_левое_1 = plan.addBeam(0, y-75, 7200, 5300, y-75, 10200, 50, 200).setMaterial($$Доска200х50);
  var Стропило_левое_2 = plan.addBeam(0, y+75, 7200, 5300, y+75, 10200, 50, 200).setMaterial($$Доска200х50);
  var Стропило_правое_1 = plan.addBeam(5300, y-75, 10200, 10600, y-75, 7200, 50, 200).setMaterial($$Доска200х50);
  var Стропило_правое_2 = plan.addBeam(5300, y+75, 10200, 10600, y+75, 7200, 50, 200).setMaterial($$Доска200х50);

  var len = Балки_перекрытия[i].getLength();
  var Стойка_левая = plan.addConnectBeam(Балки_перекрытия[i], 3200, Стропило_левое_1, 100, 100).setMaterial($$Брус100х100);
  var Стойка_правая = plan.addConnectBeam(Балки_перекрытия[i], len-3200, Стропило_правое_1, 100, 100).setMaterial($$Брус100х100);

  Длина_стойки_фермы$м = Стойка_левая.getLength();
  Длина_стропила$м = Стропило_левое_1.getLength();
  Вес_ферм$тн += Стойка_левая.getWeight() + Стойка_правая.getWeight() +
    Стропило_левое_1.getWeight() + Стропило_левое_2.getWeight() +
    Стропило_правое_1.getWeight() + Стропило_правое_2.getWeight();
  Объем_ферм$м3 += Стойка_левая.getVolume() + Стойка_правая.getVolume() +
    Стропило_левое_1.getVolume() + Стропило_левое_2.getVolume() +
    Стропило_правое_1.getVolume() + Стропило_правое_2.getVolume();
  Цена_ферм$руб += Стойка_левая.getPrice() + Стойка_правая.getPrice() +
    Стропило_левое_1.getPrice() + Стропило_левое_2.getPrice() +
    Стропило_правое_1.getPrice() + Стропило_правое_2.getPrice();
}

/* Ребра вальм со стойками и стропилами */
plan.setDefaultMaterial($$ДвутаврN20).setDefaultMaterial($$Брус100х100).setDefaultKeColor(255, 100, 30);
var Ребро_вальмы_сз = plan.addBeam(0, 0, 7200, 5300, Балки_перекрытия[3].getYStart(), 10200, 100, 100);
var Ребро_вальмы_св = plan.addBeam(5300, Балки_перекрытия[3].getYStart(), 10200, 10600, 0, 7200, 100, 100);
var Ребро_вальмы_юз = plan.addBeam(0, 13500, 7200, 5300, Балки_перекрытия[9].getYStart(), 10200, 100, 100);
var Ребро_вальмы_юв = plan.addBeam(5300, Балки_перекрытия[9].getYStart(), 10200, 10600, 13500, 7200, 100, 100);

plan.setDefaultBackgroundColor(255, 200, 60).setDefaultMaterial($$Брус100х100).setDefaultKeColor(255, 200, 60);
var Стойки_вальм = [
  // СЗ
  plan.addCrossBeam(Ребро_вальмы_сз, Балки_перекрытия[0], 100, 100),
  plan.addCrossBeam(Ребро_вальмы_сз, Балки_перекрытия[1], 100, 100),
  plan.addCrossBeam(Ребро_вальмы_сз, Балки_перекрытия[2], 100, 100),

  // СВ
  plan.addCrossBeam(Ребро_вальмы_св, Балки_перекрытия[0], 100, 100),
  plan.addCrossBeam(Ребро_вальмы_св, Балки_перекрытия[1], 100, 100),
  plan.addCrossBeam(Ребро_вальмы_св, Балки_перекрытия[2], 100, 100),

  // ЮЗ
  plan.addCrossBeam(Ребро_вальмы_юз, Балки_перекрытия[10], 100, 100),
  plan.addCrossBeam(Ребро_вальмы_юз, Балки_перекрытия[11], 100, 100),
  plan.addCrossBeam(Ребро_вальмы_юз, Балки_перекрытия[12], 100, 100),

  // ЮВ
  plan.addCrossBeam(Ребро_вальмы_юв, Балки_перекрытия[10], 100, 100),
  plan.addCrossBeam(Ребро_вальмы_юв, Балки_перекрытия[11], 100, 100),
  plan.addCrossBeam(Ребро_вальмы_юв, Балки_перекрытия[12], 100, 100)
];

plan.setDefaultPattern("background").setDefaultBackgroundColor(255, 200, 0).setDefaultKeColor(255, 200, 0);
var Стропила_вальм = [
  // СЗ
  // горизонтальные
  plan.addBeam(0, Балки_перекрытия[0].getYStart()-75, 7200, Стойки_вальм[0].getXStart(), Балки_перекрытия[0].getYStart()-75, Стойки_вальм[0].getZStart(), 50, 200),
  plan.addBeam(0, Балки_перекрытия[0].getYStart()+75, 7200, Стойки_вальм[0].getXStart(), Балки_перекрытия[0].getYStart()+75, Стойки_вальм[0].getZStart(), 50, 200),
  plan.addBeam(0, Балки_перекрытия[1].getYStart()-75, 7200, Стойки_вальм[1].getXStart(), Балки_перекрытия[1].getYStart()-75, Стойки_вальм[1].getZStart(), 50, 200),
  plan.addBeam(0, Балки_перекрытия[1].getYStart()+75, 7200, Стойки_вальм[1].getXStart(), Балки_перекрытия[1].getYStart()+75, Стойки_вальм[1].getZStart(), 50, 200),
  plan.addBeam(0, Балки_перекрытия[2].getYStart()-75, 7200, Стойки_вальм[2].getXStart(), Балки_перекрытия[2].getYStart()-75, Стойки_вальм[2].getZStart(), 50, 200),
  plan.addBeam(0, Балки_перекрытия[2].getYStart()+75, 7200, Стойки_вальм[2].getXStart(), Балки_перекрытия[2].getYStart()+75, Стойки_вальм[2].getZStart(), 50, 200),
  // вертикальные
  plan.addConnectXBeam(1000-75, 0, 7200, Ребро_вальмы_сз, 50, 200),
  plan.addConnectXBeam(1000+75, 0, 7200, Ребро_вальмы_сз, 50, 200),
  plan.addConnectXBeam(2000-75, 0, 7200, Ребро_вальмы_сз, 50, 200),
  plan.addConnectXBeam(2000+75, 0, 7200, Ребро_вальмы_сз, 50, 200),
  plan.addConnectXBeam(3000-75, 0, 7200, Ребро_вальмы_сз, 50, 200),
  plan.addConnectXBeam(3000+75, 0, 7200, Ребро_вальмы_сз, 50, 200),
  plan.addConnectXBeam(4000-75, 0, 7200, Ребро_вальмы_сз, 50, 200),
  plan.addConnectXBeam(4000+75, 0, 7200, Ребро_вальмы_сз, 50, 200),
  plan.addConnectXBeam(5000-75, 0, 7200, Ребро_вальмы_сз, 50, 200),
  plan.addConnectXBeam(5000+75, 0, 7200, Ребро_вальмы_сз, 50, 200),

  // СВ
  // горизонтальные
  plan.addBeam(Стойки_вальм[3].getXStart(), Балки_перекрытия[0].getYStart()-75, Стойки_вальм[3].getZStart(), 10600, Балки_перекрытия[0].getYStart()-75, 7200, 50, 200),
  plan.addBeam(Стойки_вальм[3].getXStart(), Балки_перекрытия[0].getYStart()+75, Стойки_вальм[3].getZStart(), 10600, Балки_перекрытия[0].getYStart()+75, 7200, 50, 200),
  plan.addBeam(Стойки_вальм[4].getXStart(), Балки_перекрытия[1].getYStart()-75, Стойки_вальм[4].getZStart(), 10600, Балки_перекрытия[1].getYStart()-75, 7200, 50, 200),
  plan.addBeam(Стойки_вальм[4].getXStart(), Балки_перекрытия[1].getYStart()+75, Стойки_вальм[4].getZStart(), 10600, Балки_перекрытия[1].getYStart()+75, 7200, 50, 200),
  plan.addBeam(Стойки_вальм[5].getXStart(), Балки_перекрытия[2].getYStart()-75, Стойки_вальм[5].getZStart(), 10600, Балки_перекрытия[2].getYStart()-75, 7200, 50, 200),
  plan.addBeam(Стойки_вальм[5].getXStart(), Балки_перекрытия[2].getYStart()+75, Стойки_вальм[5].getZStart(), 10600, Балки_перекрытия[2].getYStart()+75, 7200, 50, 200),
  // вертикальные
  plan.addConnectXBeam(6000-75, 0, 7200, Ребро_вальмы_св, 50, 200),
  plan.addConnectXBeam(6000+75, 0, 7200, Ребро_вальмы_св, 50, 200),
  plan.addConnectXBeam(7000-75, 0, 7200, Ребро_вальмы_св, 50, 200),
  plan.addConnectXBeam(7000+75, 0, 7200, Ребро_вальмы_св, 50, 200),
  plan.addConnectXBeam(8000-75, 0, 7200, Ребро_вальмы_св, 50, 200),
  plan.addConnectXBeam(8000+75, 0, 7200, Ребро_вальмы_св, 50, 200),
  plan.addConnectXBeam(9000-75, 0, 7200, Ребро_вальмы_св, 50, 200),
  plan.addConnectXBeam(9000+75, 0, 7200, Ребро_вальмы_св, 50, 200),
  plan.addConnectXBeam(10000-75, 0, 7200, Ребро_вальмы_св, 50, 200),
  plan.addConnectXBeam(10000+75, 0, 7200, Ребро_вальмы_св, 50, 200),

  // ЮЗ
  plan.addBeam(0, Балки_перекрытия[10].getYStart()-75, 7200, Стойки_вальм[6].getXStart(), Балки_перекрытия[10].getYStart()-75, Стойки_вальм[6].getZStart(), 50, 200),
  plan.addBeam(0, Балки_перекрытия[10].getYStart()+75, 7200, Стойки_вальм[6].getXStart(), Балки_перекрытия[10].getYStart()+75, Стойки_вальм[6].getZStart(), 50, 200),
  plan.addBeam(0, Балки_перекрытия[11].getYStart()-75, 7200, Стойки_вальм[7].getXStart(), Балки_перекрытия[11].getYStart()-75, Стойки_вальм[7].getZStart(), 50, 200),
  plan.addBeam(0, Балки_перекрытия[11].getYStart()+75, 7200, Стойки_вальм[7].getXStart(), Балки_перекрытия[11].getYStart()+75, Стойки_вальм[7].getZStart(), 50, 200),
  plan.addBeam(0, Балки_перекрытия[12].getYStart()-75, 7200, Стойки_вальм[8].getXStart(), Балки_перекрытия[12].getYStart()-75, Стойки_вальм[8].getZStart(), 50, 200),
  plan.addBeam(0, Балки_перекрытия[12].getYStart()+75, 7200, Стойки_вальм[8].getXStart(), Балки_перекрытия[12].getYStart()+75, Стойки_вальм[8].getZStart(), 50, 200),

  // ЮВ
  plan.addBeam(Стойки_вальм[9].getXStart(), Балки_перекрытия[10].getYStart()-75, Стойки_вальм[9].getZStart(), 10600, Балки_перекрытия[10].getYStart()-75, 7200, 50, 200),
  plan.addBeam(Стойки_вальм[9].getXStart(), Балки_перекрытия[10].getYStart()+75, Стойки_вальм[9].getZStart(), 10600, Балки_перекрытия[10].getYStart()+75, 7200, 50, 200),
  plan.addBeam(Стойки_вальм[10].getXStart(), Балки_перекрытия[11].getYStart()-75, Стойки_вальм[10].getZStart(), 10600, Балки_перекрытия[11].getYStart()-75, 7200, 50, 200),
  plan.addBeam(Стойки_вальм[10].getXStart(), Балки_перекрытия[11].getYStart()+75, Стойки_вальм[10].getZStart(), 10600, Балки_перекрытия[11].getYStart()+75, 7200, 50, 200),
  plan.addBeam(Стойки_вальм[11].getXStart(), Балки_перекрытия[12].getYStart()-75, Стойки_вальм[11].getZStart(), 10600, Балки_перекрытия[12].getYStart()-75, 7200, 50, 200),
  plan.addBeam(Стойки_вальм[11].getXStart(), Балки_перекрытия[12].getYStart()+75, Стойки_вальм[11].getZStart(), 10600, Балки_перекрытия[12].getYStart()+75, 7200, 50, 200)
];

Вес_вальм$тн = Ребро_вальмы_сз.getWeight() + Ребро_вальмы_св.getWeight() + Ребро_вальмы_юз.getWeight() + Ребро_вальмы_юв.getWeight();
Объем_вальм$м3 = Ребро_вальмы_сз.getVolume() + Ребро_вальмы_св.getVolume() + Ребро_вальмы_юз.getVolume() + Ребро_вальмы_юв.getVolume();
Цена_вальм$руб = Ребро_вальмы_сз.getPrice() + Ребро_вальмы_св.getPrice() + Ребро_вальмы_юз.getPrice() + Ребро_вальмы_юв.getPrice();
for (var i = 0; i < Стойки_вальм.length; i ++) {
  Вес_вальм$тн += Стойки_вальм[i].getWeight();
  Объем_вальм$м3 += Стойки_вальм[i].getVolume();
  Цена_вальм$руб += Стойки_вальм[i].getPrice();
}
for (var i = 0; i < Стропила_вальм.length; i ++) {
  Вес_вальм$тн += Стропила_вальм[i].getWeight();
  Объем_вальм$м3 += Стропила_вальм[i].getVolume();
  Цена_вальм$руб += Стропила_вальм[i].getPrice();
}

/* Стойки конька */
plan.setDefaultMaterial($$Брус150х150).setDefaultKeColor(255, 200, 60);
var Стойки_конька = [
  plan.addConnectBeam(Балки_перекрытия[3], 5000, Ребро_вальмы_сз, 150, 150),
  plan.addConnectBeam(Балки_перекрытия[9], 5000, Ребро_вальмы_юз, 150, 150)
];
Вес_стоек_конька$тн = 0;
Объем_стоек_конька$м3 = 0;
Цена_стоек_конька$руб = 0;
for (var i = 0; i < Стойки_конька.length; i ++) {
  Вес_стоек_конька$тн += Стойки_конька[i].getWeight();
  Объем_стоек_конька$м3 += Стойки_конька[i].getVolume();
  Цена_стоек_конька$руб += Стойки_конька[i].getPrice();
}

$$1 = "\nИтого:";
Итого_вес_металла$тн = toStr(Вес_балок_перекрытия$тн, 3);
Итого_цена_металла$руб = toStr(Цена_балок_перекрытия$руб, 0);
Итого_вес_дерева$тн = toStr(Вес_ферм$тн + Вес_вальм$тн + Вес_стоек_конька$тн, 3);
Итого_объем_дерева$м3 = toStr(Объем_ферм$м3 + Объем_вальм$м3 + Объем_стоек_конька$м3, 2);
Итого_цена_дерева$руб = toStr(Цена_ферм$руб + Цена_вальм$руб + Цена_стоек_конька$руб, 0);

var Труба = plan.addWall(6170, 10300, 6000, 6870, 10600, 11000).setPattern("brick").setKeColor(255, 80, 80);

//plan.addLight("ambient").setCenter(5000, 6000, 10000);
//plan.addLight("directional").setCenter(5000, 6000, 10000).setDirection(1, 1, 1);

project.saveToFile("Модель_крыши.mpc");

output.printVariables();
output.print("*** Конец расчета крыши ***");
