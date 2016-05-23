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


# add Elasticsearch Library
sudo sed -i -e 's/us.archive.ubuntu.com/archive.ubuntu.com/g' /etc/apt/sources.list
sudo apt-get update

# enable add-apt-repository command
sudo apt-get -y install software-properties-common

# add openjdk8 repo
sudo add-apt-repository ppa:openjdk-r/ppa
sudo apt-get update

# install OpenJDK8
sudo apt-get -y install openjdk-8-jdk

# install Elasticsearch
wget -qO - https://packages.elastic.co/GPG-KEY-elasticsearch | sudo apt-key add -
echo "deb http://packages.elastic.co/elasticsearch/2.x/debian stable main" | sudo tee -a /etc/apt/sources.list.d/elasticsearch-2.x.list
sudo apt-get update && sudo apt-get install elasticsearch

# update Elasticsearch config
sudo update-rc.d elasticsearch defaults 95 10

# update elasticsearch.yml

sudo echo "network.bind_host: 0" >> /etc/elasticsearch/elasticsearch.yml
sudo echo "network.host: 0.0.0.0" >> /etc/elasticsearch/elasticsearch.yml
sudo echo "script.inline: on" >> /etc/elasticsearch/elasticsearch.yml
sudo echo "script.indexed: on" >> /etc/elasticsearch/elasticsearch.yml
sudo echo "http.cors.enabled: true" >> /etc/elasticsearch/elasticsearch.yml
sudo echo "http.cors.allow-origin: /https?:\/\/.*/" >> /etc/elasticsearch/elasticsearch.yml
sudo /etc/init.d/elasticsearch start
