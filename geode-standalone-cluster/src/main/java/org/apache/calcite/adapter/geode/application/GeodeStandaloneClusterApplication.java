/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.calcite.adapter.geode.application;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.CacheFactory;
import org.apache.geode.cache.RegionShortcut;
import org.apache.geode.distributed.LocatorLauncher;
import org.apache.geode.distributed.ServerLauncher;
import org.apache.geode.pdx.ReflectionBasedAutoSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.io.File;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableConfigurationProperties(GeodeStandaloneClusterProperties.class)
public class GeodeStandaloneClusterApplication implements CommandLineRunner {

    private static Logger LOG = LoggerFactory.getLogger(GeodeStandaloneClusterApplication.class);

    @Autowired
    private GeodeStandaloneClusterProperties properties;

    public static void main(String[] args) {
        SpringApplication.run(GeodeStandaloneClusterApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        if (properties.getMemberMode() == GeodeStandaloneClusterProperties.MemberMode.locator) {
            startLocator();
        } else if (properties.getMemberMode() == GeodeStandaloneClusterProperties.MemberMode.server) {
            startServer(false);
        } else if (properties.getMemberMode() == GeodeStandaloneClusterProperties.MemberMode.all) {
            startServer(true);
        }
    }

    private void startServer(boolean startLocator) throws Exception {
        ServerLauncher.Builder serverBulder = new ServerLauncher.Builder()
                .setPdxPersistent(false)
                .setPdxReadSerialized(true)
                .setDebug(properties.getEnableDebug())
                .setServerPort(properties.getServerPort())
                .setPdxSerializer(
                        new ReflectionBasedAutoSerializer(properties.getPdxPatterns()))
//                .set("locator-wait-time", "10")
                .set("start-dev-rest-api", "" + properties.getStartDevRestApi())
                .set("use-cluster-configuration", "false")
                .setMemberName(properties.getMemberName())
                .set("locators", properties.getLocatorHost() + "[" + properties.getLocatorPort() + "]");

        if (startLocator) {
            serverBulder.set("start-locator", properties.getLocatorHost() + "[" + properties.getLocatorPort() + "]")
                    .set("jmx-manager-port", "" + properties.getJmxManagerPort())
                    .set("jmx-manager", "" + properties.getJmxManager())
                    .set("jmx-manager-start", "" + properties.getJmxManagerStart());
        }

        ServerLauncher serverLauncher = serverBulder.build();

        ServerLauncher.ServerState start = serverLauncher.start();

        Cache cache = new CacheFactory().create();

        // Create regions as defined in geode.region.<region-name>=<region-type> properties
        for (String regionName : properties.getRegion().keySet()) {
            RegionShortcut regionType = RegionShortcut.valueOf(properties.getRegion().get(regionName));
            cache.createRegionFactory(regionType).create(regionName);
            LOG.info("Create region [" + regionName + "] or type [" + regionType + "]");
        }

        LOG.info("Current Directory: " + new File(".").getAbsolutePath());

        // Load json data into Regions
        for (String regionName : properties.getJsonLoad().keySet()) {
            String jsonPath = properties.getJsonLoad().get(regionName);
            new JsonLoader(cache, regionName, "org.apache.calcite.adapter.geode." + regionName)
                    .load(jsonPath);

            LOG.info("Load data [" + jsonPath + "] in region [" + regionName + "]");
        }

        LOG.info("Cache server successfully started");

    }

    private void startLocator() {
        LocatorLauncher locatorLauncher = new LocatorLauncher.Builder()
                .set("jmx-manager", "true")
                .set("jmx-manager-start", "true")
                .set("jmx-manager-http-port", "8083")
                .set("use-cluster-configuration", "false")
                .setMemberName("locator")
                .setPort(properties.getLocatorPort())
//                .setBindAddress("127.0.0.1")
                .build();

        LOG.info("Attempting to start Locator");

        locatorLauncher.start();

        locatorLauncher.waitOnStatusResponse(30, 5, TimeUnit.SECONDS);

        LOG.info("Locator successfully started");
    }
}
