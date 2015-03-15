time mvn -T2C -Dmaven.test.skip=true package > /tmp/maven.log 2>&1

if [ "$?" != "0" ] ; then
  cat /tmp/maven.log
else
  java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=9009 \
     -Djava.util.logging.config.file=./logging.properties \
     -jar ./target/myprocad-1.0.jar \
     2>./stderr.log &

  JPID=$!

  # wait until Java process stopped
  # catch and pop up it's stderr
  ERR_LINES=0
  IS_ALIVE=0
  while [ "$IS_ALIVE" == "0" ] ; do
    sleep 1
    kill -s 0 $JPID 2>/dev/null
    IS_ALIVE=$?

    if [ -f ./stderr.log ] ; then
      EL=`cat ./stderr.log | wc -l`
      if [ "$ERR_LINES" != "$EL" ] ; then
        "d:/distr/notifu-1.6/notifu64.exe" /m "Error, line $ERR_LINES"
         ERR_LINES=$EL
      fi
    fi
  done
fi
