
package com.sandbox.kafka;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Blueprint {

    public Blueprint() {
        // empty default constructor
    }

    public Map<String, String> getBlueprintMapping() throws Exception {
        // use reflection to map class fields to their associated values being assigned
        // in the class constructor
        Map<String, String> blueprintMapping = new HashMap<String, String>();

        for (Field classField : this.getClass().getDeclaredFields()) {
            // reflected object should supress java language access checking when it is used
            classField.setAccessible(true);

            String key = classField.getName();
            String value = classField.get(this).toString();

            // append the field name and its value to the map
            blueprintMapping.put(key, value);
        }

        if (blueprintMapping.isEmpty()) {
            throw new Exception("properties not found in /src/main/resources");
        }

        return blueprintMapping;
    }
}
