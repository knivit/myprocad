#!/bin/bash

set -e

WORKING_DIR=$(cd `dirname $0`; pwd)

for FILE in `find $WORKING_DIR/download/ -not -path '*.svn*' -type f` ; do
  FILENAME=`basename $FILE`
  HASH=`md5sum $FILE | awk '{ print $1 }'`
  SIZE=`du -h $FILE | awk '{ print $1 }'`
  DATE=`du --time --time-style='+ %d-%b-%y' $FILE | awk '{ print $2 }'`
  echo "$FILE"
  echo "  MD5: $HASH"
  echo "  Size: $SIZE"
  echo "  Date: $DATE"

  for HTML in `find $WORKING_DIR/ -not -path '*.svn*' -name download.html` ; do
    sed -i -b 's/>'$FILENAME'\([^#]*\)##HASH##/>'$FILENAME'\1'$HASH'/g' $HTML
    sed -i -b 's/>'$FILENAME'\([^#]*\)##SIZE##/>'$FILENAME'\1'$SIZE'/g' $HTML
    sed -i -b 's/>'$FILENAME'\([^#]*\)##DATE##/>'$FILENAME'\1'$DATE'/g' $HTML
  done
  echo ; echo
done
