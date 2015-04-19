output.clear();

output.print("*** Стены 3D ***\n");

var plan = project.getActivePlan();

/* Материалы */
$$Бетон = project.addMaterial("Бетон", 2.400, 4200);
$$Кирпич = project.addMaterial("Кирпич", 2.400, 4200);
$$Дверь = project.addMaterial("ДВерь", 0.050, 6000);

// 1 этаж
plan.setDefaultMaterial($$Кирпич).setDefaultPattern("brick").setDefaultBackgroundColor(255, 40, 0).setDefaultKeColor(255, 40, 0);
Облицовка_слева = plan.addWallWithAperturesYZ(-20, -20, 0, 100, 13620, 3200,
  2000, 260, 3000, 2460, // дверь
  5100, 1225, 6300, 2825, // три окна
  6900, 1225, 8100, 2825,
  10500, 1225, 11700, 2825);
Облицовка_верх = plan.addWallWithAperturesXZ(100, -20, 0, 10500, 100, 3200,
  3800, 1225, 5000, 2825); // окно
Облицовка_справа = plan.addWallWithAperturesYZ(10500, -20, 0, 10620, 13620, 3200,
  2000, 260, 3000, 2460, // дверь
  4600, 1225, 5800, 2825, // два окна
  6400, 1225, 7600, 2825);
Облицовка_низ = plan.addWallWithAperturesXZ(100, 13500, 0, 10500, 13620, 3200,
  1600, 1225, 2800, 2825, // два окна
  3400, 1225, 4600, 2825);

// 2 этаж
Облицовка_слева_2 = plan.addWallWithAperturesYZ(-20, -20, 3200, 100, 13620, 6400,
  2000, 4437, 3000, 6037, // 4 окна
  5100, 4437, 6300, 6037,
  6900, 4437, 8100, 6037,
  10500, 4437, 11700, 6037);
Облицовка_верх_2 = plan.addWallWithAperturesXZ(100, -20, 3200, 10500, 100, 6400,
  3800, 4437, 5000, 6037); // окно
Облицовка_справа_2 = plan.addWallWithAperturesYZ(10500, -20, 3200, 10620, 13620, 6400,
  2000, 4437, 3000, 6037, // 4 окна
  4600, 4437, 5800, 6037,
  6400, 4437, 7600, 6037,
  10500, 4437, 11700, 6037);
Облицовка_низ_2 = plan.addWallWithAperturesXZ(100, 13500, 3200, 10500, 13620, 6400,
  1600, 4437, 2800, 6037, // три окна
  3400, 4437, 4600, 6037,
  7800, 4437, 9000, 6037);

// Полы и внутренние стены
//plan.setDefaultMaterial($$Бетон).setDefaultPattern("hatchUp").setDefaultBackgroundColor(49, 200, 77).setDefaultKeColor(49, 200, 77);
//plan.addWall(0, 0, 0, 10600, 13600, 160);
//plan.addWall(0, 0, 3200, 10600, 13600, 3360);
//Стена_слева = plan.addWall(100, 100, 160, 500, 13500, 3200);
//Стена_верх = plan.addWall(500, 100, 160, 10100, 500, 3200);
//Стена_справа = plan.addWall(10100, 100, 160, 10500, 13500, 3200);
//Стена_низ = plan.addWallWithAperturesXZ(500, 13100, 160, 10100, 13500, 3200);

project.saveToFile("Стены3D.mpc");

output.printVariables();
output.print("*** Конец расчета Стены 3D ***");
