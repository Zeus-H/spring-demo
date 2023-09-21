#!/bin/bash

# 安装Redis依赖库
sudo yum install gcc-c++ -y
# 设置ZooKeeper版本
REDIS_VERSION="4.0.11"
# shellcheck disable=SC2034
INSTALL_PATH=/usr/local/redis

# 下载、编译和安装Redis
cd /tmp || exit
wget http://download.redis.io/releases/redis-${REDIS_VERSION}.tar.gz
tar -xzvf redis-${REDIS_VERSION}.tar.gz 
cd redis-${REDIS_VERSION} || exit
make
sudo make install

# 创建Redis配置文件
sudo mkdir /etc/redis
sudo cp redis.conf /etc/redis/redis.conf

# 修改Redis配置文件以设置密码、监听IP、端口、持久化快照目录
REDIS_PASSWORD="你要设置的密码"
sed -i "s/# requirepass foobared/requirepass $REDIS_PASSWORD/" /etc/redis/redis.conf
# sed -i "s/port 6379/port 你要修改的端口/" /etc/redis/redis.conf
sed -i "s!dir ./!dir /tmp!g" /etc/redis/redis.conf

# 启动Redis服务器
sudo /usr/local/bin/redis-server /etc/redis/redis.conf

# 添加Redis服务器开机自启动
echo -e "[Unit]\nDescription=Redis\nAfter=network.target\n\n[Service]\nExecStart=/usr/local/bin/redis-server /etc/redis/redis.conf\nExecStop=/usr/local/bin/redis-cli -h 127.0.0.1 -p 6379 -a $REDIS_PASSWORD shutdown\nRestart=always\n\n[Install]\nWantedBy=multi-user.target" | sudo tee /etc/systemd/system/redis.service

# 启动Redis服务并设置开机自启动
sudo systemctl start redis
sudo systemctl enable redis

# 清理安装文件
cd ..
rm -rf redis-${REDIS_VERSION} redis-${REDIS_VERSION}.tar.gz

echo "Redis安装完成并已设置为开机自启动，密码已配置。"
