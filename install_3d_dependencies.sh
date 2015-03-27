# Download J3D from Oracle official site
# Current version is 1.5.1

mvn install:install-file -DgroupId=java3d -DartifactId=j3d-core -Dversion=1.5.1 -Dpackaging=jar -Dfile=./lib/j3dcore.jar -DgeneratePom=true
mvn install:install-file -DgroupId=java3d -DartifactId=j3d-core-utils -Dversion=1.5.1 -Dpackaging=jar -Dfile=./lib/j3dutils.jar -DgeneratePom=true
mvn install:install-file -DgroupId=java3d -DartifactId=vecmath -Dversion=1.5.1 -Dpackaging=jar -Dfile=./lib/vecmath.jar -DgeneratePom=true
