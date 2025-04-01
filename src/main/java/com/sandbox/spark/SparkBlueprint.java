
package com.sandbox.spark;

import java.util.Properties;
import com.sandbox.Blueprint;

public class SparkBlueprint extends Blueprint {
    public String sparkAppName = null;
    public String sparkMaster = null;
    public String sparkSqlShufflePartitions = null;
    public String sparkDynamicAllocationEnabled = null;
    public String sparkDynamicAllocationMinExecutors = null;
    public String sparkDynamicAllocationMaxExecutors = null;
    // public String sparkDriverHost = null;
    // public String sparkDriverPort = null;
    public String sparkDriverMemory = null;
    public String sparkExecutorMemory = null;
    public String sparkExecutorCores = null;
    // public String sparkExecutorHost = null;
    public String sparkEventLogEnabled = null;
    public String sparkEventLogDir = null;

    public SparkBlueprint(Properties properties) {
        this.sparkAppName = properties.getProperty("spark.app.name");
        this.sparkMaster = properties.getProperty("spark.master");
        this.sparkSqlShufflePartitions = properties.getProperty("spark.sql.shuffle.partitions");
        this.sparkDynamicAllocationEnabled = properties.getProperty("spark.dynamicAllocation.enabled");
        this.sparkDynamicAllocationMinExecutors = properties.getProperty("spark.dynamicAllocation.minExecutors");
        this.sparkDynamicAllocationMaxExecutors = properties.getProperty("spark.dynamicAllocation.maxExecutors");
        // this.sparkDriverHost = properties.getProperty("spark.driver.host");
        // this.sparkDriverPort = properties.getProperty("spark.driver.port");
        this.sparkDriverMemory = properties.getProperty("spark.driver.memory");
        this.sparkExecutorMemory = properties.getProperty("spark.executor.memory");
        this.sparkExecutorCores = properties.getProperty("spark.executor.cores");
        // this.sparkExecutorHost = properties.getProperty("spark.executor.host");
        this.sparkEventLogEnabled = properties.getProperty("spark.eventLog.enabled");
        this.sparkEventLogDir = properties.getProperty("spark.eventLog.dir");

        validateBlueprint(this.getClass(), properties);
    }
}
