package com.example.SpringProject1.FileUtill;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FileUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Load data from a file into a Map with generic values.
     *
     * @param filePath the path to the file.
     * @return a Map of data from the file, or an empty Map if an error occurs.
     */
    public static Map<String, Object> loadFromFile(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                System.err.println("File not found: " + filePath);
                return Collections.emptyMap();
            }
            return objectMapper.readValue(file, new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
            return Collections.emptyMap();
        }
    }

    /**
     * Save data to a file.
     *
     * @param filePath the path to the file.
     * @param data     the data to be saved.
     */
    public static void saveToFile(String filePath, Object data) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
        } catch (IOException e) {
            throw new RuntimeException("Error saving to file: " + filePath, e);
        }
    }

    /**
     * Load data from a file into a Map with specified value type.
     *
     * @param filePath  the path to the file.
     * @param valueType the class type of the map's values.
     * @param <T>       the type of the map values.
     * @return a Map with specified value type, or an empty Map if an error occurs.
     */
    public static <T> Map<String, T> loadFromFile(String filePath, Class<T> valueType) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                System.err.println("File not found: " + filePath);
                return new ConcurrentHashMap<>();
            }
            return objectMapper.readValue(file, objectMapper.getTypeFactory()
                    .constructMapType(ConcurrentHashMap.class, String.class, valueType));
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath + " - " + e.getMessage());
            return new ConcurrentHashMap<>();
        }
    }
}
