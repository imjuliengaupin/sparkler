
package com.sandbox.kafka;

import java.util.Properties;
import com.sandbox.Blueprint;

public class ProducerBlueprint extends Blueprint {
    public String keySerializer = null;
    public String valueSerializer = null;
    public String bootstrapServers = null;
    public String acks = null;
    public String retries = null;
    public String batchSize = null;
    public String lingerMs = null;
    public String bufferMemory = null;
    public String compressionType = null;
    public String maxRequestSize = null;
    public String requestTimeoutMs = null;
    public String enableIdempotence = null;
    public String partitionerClass = null;

    public ProducerBlueprint(Properties properties) {
        this.keySerializer = properties.getProperty("key.serializer");
        this.valueSerializer = properties.getProperty("value.serializer");
        this.bootstrapServers = properties.getProperty("bootstrap.servers");
        this.acks = properties.getProperty("acks");
        this.retries = properties.getProperty("retries");
        this.batchSize = properties.getProperty("batch.size");
        this.lingerMs = properties.getProperty("linger.ms");
        this.bufferMemory = properties.getProperty("buffer.memory");
        this.compressionType = properties.getProperty("compression.type");
        this.maxRequestSize = properties.getProperty("max.request.size");
        this.requestTimeoutMs = properties.getProperty("request.timeout.ms");
        this.enableIdempotence = properties.getProperty("enable.idempotence");
        this.partitionerClass = properties.getProperty("partitioner.class");

        validateBlueprint(this.getClass(), properties);
    }
}
