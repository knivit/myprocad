#!/bin/bash

WORKING_DIR=$(cd `dirname $0`; pwd)

/usr/sbin/nginx -c $WORKING_DIR/nginx.conf -s stop
