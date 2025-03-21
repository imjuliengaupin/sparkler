
package com.sandbox;

import java.io.InputStream;
import java.net.Socket;
import java.util.Properties;
import com.sandbox.kafka.Producer;

public class Main {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static void main(String[] args) {
        Producer kafkaProducer = setupKafkaProducer();

        kafkaProducer.close();
        System.exit(0);
    }

    public static Producer setupKafkaProducer() {
        Producer kafkaProducer = null;

        try {
            // load custom producer properties
            Properties kafkaProducerProperties = loadCustomProperties("producer.properties");

            // extract host and port from bootstrap.servers property
            String host = kafkaProducerProperties.getProperty("bootstrap.servers").split(":")[0];
            String port = kafkaProducerProperties.getProperty("bootstrap.servers").split(":")[1];

            // check if server is reachable
            if (isPortOpen(host, Integer.parseInt(port))) {
                kafkaProducer = new Producer(kafkaProducerProperties);
            }

        } catch (Exception e) {
            System.out.println(ANSI_RED + e.getMessage() + ANSI_RESET);
            System.exit(1);
        }

        return kafkaProducer;
    }

    public static Properties loadCustomProperties(String fileName) throws Exception {
        Properties properties = new Properties();

        // search project resources for the file (in the classpath)
        InputStream input = Main.class.getClassLoader().getResourceAsStream(fileName);

        if (input == null) {
            throw new Exception(fileName.concat(" not found in /src/main/resources"));

        } else {
            properties.load(input);
        }

        input.close();

        return properties;
    }

    public static boolean isPortOpen(String host, int port) throws Exception {
        try (Socket socket = new Socket(host, port)) {
            return true;

        } catch (Exception e) {
            throw new Exception(
                    "unable to connect to " + host + ":" + port + ", please check if the kafka server is running");
        }
    }
}
