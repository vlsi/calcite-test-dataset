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
echo Importing zips dataset
cd /dataset

# Since ES takes a while to start, keep trying to connect
for i in $(seq 1 10); do
  curl -s -XPOST localhost:9200/_bulk --data-binary "@zips/zips_es.json" > /dev/null && break
  sleep 5s
done
