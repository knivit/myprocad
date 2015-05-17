Console.clear();

Console.print("*** Перекрытие двутавр ***");
project = Application.createProject();
var plan = project.addPlan("Крыша 3D");

$$1 = "Общие характеристики\n";
Пролет_1$мм = 3900;
Пролет_2$мм = 5100;

$$Бетон = project.addMaterial("Бетон", 2.400, 4200);
$$Двутавр20 = project.addMaterial("Двутавр", 1, 1);

/* Армопояс */
plan.setDefaultMaterial($$Бетон).setDefaultPattern("hatchUp").setDefaultBackgroundColor(200, 200, 200).setDefaultKeColor(200, 200, 200);
plan.addWall(200, 200, 3045, 10400, 600, 3235);
plan.addWall(200, 600, 3045, 600, 13000, 3235);
plan.addWall(200, 13000, 3045, 10400, 13400, 3235);
plan.addWall(10000, 600, 3045, 10400, 13000, 3235);
plan.addWall(5700, 600, 3045, 6100, 13000, 3235);

plan.setDefaultMaterial($$Двутавр20).setDefaultPattern("background").setDefaultBackgroundColor(50, 50, 50).setDefaultKeColor(50, 50, 50);
Балки_перекрытия = [
  plan.addBeam(200, 700, 3335, 10400, 700, 3335, 50, 200),
  plan.addBeam(200, 2200, 3335, 10400, 2200, 3335, 50, 200),
  plan.addBeam(200, 3700, 3335, 10400, 3700, 3335, 50, 200),
  plan.addBeam(200, 5200, 3335, 10400, 5200, 3335, 50, 200),
  plan.addBeam(200, 6700, 3335, 10400, 6700, 3335, 50, 200),
  plan.addBeam(200, 8200, 3335, 10400, 8200, 3335, 50, 200),
  plan.addBeam(200, 9700, 3335, 10400, 9700, 3335, 50, 200),
  plan.addBeam(200, 11200, 3335, 10400, 11200, 3335, 50, 200),
  plan.addBeam(200, 12700, 3335, 10400, 12700, 3335, 50, 200)
];

plan.addLight("ambient").setCenter(5000, 6000, 10000);
plan.addLight("directional").setCenter(5000, 6000, 10000).setDirection(0, 0, 0);

project.saveToFile("Перекрытие двутавр.mpc");
Console.printVariables();
Console.print("*** Конец Перекрытие двутавр ***");
