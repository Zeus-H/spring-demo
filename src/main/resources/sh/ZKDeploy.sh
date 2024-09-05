#!/bin/bash

# 设置ZooKeeper版本
ZOOKEEPER_VERSION="3.6.2"
# 设置ZooKeeper安装目录
ZOOKEEPER_INSTALL_DIR="/opt/zookeeper"

# 下载ZooKeeper二进制文件
wget https://archive.apache.org/dist/zookeeper/zookeeper-${ZOOKEEPER_VERSION}/apache-zookeeper-${ZOOKEEPER_VERSION}-bin.tar.gz
# 解压安装
tar -xzvf apache-zookeeper-${ZOOKEEPER_VERSION}-bin.tar.gz -C /opt
# 重命名目录
mv /opt/apache-zookeeper-${ZOOKEEPER_VERSION}-bin /opt/zookeeper

# 创建ZooKeeper数据目录
mkdir -p /var/lib/zookeeper/data
# 创建ZooKeeper日志目录
mkdir -p /var/lib/zookeeper/log

# 复制示例配置文件
cp $ZOOKEEPER_INSTALL_DIR/conf/zoo_sample.cfg $ZOOKEEPER_INSTALL_DIR/conf/zoo.cfg

# 编辑配置文件（根据需要进行修改）
# vi $ZOOKEEPER_INSTALL_DIR/conf/zoo.cfg

# 启动ZooKeeper
$ZOOKEEPER_INSTALL_DIR/bin/zkServer.sh start

# 设置ZooKeeper开机自启动
echo "$ZOOKEEPER_INSTALL_DIR/bin/zkServer.sh start" >> /etc/rc.local
chmod +x /etc/rc.local
echo "ZooKeeper安装和启动完成！"
