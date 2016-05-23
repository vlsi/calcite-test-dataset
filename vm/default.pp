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
    class { 'cassandra::datastax_repo':
        before => Class['cassandra']
    } ->
    class {'cassandra':
        package_ensure   => '3.0.3',
        cluster_name     => 'CalciteCassandraCluster',
        endpoint_snitch  => 'SimpleSnitch',
        listen_address   => "${::ipaddress_eth0}",
        rpc_address      => "${::ipaddress_eth0}",
        seeds            => "${::ipaddress_eth0}",
        require          => Exec['apt_update'],
    }

    # Mongo
    # This should install mongodb server and client, in the latest mongodb-org version
    class {'::mongodb::globals':
        manage_package_repo => true,
        server_package_name => 'mongodb-org'
    } ->
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
