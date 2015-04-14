output.clear();

output.print("*** Расчет 1 этажа ***\n");

var plan = project.getActivePlan();

/* Материалы */
$$Бетон = project.addMaterial("Бетон", 2.400, 4200);
$$Кирпич = project.addMaterial("Кирпич", 2.400, 4200);
$$Дверь = project.addMaterial("ДВерь", 0.050, 6000);

plan.setDefaultMaterial($$Бетон).setDefaultPattern("hatchUp").setDefaultBackgroundColor(49, 200, 77).setDefaultKeColor(49, 200, 77);
plan.addWall(0, 0, 0, 10600, 13600, 160);
plan.addWall(0, 0, 0, 10600, 13600, 160);

plan.setDefaultMaterial($$Кирпич).setDefaultPattern("brick").setDefaultBackgroundColor(255, 40, 0).setDefaultKeColor(255, 40, 0);
Облицовка_слева = plan.addWallWithAperturesYZ(-20, -20, 0, 100, 13620, 3200,
  2000, 260, 3000, 2460, // дверь
  5100, 1225, 6300, 2825, // три окна
  7300, 1225, 8500, 2825,
  10500, 1225, 11700, 2825);
Стена_слева = plan.addWall(100, 100, 160, 500, 13500, 3200);
plan.addLight()
Облицовка_верх = plan.addWallWithAperturesXZ(100, -20, 0, 10500, 100, 3200,
  3800, 1225, 5000, 2825); // окно
Стена_верх = plan.addWall(500, 100, 160, 10100, 500, 3200);
Облицовка_справа = plan.addWallWithAperturesYZ(10500, -20, 0, 10620, 13620, 3200,
  2000, 260, 3000, 2460, // дверь
  4600, 1225, 5800, 2825, // два окна
  6600, 1225, 7800, 2825);
Стена_справа = plan.addWall(10100, 100, 160, 10500, 13500, 3200);
Облицовка_низ = plan.addWallWithAperturesXZ(100, 13500, 0, 10500, 13620, 3200,
  1600, 1225, 2800, 2825, // два окна
  3800, 1225, 5000, 2825);
Стена_низ = plan.addWallWithAperturesXZ(500, 13100, 160, 10100, 13500, 3200);

// 2 этаж
Облицовка_слева_2 = plan.addWallWithAperturesYZ(-20, -20, 3200, 100, 13620, 6400,
  1900, 4437, 3100, 6037,
  5100, 4437, 6300, 6037,
  7300, 4437, 8500, 6037,
  10500, 4437, 11700, 6037);
Облицовка_верх_2 = plan.addWallWithAperturesXZ(100, -20, 3200, 10500, 100, 6400,
  3800, 4437, 5000, 6037); // окно
Облицовка_справа_2 = plan.addWallWithAperturesYZ(10500, -20, 3200, 10620, 13620, 6400,
  1900, 4437, 3100, 6037,
  4600, 4437, 5800, 6037,
  6600, 4437, 7800, 6037,
  10500, 4437, 11700, 6037);
Облицовка_низ_2 = plan.addWallWithAperturesXZ(100, 13500, 3200, 10500, 13620, 6400,
  1600, 4437, 2800, 6037, // два окна
  3800, 4437, 5000, 6037,
  7800, 4437, 9000, 6037);

project.saveToFile("Модель_1_этажа.mpc");

output.printVariables();
output.print("*** Конец расчета 1 этажа ***");
