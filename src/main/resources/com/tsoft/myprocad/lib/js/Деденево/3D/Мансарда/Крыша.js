var plan = project.addPlan("Крыша");

/* Материалы */
$$Бетон = project.addMaterial("Бетон", 2.400, 4200);
$$КББ = project.addMaterial("КББ", 1100, 2500);
$$Кирпич = project.addMaterial("Кирпич", 2.400, 4200);
$$Доска200х50 = project.addMaterial("Доска 200х50", 0.550, 6500);
$$Брус100х100 = project.addMaterial("Брус 100х100", 0.550, 6500);
$$Брус150х150 = project.addMaterial("Брус 150х150", 0.550, 6500);

var $$Высота_первого_этажа = 3200;
var $$Высота_стены_мансарды = 1400;

/* Кирпичная облицовка */
plan.setDefaultMaterial($$Кирпич).setDefaultPattern("brick").setDefaultBackgroundColor(255, 40, 0).setDefaultKeColor(255, 40, 0);
plan.addWall(-20, -20, 3200, 10620, 100, 4378);
plan.addWall(-20, 100, 3200, 100, 13500, 4378);
plan.addWall(-20, 13500, 3200, 10620, 13620, 4378);
plan.addWall(10500, -20, 3200, 10620, 13500, 4378);

// Фронтоны
plan.addWall(400, 13500, 4600, 5300, 13620, 8500).setWallShape("Triangle3");
plan.addWall(5300, 13500, 4600, 10200, 13620, 8500).setWallShape("Triangle4");
plan.addWall(400, -20, 4600, 5300, 100, 8500).setWallShape("Triangle3");
plan.addWall(5300, -20, 4600, 10200, 100, 8500).setWallShape("Triangle4");

/* Армопояс */
plan.setDefaultMaterial($$Бетон).setDefaultPattern("foreground").setDefaultBackgroundColor(192, 192, 192).setDefaultKeColor(192, 192, 192);
plan.addWall(200, 200, 4400, 600, 13400, 4600);
plan.addWall(10000, 200, 4400, 10400, 13400, 4600);

/* Стены мансарды */
plan.setDefaultMaterial($$КББ).setDefaultPattern("hatchUp").setDefaultBackgroundColor(60, 60, 60).setDefaultKeColor(60, 60, 60);
plan.addWall(200, 200, 3400, 10400, 600, 4400);
plan.addWall(200, 600, 3400, 600, 13000, 4400);
plan.addWall(200, 13400, 3400, 10400, 13000, 4400);
plan.addWall(10000, 600, 3400, 10400, 13000, 4400);
plan.addWall(5700, 600, 3400, 6100, 13000, 3400 + 14*200);

/* Первый этаж */
plan.setDefaultMaterial($$Бетон).setDefaultPattern("hatchUp").setDefaultBackgroundColor(60, 60, 60).setDefaultKeColor(60, 60, 60);
plan.addWall(200, 200, 0, 10400, 600, 3200 - 100);
plan.addWall(200, 600, 0, 600, 13000, 3200 - 100);
plan.addWall(200, 13000, 0, 10400, 13400, 3200 - 100);
plan.addWall(10000, 600, 0, 10400, 13000, 3200 - 100);

/* Цоколь */
plan.setDefaultMaterial($$Бетон).setDefaultPattern("hatchUp").setDefaultBackgroundColor(60, 60, 60).setDefaultKeColor(60, 60, 60);
plan.addWall(200, 200, -800, 10400, 600, -100);
plan.addWall(200, 600, -800, 600, 13000, -100);
plan.addWall(200, 13000, -800, 10400, 13400, -100);
plan.addWall(10000, 600, -800, 10400, 13000, -100);
