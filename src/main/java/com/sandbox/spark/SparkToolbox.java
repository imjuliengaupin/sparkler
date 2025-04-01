
package com.sandbox.spark;

import java.util.Map;
import java.util.Properties;
import org.apache.spark.sql.SparkSession;
import com.sandbox.Blueprint;

public class SparkToolbox {
    public Blueprint sparkBlueprint = null;
    public Map<String, String> blueprint = null;
    public Properties sparkProperties = null;
    public SparkSession sparkSession = null;

    public SparkToolbox(Properties properties) throws Exception {
        this.sparkProperties = properties;

        this.sparkBlueprint = new SparkBlueprint(this.sparkProperties);
        this.blueprint = this.sparkBlueprint.reflectBlueprint();

        this.sparkSession = this.openSparkSession();
    }

    public SparkSession getSparkSession() {
        return this.sparkSession;
    }

    public SparkSession openSparkSession() throws Exception {
        switch (this.blueprint.get("sparkMaster")) {
            case "yarn":
                return this.sparkOnYarn();
            case "local[*]":
                return this.sparkOnLocal();
            default:
                throw new Exception("invalid option provided, available options are: yarn, local[*]");
        }
    }

    public SparkSession sparkOnLocal() throws Exception {
        SparkSession.Builder builder = SparkSession
                .builder()
                .appName(this.blueprint.get("sparkAppName"))
                .master(this.blueprint.get("sparkMaster"));

        // apply /src/main/resources/spark/spark.properties to the builder
        for (String key : this.sparkProperties.stringPropertyNames()) {
            String value = this.sparkProperties.getProperty(key);
            builder.config(key, value);
        }

        this.sparkSession = builder
                .getOrCreate();

        return this.sparkSession;
    }

    public SparkSession sparkOnYarn() throws Exception {
        SparkSession.Builder builder = SparkSession
                .builder()
                .appName(this.blueprint.get("sparkAppName"))
                .master(this.blueprint.get("sparkMaster"));

        // apply /src/main/resources/spark/spark.properties to the builder
        for (String key : this.sparkProperties.stringPropertyNames()) {
            String value = this.sparkProperties.getProperty(key);
            builder.config(key, value);
        }

        this.sparkSession = builder
                .enableHiveSupport()
                .getOrCreate();

        return this.sparkSession;
    }
}
