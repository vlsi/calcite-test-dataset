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

# This file is called automatically by Vagrant VM provision
echo Start Geode and Load datasets

cd /geode

ln -s /geode/geode-standalone-cluster-0.0.1-SNAPSHOT.jar /etc/init.d/geode-locator
ln -s /geode/geode-standalone-cluster-0.0.1-SNAPSHOT.jar /etc/init.d/geode-server

# Stop running Geode services and remove the related temp files
/etc/init.d/geode-locator stop
/etc/init.d/geode-server stop

rm ./vf.gf.*.pid
rm ./*.log
rm ./*.dat
rm -Rf ./ConfigDiskDir_locator

# Start Geode locator
/etc/init.d/geode-locator start --geode.memberMode=locator

sleep 15s

# Start Geode server
/etc/init.d/geode-server start --geode.memberMode=server \
       --geode.region.Zips=PARTITION \
       --geode.jsonLoad.Zips=file:/dataset/zips/zips.json

echo Geode is ready