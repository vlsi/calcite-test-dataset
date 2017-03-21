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

node 'ubuntucalcite' {
    # group { 'puppet': ensure => present }

    Exec {
        path => [ '/bin/', '/sbin/', '/usr/bin/', '/usr/sbin/', '/usr/local/bin/' ],
        logoutput => 'on_failure'
    }

    # Install Oracle JDK
    class { 'oracle_java':
        add_alternative => true,
        add_system_env  => true,
        before => [ Class['cassandra'], Class['elasticsearch'] ]
    }

    # Cassandra
    include cassandra::datastax_repo
    class {'cassandra':
        package_ensure => '3.0.9',
        settings => {
            cluster_name                => 'CalciteCassandraCluster',
            authenticator               => 'AllowAllAuthenticator',
            listen_address              => "${::ipaddress_enp0s3}",
            rpc_address                 => "${::ipaddress_enp0s3}",
            commitlog_directory         => '/var/lib/cassandra/commitlog',
            commitlog_sync              => 'periodic',
            commitlog_sync_period_in_ms => 10000,
            data_file_directories       => ['/var/lib/cassandra/data'],
            saved_caches_directory      => '/var/lib/cassandra/saved_caches',
            partitioner                 => 'org.apache.cassandra.dht.Murmur3Partitioner',
            endpoint_snitch             => 'SimpleSnitch',
            seed_provider               => [{
                class_name => 'org.apache.cassandra.locator.SimpleSeedProvider',
                parameters => [{
                    seeds      => "${::ipaddress_enp0s3}",
                }]
            }],
            start_native_transport      => true,
        },
        require => Class['cassandra::datastax_repo']
    }

    # Mongo
    apt::source { 'mongodb':
        location        => 'http://repo.mongodb.org/apt/ubuntu',
        release         => 'xenial/mongodb-org/3.4',
        repos           => 'multiverse',
        architecture    => 'amd64,arm64',
        key             => {
            id     => '0C49F3730359A14518585931BC711F9BA15703C6',
            server => 'keyserver.ubuntu.com'
        },
    } ->
    class {'::mongodb::globals':
        manage_package_repo => false,
        manage_package      => true,
    } ->
    # This should install mongodb server and client, in the latest mongodb-org version
    file { '/var/run/mongodb': # XXX PID file cannot be writen to /var/run
        ensure => 'directory',
        mode  => '777'
    } ->
    class {'::mongodb::server':
        journal => true,
        bind_ip => ['0.0.0.0'],
        pidfilepath => '/var/run/mongodb/mongo.pid',
        require => Exec['apt_update'],
    } ->
    class {'::mongodb::client':
    } ->
    package { 'mongodb_tools':
        ensure => 'present',
        name   => 'mongodb-org-tools',
    }

    # MySQL
    class {'::mysql::server':
      root_password    => 'strongpassword',
      override_options => {
        'mysqld' => {
          'bind-address' => '0.0.0.0'
        }
      },
      restart => true,
    }
    class {'::mysql::client':
    }
    # Create foodmart database
    mysql::db {'foodmart':
      user     => 'foodmart',
      password => 'foodmart',
      host     => '%',
      grant    => ['ALL'],
    }

    # PostgreSQL 9.3 server
    class {'::postgresql::globals':
      version => '9.3',
      manage_package_repo => true,
      encoding => 'UTF8',
      locale  => 'en_US.utf8',
    } ->
    class {'::postgresql::server':
      service_ensure => true,
      listen_addresses => '*',
      ip_mask_allow_all_users => '0.0.0.0/0',
      ipv4acls => ['local all all md5'],
      require => Exec['apt_update'],
    }
    # Create postgresql database
    postgresql::server::db {'foodmart':
      user     => 'foodmart',
      password => postgresql_password('foodmart', 'foodmart'),
    } ->
    # Create postgresql database
    postgresql::server::schema {'foodmart':
      owner    => 'foodmart',
      db       => 'foodmart',
    }

    # Elasticsearch
    class { 'elasticsearch':
      manage_repo  => true,
      repo_version => '2.x',
      config => {
        'network' => {
          'bind_host' => 0,
          'host' => '0.0.0.0'
        },
        'script' => {
          'inline' => 'on',
          'indexed' => 'on'
        },
        'http.cors' => {
          'enabled' => true,
          'allow_origin' => '/https?:\/\/.*/'
        }
      }
    } ->
    elasticsearch::instance { 'calcite': }
}

node 'ubuntucalcite-not-yet-ready' {
  class {"splunk":
    install => "server",
  }
}
