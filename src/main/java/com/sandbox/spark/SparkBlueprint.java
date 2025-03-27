
package com.sandbox.spark;

import java.util.Properties;
import com.sandbox.Blueprint;

public class SparkBlueprint extends Blueprint {
    public String sample = null;

    public SparkBlueprint(Properties properties) {
        this.sample = properties.getProperty("sample.property");
    }
}
