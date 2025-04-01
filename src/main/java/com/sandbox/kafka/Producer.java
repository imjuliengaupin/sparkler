
package com.sandbox.kafka;

import java.util.Map;
import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import com.sandbox.Blueprint;

public class Producer extends KafkaProducer<String, String> {
    public Blueprint producerBlueprint = null;
    public Map<String, String> blueprint = null;
    public Properties producerProperties = null;

    public Producer(Properties properties) throws Exception {
        // must explicitly invoke the super constructor in KafkaProducer class
        super(properties);

        this.producerProperties = properties;

        this.producerBlueprint = new ProducerBlueprint(this.producerProperties);
        this.blueprint = this.producerBlueprint.reflectBlueprint();
    }

    public Map<String, String> getProducerBlueprint() {
        return this.blueprint;
    }
}
