#!/bin/bash

process_name="demo.jar"

# 查找包含特定名称的 Java 进程的 ID
process_id=$(jps -l | grep "$process_name" | awk '{print $1}')

if [ -z "$process_id" ]; then
    echo "Process $process_name not found."
else
    echo "Stopping process $process_name with PID $process_id..."
    kill -9 "$process_id"
    echo "Process $process_name with PID $process_id stopped."
fi

echo "Starting $process_name..."
# 此处启动服务的命令
nohup java -jar -Dspring.jpa.show-sql=false -Dlogging.level.com.purcotton.salechannelapigateway.taobaoapigateway.site.api=info -Dspring.profiles.active=prod demo.jar >/dev/null 2>&1 &

echo "Service $process_name started."
