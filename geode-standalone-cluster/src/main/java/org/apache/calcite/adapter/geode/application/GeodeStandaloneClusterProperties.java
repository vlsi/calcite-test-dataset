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

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Christian Tzolov
 */
@ConfigurationProperties("geode")
public class GeodeStandaloneClusterProperties {

    public enum MemberMode {locator, server, all};

    private MemberMode memberMode;
    private Integer locatorPort = 10334;
    private String locatorHost = "localhost";
    private Integer serverPort = 40405;
    private Integer jmxManagerPort = 1099;
    private Boolean jmxManager = true;
    private Boolean jmxManagerStart = true;
    private Boolean startDevRestApi = true;
    private String memberName = "server1";
    private Boolean enableDebug = false;
    private String[] pdxPatterns = new String[]{"org.apache.calcite.adapter.geode.*"};
    private Map<String, String> region = new HashMap<>();
    private Map<String, String> jsonLoad = new HashMap<>();

    public MemberMode getMemberMode() {
        return memberMode;
    }

    public void setMemberMode(MemberMode memberMode) {
        this.memberMode = memberMode;
    }

    public Integer getLocatorPort() {
        return locatorPort;
    }

    public void setLocatorPort(Integer locatorPort) {
        this.locatorPort = locatorPort;
    }

    public String getLocatorHost() {
        return locatorHost;
    }

    public void setLocatorHost(String locatorHost) {
        this.locatorHost = locatorHost;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public Integer getJmxManagerPort() {
        return jmxManagerPort;
    }

    public void setJmxManagerPort(Integer jmxManagerPort) {
        this.jmxManagerPort = jmxManagerPort;
    }

    public Boolean getJmxManager() {
        return jmxManager;
    }

    public void setJmxManager(Boolean jmxManager) {
        this.jmxManager = jmxManager;
    }

    public Boolean getJmxManagerStart() {
        return jmxManagerStart;
    }

    public void setJmxManagerStart(Boolean jmxManagerStart) {
        this.jmxManagerStart = jmxManagerStart;
    }

    public Boolean getStartDevRestApi() {
        return startDevRestApi;
    }

    public void setStartDevRestApi(Boolean startDevRestApi) {
        this.startDevRestApi = startDevRestApi;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public Boolean getEnableDebug() {
        return enableDebug;
    }

    public void setEnableDebug(Boolean enableDebug) {
        this.enableDebug = enableDebug;
    }

    public String[] getPdxPatterns() {
        return pdxPatterns;
    }

    public void setPdxPatterns(String[] pdxPatterns) {
        this.pdxPatterns = pdxPatterns;
    }

    public Map<String, String> getRegion() {
        return region;
    }

    public void setRegion(Map<String, String> region) {
        this.region = region;
    }

    public Map<String, String> getJsonLoad() {
        return jsonLoad;
    }

    public void setJsonLoad(Map<String, String> jsonLoad) {
        this.jsonLoad = jsonLoad;
    }
}
