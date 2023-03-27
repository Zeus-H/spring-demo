#!/bin/bash

# 定义阈值和需要清理的路径
THRESHOLD=85
CLEAN_PATH=/home/

# 进入路径
cd $CLEAN_PATH

# 查找最大的 .log 和 .err 文件，并将结果保存到数组中
log_files=$(find . -type f \( -name "*.log" -o -name "*.err" \) -printf "%s %p\n" | sort -rn | head -n 2 | awk '{print $2}')


# 获取磁盘使用率，并将百分比转换为整数
USAGE=$(df -h $CLEAN_PATH | awk 'NR==2 {print $5}' | sed 's/%//')
if [ $USAGE -ge $THRESHOLD ]; then
  echo "Disk usage is above threshold. Cleaning files in $CLEAN_PATH..."
  # 循环清空文件内容
  for file in "${log_files[@]}"; do
      cat /dev/null > "$file"
      echo "$file :文件已清空..."
  done
fi