
package com.sandbox.spark;

import org.apache.spark.sql.SparkSession;

public class SparkToolbox {
    private final String appName = "Sparkler";
    private SparkSession sparkSession = null;

    public SparkToolbox(String[] args) throws Exception {
        String option = null;

        if (args.length > 0) {
            option = args[0];

        } else {
            throw new Exception("no option provided, must provide either: yarn, local");
        }

        this.sparkSession = this.openSparkSession(option);
    }

    public SparkSession getSparkSession() {
        return this.sparkSession;
    }

    public SparkSession openSparkSession(String option) throws Exception {
        option = option.trim().toLowerCase();

        switch (option) {
            case "yarn":
                return this.sparkOnYarn(option);

            case "local":
                return this.sparkOnLocal(option);

            default:
                throw new Exception("invalid option provided, available options are: yarn, local");
        }
    }

    public SparkSession sparkOnLocal(String option) throws Exception {
        this.sparkSession = SparkSession
                .builder()
                .appName(this.appName)
                .master(option)
                .getOrCreate();

        return this.sparkSession;
    }

    public SparkSession sparkOnYarn(String option) throws Exception {
        return null;
    }
}
