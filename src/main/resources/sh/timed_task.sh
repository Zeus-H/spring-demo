#!/bin/bash

# wget https://raw.githubusercontent.com/Zeus-H/spring-demo/master/src/main/resources/sh/timed_task.sh

wget https://raw.githubusercontent.com/Zeus-H/spring-demo/master/src/main/resources/sh/clean_logs.sh
echo "下载脚本..."

chmod +x clean_logs.sh
echo "设置权限..."

#echo "* * * * * /bin/echo 'the first cron entry'  >>/root/demo.txt" | sudo crontab -
echo "0 1 * * * /root/clean_logs.sh" | sudo crontab -
echo "设置定时任务..."
# 重启
sudo systemctl restart crond
