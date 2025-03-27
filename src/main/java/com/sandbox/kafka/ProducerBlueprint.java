
package com.sandbox.kafka;

import java.util.Properties;
import com.sandbox.Blueprint;

public class ProducerBlueprint extends Blueprint {
    public String keySerializer = null;
    public String valueSerializer = null;
    public String bootstrapServers = null;

    public ProducerBlueprint(Properties properties) {
        this.keySerializer = properties.getProperty("key.serializer");
        this.valueSerializer = properties.getProperty("value.serializer");
        this.bootstrapServers = properties.getProperty("bootstrap.servers");
    }
}
