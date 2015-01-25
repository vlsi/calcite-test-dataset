# Test data sets

This repository includes data sets and Vagrant script to provision a virtual machine
with pre-installed databases.

The idea is to have easily-available development machine for testing Apache Calcite.

# Requirements

* Java
* Maven 3.0.4
* <a href="https://www.vagrantup.com/">Vagrant</a>
* <a href="https://www.virtualbox.org/">Virtual Box</a>
* 1GiB of internet for initial VM provision
* ~10GiB disk space (VirtualBox image with data consumes 3.2GiB)

# Installation

Note: the databases are listening on the default ports, so you might need
to pick another ports if you have MongoDB/MySQL/PosgreSQL running on your host machine.
To update port forwarding, edit `vm/Vagrantfile`.

The step by step is as follows:
  * Install <a href="https://www.virtualbox.org/">Virtual Box</a>
  * Install <a href="https://www.vagrantup.com/">Vagrant</a>
  * Provision a VM:
```bash
mvn install # this will download base image and install all the databases
```

Note: it would take you 10-30 minutes depending on your machine and internet connection.

# List of created databases

* H2 (h2/target folder)
* HSQLDB (hsqldb/target folder)
* MongoDB (port 27017)
* MySQL (port 3306)
* PostgreSQL (port 5432)

# List of data sets

* <a href="https://github.com/julianhyde/foodmart-data-mysql">Foodmart</a> from Mondrian demo
* <a href="http://docs.mongodb.org/manual/tutorial/aggregation-zip-code-data-set">Mongo zips</a> (as of 25 January 2015)

# Using the VM

## How to create a VM
A single `mvn install` setups and starts up the VM.
```bash
mvn install
```

Note: `vm/target` stores `apt-get` cache (~150MiB), so you might want avoid cleaning it.

## How to drop the VM
Note: this destroys VM's data (virtual hard drive), so make sure you've backed up all your changes done in the VM.

```bash
cd vm && vagrant destroy
```

## How to connect to VM via SSH

```bash
cd vm && vagrant ssh
```

## How to startup and shutdown the VM

```bash
cd vm
vagrant up
vagrant halt
```

## Accessing MongoDB in the VM
Zips data:
```bash
$ cd vm & vagrant ssh
vagrant@ubuntucalcite:~$ mongo test
MongoDB shell version: 2.6.6
connecting to: test
> show collections
system.indexes
zips
```

Foodmart data:
```bash
$ cd vm & vagrant ssh
vagrant@ubuntucalcite:~$ mongo foodmart
MongoDB shell version: 2.6.6
connecting to: foodmart
> show collections
account
agg_c_10_sales_fact_1997
agg_c_14_sales_fact_1997
agg_c_special_sales_fact_1997
agg_g_ms_pcat_sales_fact_1997
...
```

## Accessing MySQL in the VM
```bash
$ cd vm & vagrant ssh
vagrant@ubuntucalcite:~$ mysql --user=foodmart --password=foodmart --database=foodmart
...
Server version: 5.5.40-0ubuntu0.14.04.1 (Ubuntu)
...
mysql> show tables
    -> ;
+-------------------------------+
| Tables_in_foodmart            |
+-------------------------------+
| account                       |
| agg_c_10_sales_fact_1997      |
| agg_c_14_sales_fact_1997      |
| agg_c_special_sales_fact_1997 |
| agg_g_ms_pcat_sales_fact_1997 |
...
```

### Accessing PostgreSQL in the VM
```bash
$ cd vm & vagrant ssh
vagrant@ubuntucalcite:~$ PGPASSWORD=foodmart PGHOST=localhost psql -U foodmart -d foodmart
psql (9.3.5)
foodmart=> \d
 public | account                       | table | foodmart
 public | agg_c_10_sales_fact_1997      | table | foodmart
 public | agg_c_14_sales_fact_1997      | table | foodmart
 public | agg_c_special_sales_fact_1997 | table | foodmart
 public | agg_g_ms_pcat_sales_fact_1997 | table | foodmart
...
```
