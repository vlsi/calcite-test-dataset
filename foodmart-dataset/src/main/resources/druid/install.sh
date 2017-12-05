#!/bin/bash
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to you under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

cd ~
export druid_version=0.11.0
export zk_version=3.4.10

echo Removing previous Zookeeper
rm -rf zookeeper-${zk_version}

echo "Install and start Zookeeper Version - [${zk_version}]"
(
 if [ ! -f /var/cache/apt/archives/zookeeper-${zk_version}.tar.gz ]; then
   curl --silent http://apache.org/dist/zookeeper/zookeeper-${zk_version}/zookeeper-${zk_version}.tar.gz -o /var/cache/apt/archives/zookeeper-${zk_version}.tar.gz
 fi
 tar -xzf /var/cache/apt/archives/zookeeper-${zk_version}.tar.gz
 cd zookeeper-${zk_version}
 cp conf/zoo_sample.cfg conf/zoo.cfg
 ./bin/zkServer.sh start
)

echo Removing previous Druid
rm -rf druid-*

echo "Installing Druid Version - [${druid_version}]"
if [ ! -f /var/cache/apt/archives/druid-${druid_version}-bin.tar.gz ]; then
  curl --silent http://static.druid.io/artifacts/releases/druid-${druid_version}-bin.tar.gz -o /var/cache/apt/archives/druid-${druid_version}-bin.tar.gz
fi
tar -xzf /var/cache/apt/archives/druid-${druid_version}-bin.tar.gz
cd druid-${druid_version}
bin/init

ln -s /dataset/druid/foodmart .
ln -s /dataset/druid/run.sh .
ln -s /dataset/druid/index.sh .
ln -s /dataset/druid/query.sh .

echo Starting Druid
./run.sh
echo Index foodmart and wiki data sets
./index.sh
sleep 300
echo Run a query
./query.sh
echo Completed Druid start up

# End
