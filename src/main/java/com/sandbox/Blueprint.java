
package com.sandbox;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Blueprint {

    public Blueprint() {
        // empty default constructor
    }

    public Map<String, String> reflectBlueprint() throws Exception {
        // use reflection to map class fields to their associated values being assigned
        // in the class constructor
        Map<String, String> map = new HashMap<String, String>();

        for (Field classField : this.getClass().getDeclaredFields()) {
            // reflected object should supress java language access checking when it is used
            classField.setAccessible(true);

            String key = classField.getName();
            String value = classField.get(this).toString();

            // TODO handle the scenario where value is null, otherwise a
            // NullPointerException is thrown because the field is defined in the blueprint
            // class, but not initialized

            // append the field name and its value to the map
            map.put(key, value);
        }

        if (map.isEmpty()) {
            throw new Exception("properties not found in /src/main/resources");
        }

        return map;
    }

    public void validateBlueprint(Class<?> blueprintClass, Properties properties) {
        // get all declared fields in the class
        Field[] fields = blueprintClass.getDeclaredFields();
        boolean blueprintSynced = true;

        // iterate through properties
        for (String key : properties.stringPropertyNames()) {
            boolean fieldExists = false;

            // Convert the property key to camelCase
            String camelCaseKey = toCamelCase(key);

            // check if the class has a field matching the property key
            for (Field field : fields) {
                if (field.getName().equals(camelCaseKey)) {
                    fieldExists = true;
                    break;
                }
            }

            // print a warning if the field does not exist
            if (!fieldExists) {
                System.out.println("WARNING: property '" + key
                        + "' does not have a matching field in " + blueprintClass.getSimpleName());
                blueprintSynced = false;
            }
        }

        if (blueprintSynced) {
            System.out.println("SUCCESS: all properties have matching fields in "
                    + blueprintClass.getSimpleName());
        }
    }

    public String toCamelCase(String key) {
        // helper method to convert dot-separated keys to camelCase
        StringBuilder camelCaseKey = new StringBuilder();
        boolean capitalizeNext = false;

        for (char c : key.toCharArray()) {
            if (c == '.') {
                capitalizeNext = true;

            } else if (capitalizeNext) {
                camelCaseKey.append(Character.toUpperCase(c));
                capitalizeNext = false;

            } else {
                camelCaseKey.append(c);
            }
        }

        return camelCaseKey.toString();
    }
}
