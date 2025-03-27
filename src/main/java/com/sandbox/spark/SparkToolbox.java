
package com.sandbox.spark;

import java.util.Properties;
import org.apache.spark.sql.SparkSession;

public class SparkToolbox {
    private Properties sparkProperties = null;
    private String sparkAppName = null;
    private String sparkMaster = null;
    private SparkSession sparkSession = null;

    public SparkToolbox(Properties sparkProperties) throws Exception {
        this.sparkProperties = sparkProperties;
        this.sparkAppName = this.sparkProperties.getProperty("spark.app.name");
        this.sparkMaster = this.sparkProperties.getProperty("spark.master");
        this.sparkSession = this.openSparkSession();
    }

    public SparkSession getSparkSession() {
        return this.sparkSession;
    }

    public SparkSession openSparkSession() throws Exception {
        switch (this.sparkMaster) {
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
                .appName(this.sparkAppName)
                .master(this.sparkMaster);

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
                .appName(this.sparkAppName)
                .master(this.sparkMaster);

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
