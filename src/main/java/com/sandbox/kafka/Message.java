
package com.sandbox.kafka;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.lit;
import static org.apache.spark.sql.functions.struct;
import static org.apache.spark.sql.functions.to_json;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Message {
    public String message = null;
    public String applicationId = null;
    public String applicationName = null;

    public Message(SparkSession session) {
        this.applicationId = session.sparkContext().applicationId();
        this.applicationName = session.sparkContext().appName();
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void writeMessage(SparkSession session, Map<String, String> blueprint, Message message) {
        try {
            // capture the message as a json string
            String jsonMessage = new ObjectMapper().writeValueAsString(message);

            Dataset<String> dataFrame = session.createDataset(Collections.singletonList(jsonMessage),
                    Encoders.STRING());
            StructType dataFrameSchema = new StructType();

            System.out.println(dataFrame.showString(3, 0, false));

            // +----------------------------------------------------------------------------------------------------+
            // |value |
            // +----------------------------------------------------------------------------------------------------+
            // |{"message":"sample","applicationId":"sample","applicationName":"sample"}|
            // +----------------------------------------------------------------------------------------------------+

            Dataset<Row> jsonDataFrame = session.read().json(dataFrame);
            StructType jsonDataFrameSchema = jsonDataFrame.schema();

            for (StructField field : jsonDataFrameSchema.fields()) {
                dataFrameSchema = dataFrameSchema.add(field.name(), DataTypes.StringType, true);
            }

            // System.out.println(dataFrameSchema.prettyJson());

            jsonDataFrame = jsonDataFrame.selectExpr(dataFrameSchema.fieldNames())
                    .selectExpr(Arrays.stream(dataFrameSchema.fields())
                            .map(field -> String.format("CAST(%s AS STRING) AS %s", field.name(), field.name()))
                            .toArray(String[]::new));

            System.out.println(jsonDataFrame.showString(3, 0, false));

            // +-------------------+---------------+-------------+
            // |applicationId|applicationName|message|
            // +-------------------+---------------+-------------+
            // |sample |sample |sample|
            // +-------------------+---------------+-------------+

            Dataset<Row> messageDataFrame = jsonDataFrame.select(lit("blueprint").as("key"),
                    to_json(struct(Arrays.stream(dataFrameSchema.fieldNames()).filter(column -> !column.equals("key"))
                            .map(column -> col(column)).toArray(Column[]::new))).as("value"));

            System.out.println(messageDataFrame.showString(3, 0, false));

            // +------+-------------------------------------------------------------------------------+
            // |key|value |
            // +------+-------------------------------------------------------------------------------+
            // |sample|{"applicationId":"sample","applicationName":"sample","message":"sample"}|
            // +------+-------------------------------------------------------------------------------+

            // messageDataFrame.write().format("kafka").options(blueprint).save();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
