#!/usr/bin/env bash

# add Elasticsearch Library
sudo sed -i -e 's/us.archive.ubuntu.com/archive.ubuntu.com/g' /etc/apt/sources.list
sudo apt-get update

# install OpenJDK7
sudo apt-get purge openjdk*
sudo apt-get -y install openjdk-7-jdk

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