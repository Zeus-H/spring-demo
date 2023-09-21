#!/bin/bash

# wget https://purcotton-omni.oss-cn-shenzhen.aliyuncs.com/omni/excel_template/sh/timed_task.sh

wget https://purcotton-omni.oss-cn-shenzhen.aliyuncs.com/omni/excel_template/sh/clean_logs.sh
echo "下载脚本..."

chmod +x clean_logs.sh
echo "设置权限..."

#echo "* * * * * /bin/echo 'the first cron entry'  >>/root/demo.txt" | sudo crontab -
echo "0 1 * * * /root/clean_logs.sh" | sudo crontab -
echo "设置定时任务..."
# 重启
sudo systemctl restart crond
# 删除自身文件
rm -rf timed_task.sh

# 批量删除模糊名称的trade-source-server.2023-07- 的log数据
# find . -name "trade-source-server.2023-07-*" -exec rm -rf {} \;