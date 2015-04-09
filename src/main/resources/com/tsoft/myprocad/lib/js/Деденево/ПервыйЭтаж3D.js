output.clear();

output.print("*** Расчет 1 этажа ***\n");

var plan = project.getActivePlan();

/* Материалы */
$$Бетон = project.addMaterial("Бетон", 2.400, 4200);
$$Кирпич = project.addMaterial("Кирпич", 2.400, 4200);
$$Дверь = project.addMaterial("ДВерь", 0.050, 6000);

plan;
Облицовка_слева = plan.addWall(-20, -20, 0, 100, 13620, 3200).setMaterial($$Кирпич).setPattern("brick").setBackgroundColor(255, 40, 0).setKeColor(255, 40, 0);
Дверь_слева = plan.addWall(-50, 2000, 260, 400, 3000, 2460).setMaterial($$Дверь).setPattern("background").setBackgroundColor(200, 200, 150).setKeColor(200, 200, 150);

project.saveToFile("Модель_1_этажа.mpc");

output.printVariables();
output.print("*** Конец расчета 1 этажа ***");
