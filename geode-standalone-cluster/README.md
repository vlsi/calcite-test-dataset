## Geode Standalone Cluster
Utility project used to bootstrap a standalone cluster with 2 members: `locator1` and `server1`
Cluster is embedded in SpringBoot application and is registered as service daemon.

#### Start/Stop members

##### Locator only
* Status: `/etc/init.d/geode-locator status`
* Stop: `/etc/init.d/geode-locator stop`
* Start: `/etc/init.d/geode-locator start --geode.memberMode=locator`

#### Server only
* Status: `/etc/init.d/geode-server status`
* Stop: `/etc/init.d/geode-server stop`
* Start: 
```
/etc/init.d/geode-server start --geode.memberMode=server \
                 --geode.region.Zips=PARTITION \
                 --geode.jsonLoad.Zips=file:/dataset/zips/zips.json
```

#### Start Server with embedded Locator
```
/etc/init.d/geode-server start --geode.memberMode=all \
                 --geode.region.Zips=PARTITION \
                 --geode.jsonLoad.Zips=file:/dataset/zips/zips.json
```

#### Log files
* Geode Locator: `/var/log/geode-locator.log`
* Geode Server: `/var/log/geode-server.log`


##### Build/Start SpringBoot

```bash
cd geode-standalone-cluster
mvn clean install -DskipTests
```

Start locator (10334) + server and load the test regions.

```bash
cd geode-standalone-cluster/target
java -jar --geode.member-mode=all  --geode.region.Zips=PARTITION \
          --geode.jsonLoad.Zips=file:/dataset/zips/zips.json \
           ./geode-standalone-cluster-0.0.1-SNAPSHOT.jar
```