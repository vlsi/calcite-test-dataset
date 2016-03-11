#!/bin/bash
#
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

cd $(dirname $0)

echo Start Druid historical node
java `cat conf-quickstart/druid/historical/jvm.config | xargs` \
    -cp conf-quickstart/druid/_common:conf-quickstart/druid/historical:lib/* \
    io.druid.cli.Main server historical 2>&1 |
  tee historical.log &
sleep 30

echo Start Druid broker node
java `cat conf-quickstart/druid/broker/jvm.config | xargs` \
    -cp conf-quickstart/druid/_common:conf-quickstart/druid/broker:lib/* \
    io.druid.cli.Main server broker 2>&1 |
  tee broker.log &
sleep 30

echo Start Druid coordinator node
java `cat conf-quickstart/druid/coordinator/jvm.config | xargs` \
    -cp conf-quickstart/druid/_common:conf-quickstart/druid/coordinator:lib/* \
    io.druid.cli.Main server coordinator 2>&1 |
  tee coordinator.log &
sleep 30

echo Start Druid overlord node
java `cat conf-quickstart/druid/overlord/jvm.config | xargs` \
    -cp conf-quickstart/druid/_common:conf-quickstart/druid/overlord:lib/* \
    io.druid.cli.Main server overlord 2>&1 |
  tee overlord.log &
sleep 30

echo Start Druid middle manager node
java `cat conf-quickstart/druid/middleManager/jvm.config | xargs` \
    -cp conf-quickstart/druid/_common:conf-quickstart/druid/middleManager:lib/* \
    io.druid.cli.Main server middleManager 2>&1 |
  tee middleManager.log &
sleep 30

echo Index foodmart data set
curl -X 'POST' -H 'Content-Type:application/json' \
    -d @foodmart/foodmart-index.json \
    localhost:8090/druid/indexer/v1/task
sleep 300

echo Run a query
curl -L -H'Content-Type: application/json' -XPOST \
    --data-binary @foodmart/foodmart-top-cities.json \
    http://localhost:8082/druid/v2/?pretty

echo Completed Druid start up

# End
