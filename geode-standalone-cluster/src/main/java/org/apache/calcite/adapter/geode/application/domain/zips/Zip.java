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
package org.apache.calcite.adapter.geode.application.domain.zips;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.Map;

/**
 * @author Christian Tzolov
 */
public class Zip {

    @JsonProperty("_id")
    private String id;
    private String city;
    private Float[] loc;
    private Integer pop;
    private String state;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Float[] getLoc() {
        return loc;
    }

    public void setLoc(Float[] loc) {
        this.loc = loc;
    }

    public Integer getPop() {
        return pop;
    }

    public void setPop(Integer pop) {
        this.pop = pop;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Zip{" +
                "id='" + id + '\'' +
                ", city='" + city + '\'' +
                ", loc=" + Arrays.toString(loc) +
                ", pop=" + pop +
                ", state='" + state + '\'' +
                '}';
    }

    public static void main(String[] args) throws  Exception {
        ObjectMapper mapper = new ObjectMapper();

        Zip zip = mapper.readValue(
                "{ \"_id\" : \"11565\", \"city\" : \"MALVERNE\", \"loc\" : [ -73.673073, 40.674982 ], \"pop\" : 8660, \"state\" : \"NY\" }",
                Zip.class);

        System.out.println(zip);


        Map map = mapper.readValue(
                "{ \"_id\" : \"11565\", \"city\" : \"MALVERNE\", \"loc\" : [ -73.673073, 40.674982 ], \"pop\" : 9999, \"state\" : \"NY\" }",
                Map.class);

        System.out.println(map.get("pop").getClass().getCanonicalName());

    }
}
