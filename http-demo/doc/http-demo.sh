#!/bin/bash

# APP名称
export APP_NAME=http-demo-1.0.0.jar

# base 函数
killAPP() {
  pid=$(ps -ef | grep $APP_NAME | grep java | awk '{print $2}')
  if [ "$pid" = "" ]; 
  then
    echo "no tomcat pid alive"
  else
	echo "$APP_NAME PID  :$pid"
    kill -9 $pid
  fi
}
# 停掉项目
killAPP

# startup
nohup java -jar /www/APP/http-demo-1.0.0.jar >/www/APP/logs/http-demo.log 2>&1 &



