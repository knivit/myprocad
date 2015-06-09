Console.clear();

project = Application.createProject();
Application.executeScript("lib/js/БиблиотекаФункций.js");
Application.executeScript("lib/js/Деденево/3D/Мансарда/Крыша.js");
project.saveToFile("Мансарда.mpc");