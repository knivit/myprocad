Console.clear();

project = Application.createProject();
Application.executeScript("lib/js/БиблиотекаФункций.js");
Application.executeScript("lib/js/Деденево/3D/ЧастиМодели/Крыша3D.js");
Application.executeScript("lib/js/Деденево/3D/ЧастиМодели/Стены3D.js");
project.saveToFile("Модель3D.mpc");
