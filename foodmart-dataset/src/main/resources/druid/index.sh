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

# Start task to index the foodmart data set
curl -X 'POST' -H 'Content-Type:application/json' \
    -d @foodmart/foodmart-index.json \
    localhost:8090/druid/indexer/v1/task

sleep 5

# Set finer segmentGranularity, see CALCITE-1430
sed -i 's/\"segmentGranularity\" : \"day\"/\"segmentGranularity\" : \"hour\"/g' quickstart/tutorial/wikipedia-index.json

# Start task to index the wikiticker data set
curl -X 'POST' -H 'Content-Type:application/json' \
    -d @quickstart/tutorial/wikipedia-index.json \
    localhost:8090/druid/indexer/v1/task

# End
