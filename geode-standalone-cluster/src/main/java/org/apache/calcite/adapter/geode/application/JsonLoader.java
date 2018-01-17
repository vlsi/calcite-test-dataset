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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.geode.cache.Cache;
import org.apache.geode.cache.Region;
import org.apache.geode.pdx.PdxInstance;
import org.apache.geode.pdx.PdxInstanceFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * @author Christian Tzolov
 */
public class JsonLoader {

    private Cache cache;
    private String rootPackage;

    private Region region;

    private ObjectMapper mapper;

    public JsonLoader(Cache cache, String regionName, String rootPackage) {
        this.cache = cache;
        this.rootPackage = rootPackage;
        this.region = cache.getRegion(regionName);
        this.mapper = new ObjectMapper();
    }

    public void load(String jsonPath) throws IOException {

        Resource jsonFileResource = new DefaultResourceLoader().getResource(jsonPath);

        InputStreamReader isr = new InputStreamReader(jsonFileResource.getURL().openStream());
        try (BufferedReader br = new BufferedReader(isr)) {
            int key = 0;
            for (String line; (line = br.readLine()) != null; ) {
                Map jsonMap = mapper.readValue(line, Map.class);
                PdxInstance pdxInstance = mapToPdx(rootPackage, jsonMap);
                region.put(key++, pdxInstance);
            }
        }
    }

    private PdxInstance mapToPdx(String packageName, Map<String, Object> map) {
        PdxInstanceFactory pdxBuilder = cache.createPdxInstanceFactory(packageName);

        for (String name : map.keySet()) {
            Object value = map.get(name);

            if (value instanceof Map) {
                pdxBuilder.writeObject(name, mapToPdx(packageName + "." + name, (Map) value));
            } else {
                pdxBuilder.writeObject(name, value);
            }
        }

        return pdxBuilder.create();
    }

    public static void printJson(Object value) {
        try {
            System.out.println(new ObjectMapper().writeValueAsString(value));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
