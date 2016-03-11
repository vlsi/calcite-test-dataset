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
package net.hydromatic.foodmart;

import net.hydromatic.foodmart.data.hsqldb.FoodmartHsqldb;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;

/**
 * Generates a JSON file that contains a de-normalized Foodmart star schema,
 * to be loaded into Druid.
 */
public class FoodmartDruidJsonGenerator {
  public static void main(String[] args)
      throws SQLException, ClassNotFoundException, IOException {
    final String fileName = args.length > 0 ? args[0] : "/tmp/foodmart.json";
    final File file = fileName.equals("-") ? null : new File(fileName);
    final boolean verbose = file != null;
    if (file != null) {
      //noinspection ResultOfMethodCallIgnored
      file.getParentFile().mkdirs();
    }
    final String sql = "select\n"
        + " f.`store_sales`,\n"
        + " f.`store_cost`,\n"
        + " f.`unit_sales`,\n"
        + " p.`product_id`,\n"
        + " p.`brand_name`,\n"
        + " p.`product_name`,\n"
        + " p.`SKU`,\n"
        + " p.`SRP`,\n"
        + " p.`gross_weight`,\n"
        + " p.`net_weight`,\n"
        + " p.`recyclable_package`,\n"
        + " p.`low_fat`,\n"
        + " p.`units_per_case`,\n"
        + " p.`cases_per_pallet`,\n"
        + " p.`shelf_width`,\n"
        + " p.`shelf_height`,\n"
        + " p.`shelf_depth`,\n"
        + " q.`product_class_id`,\n"
        + " q.`product_subcategory`,\n"
        + " q.`product_category`,\n"
        + " q.`product_department`,\n"
        + " q.`product_family`,\n"
        + " c.`customer_id`,\n"
        + " c.`account_num`,\n"
        + " c.`lname`,\n"
        + " c.`fname`,\n"
        + " c.`mi`,\n"
        + " c.`address1`,\n"
        + " c.`address2`,\n"
        + " c.`address3`,\n"
        + " c.`address4`,\n"
        + " c.`city`,\n"
        + " c.`state_province`,\n"
        + " c.`postal_code`,\n"
        + " c.`country`,\n"
        + " c.`customer_region_id`,\n"
        + " c.`phone1`,\n"
        + " c.`phone2`,\n"
        + " c.`birthdate`,\n"
        + " c.`marital_status`,\n"
        + " c.`yearly_income`,\n"
        + " c.`gender`,\n"
        + " c.`total_children`,\n"
        + " c.`num_children_at_home`,\n"
        + " c.`education`,\n"
        + " c.`date_accnt_opened`,\n"
        + " c.`member_card`,\n"
        + " c.`occupation`,\n"
        + " c.`houseowner`,\n"
        + " c.`num_cars_owned`,\n"
        + " c.`fullname`,\n"
        + " r.`promotion_id`,\n"
        + " r.`promotion_district_id`,\n"
        + " r.`promotion_name`,\n"
        + " r.`media_type`,\n"
        + " r.`cost`,\n"
        + " r.`start_date`,\n"
        + " r.`end_date`,\n"
        + " s.`store_id`,\n"
        + " s.`store_type`,\n"
        + " s.`region_id`,\n"
        + " s.`store_name`,\n"
        + " s.`store_number`,\n"
        + " s.`store_street_address`,\n"
        + " s.`store_city`,\n"
        + " s.`store_state`,\n"
        + " s.`store_postal_code`,\n"
        + " s.`store_country`,\n"
        + " s.`store_manager`,\n"
        + " s.`store_phone`,\n"
        + " s.`store_fax`,\n"
        + " s.`first_opened_date`,\n"
        + " s.`last_remodel_date`,\n"
        + " s.`store_sqft`,\n"
        + " s.`grocery_sqft`,\n"
        + " s.`frozen_sqft`,\n"
        + " s.`meat_sqft`,\n"
        + " s.`coffee_bar`,\n"
        + " s.`video_store`,\n"
        + " s.`salad_bar`,\n"
        + " s.`prepared_food`,\n"
        + " s.`florist`,\n"
        + " t.`time_id`,\n"
        + " t.`the_date`,\n"
        + " t.`the_day`,\n"
        + " t.`the_month`,\n"
        + " t.`the_year`,\n"
        + " t.`day_of_month`,\n"
        + " t.`week_of_year`,\n"
        + " t.`month_of_year`,\n"
        + " t.`quarter`,\n"
        + " t.`fiscal_period`\n"
        + "from `foodmart`.`sales_fact_1997` as f\n"
        + "join `foodmart`.`product` as p on p.`product_id` = f.`product_id`\n"
        + "join `foodmart`.`product_class` as q on q.`product_class_id` = p.`product_class_id`\n"
        + "join `foodmart`.`customer` as c on c.`customer_id` = f.`customer_id`\n"
        + "join `foodmart`.`promotion` as r on r.`promotion_id` = f.`promotion_id`\n"
        + "join `foodmart`.`store` as s on s.`store_id` = f.`store_id`\n"
        + "join `foodmart`.`time_by_day` as t on t.`time_id` = f.`time_id`\n"
        + "order by `time_id`, `customer_id`, `product_id`\n";
    int n = 0;
    try (Connection connection =
             DriverManager.getConnection(FoodmartHsqldb.URI);
         Statement statement = connection.createStatement();
         PrintWriter pw = fileName.equals("-")
             ? new PrintWriter(System.out)
             : new PrintWriter(new FileWriter(fileName))) {
      final String sql2 = sql.replaceAll("`", "\\\"");
      if (verbose) {
        System.out.println(sql2);
      }
      final ResultSet r = statement.executeQuery(sql2);
      final StringBuilder b = new StringBuilder();
      final ResultSetMetaData metaData = r.getMetaData();
      final Pattern pattern =
          Pattern.compile("^([0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]) ");
      while (r.next()) {
        b.append("{");
        for (int i = 0; i < metaData.getColumnCount(); i++) {
          String v = r.getString(i + 1);
          if (v == null) {
            if (i == 0) {
              throw new AssertionError(); // cannot handle null in first column
            }
            continue;
          }
          if (i > 0) {
            b.append(", ");
          }
          final String c = metaData.getColumnLabel(i + 1);
          if (v.contains("\"") || v.contains("\\")
              || c.contains("\"") || c.contains("\\")) {
            throw new AssertionError("v: " + v + ", c: " + c); // need escape
          }
          if (c.equals("the_date")
              || c.equals("first_opened_date")
              || c.equals("last_remodel_date")) {
            v = pattern.matcher(v).replaceAll("$1T");
          }
          b.append("\"").append(c)
              .append("\": \"").append(v).append("\"");
        }
        b.append("},");
        pw.println(b);
        b.setLength(0);
        ++n;
      }
    }
    if (verbose) {
      System.out.println("Wrote " + n + " rows to " + fileName + ".");
    }
  }
}

// End FoodmartDruidJsonGenerator.java
