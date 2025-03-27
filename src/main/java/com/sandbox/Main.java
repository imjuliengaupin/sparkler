
package com.sandbox;

import java.io.InputStream;
import java.net.Socket;
import java.util.Properties;
import org.apache.spark.sql.SparkSession;
import com.sandbox.kafka.Producer;
import com.sandbox.spark.SparkToolbox;

public class Main {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static SparkSession sparkSession = null;
    public static Producer kafkaProducer = null;

    public static void main(String[] args) {
        // setup spark
        sparkSession = setupSparkSession();

        // setup kafka
        kafkaProducer = setupKafkaProducer();

        shutdown();
    }

    public static void shutdown() {
        kafkaProducer.close();
        sparkSession.stop();

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
                System.out.println("Kafka Producer: " + ANSI_GREEN + "ACTIVE" + ANSI_RESET);
            }

        } catch (Exception e) {
            System.out.println(ANSI_RED + e.getMessage() + ANSI_RESET);
            System.exit(1);
        }

        return kafkaProducer;
    }

    public static SparkSession setupSparkSession() {
        SparkToolbox sparkToolbox = null;

        try {
            // load custom spark properties
            Properties sparkProperties = loadCustomProperties("spark.properties");

            // create a spark session
            sparkToolbox = new SparkToolbox(sparkProperties);

            // BUG exception in connection from /0.0.0.0:0
            // extract host and port dynamically from the spark session
            String host = sparkToolbox.getSparkSession().sparkContext().getConf().get("spark.driver.host", "127.0.0.1");
            String port = sparkToolbox.getSparkSession().sparkContext().getConf().get("spark.driver.port", "7077");

            // check if server is reachable
            if (isPortOpen(host, Integer.parseInt(port))) {
                System.out.println("Spark Session: " + ANSI_GREEN + "ACTIVE" + ANSI_RESET);
            }

        } catch (Exception e) {
            System.out.println(ANSI_RED + e.getMessage() + ANSI_RESET);
            System.exit(1);
        }

        return sparkToolbox.getSparkSession();
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
                    "unable to connect to " + host + ":" + port);
        }
    }
}
