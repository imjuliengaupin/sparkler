
package com.sandbox.kafka;

import java.util.Map;
import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;

import com.sandbox.Blueprint;

public class Producer extends KafkaProducer<String, String> {
    public Blueprint blueprint = null;
    public Map<String, String> blueprintMapping = null;

    public Producer(Properties properties) throws Exception {
        // must explicitly invoke the super constructor in KafkaProducer class
        super(properties);

        // initialize custom blueprint
        this.blueprint = new ProducerBlueprint(properties);

        try {
            this.blueprintMapping = this.blueprint.reflectBlueprint();

        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public Map<String, String> getProducerBlueprint() {
        return this.blueprintMapping;
    }
}
