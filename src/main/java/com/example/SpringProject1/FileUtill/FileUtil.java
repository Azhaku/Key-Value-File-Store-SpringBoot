package com.example.SpringProject1.FileUtill;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    private final ObjectMapper objectMapper;

    @Autowired
    public FileUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Load data from a file into a Map with generic values.
     *
     * @param filePath the path to the file.
     * @return a Map of data from the file, or an empty Map if an error occurs.
     */
    public Map<String, Object> loadFromFile(String filePath) {
        if (!isFileAccessible(filePath)) {
            return Collections.emptyMap();
        }
        try (FileInputStream fis = new FileInputStream(filePath);
             FileChannel channel = fis.getChannel();
             FileLock lock = channel.lock(0, Long.MAX_VALUE, true)) {
            return objectMapper.readValue(new File(filePath), new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            logger.error("Error reading JSON file: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }

    /**
     * Save data to a file in JSON format.
     *
     * @param filePath the path to the file.
     * @param data     the data to be saved.
     */
    public void saveToFile(String filePath, Object data) {
        try {
            ensureFileExists(filePath);
            try (FileOutputStream fos = new FileOutputStream(filePath);
                 FileChannel channel = fos.getChannel();
                 FileLock lock = channel.lock()) {
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), data);
            }
        } catch (IOException e) {
            logger.error("Error saving data to file: {}", e.getMessage());
            throw new RuntimeException("Error saving data to file: " + filePath, e);
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
    public  <T> Map<String, T> loadFromFile(String filePath, Class<T> valueType) {
        if (!isFileAccessible(filePath)) {
            return new ConcurrentHashMap<>();
        }
        try (FileInputStream fis = new FileInputStream(filePath);
             FileChannel channel = fis.getChannel();
             FileLock lock = channel.lock(0, Long.MAX_VALUE, true)) {
            return objectMapper.readValue(new File(filePath),
                    objectMapper.getTypeFactory().constructMapType(ConcurrentHashMap.class, String.class, valueType));
        } catch (IOException e) {
            logger.error("Error reading file: {} - {}", filePath, e.getMessage());
            return new ConcurrentHashMap<>();
        }
    }

    /**
     * Checks if a file exists and is accessible.
     *
     * @param filePath the path to the file.
     * @return true if the file exists and is readable, false otherwise.
     */
    private boolean isFileAccessible(String filePath) {
        try {
            return Files.isReadable(Paths.get(filePath));
        } catch (Exception e) {
            logger.error("File access error: {} - {}", filePath, e.getMessage());
            return false;
        }
    }

    /**
     * Ensures that a file exists, creating it if necessary.
     *
     * @param filePath the path to the file.
     * @throws IOException if the file cannot be created.
     */
    private void ensureFileExists(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            Files.createDirectories(Paths.get(file.getParent()));
            file.createNewFile();
        }
    }
}
