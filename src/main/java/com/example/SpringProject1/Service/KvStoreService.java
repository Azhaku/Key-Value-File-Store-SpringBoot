package com.example.SpringProject1.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.SpringProject1.FileUtill.FileUtil;
import com.example.SpringProject1.Model.KvEntry;
@Service
public class KvStoreService {

    private static final Logger logger = LoggerFactory.getLogger(KvStoreService.class);

    private final Map<String, KvEntry> store = new ConcurrentHashMap<>();
    @Value("${kvstore.file.path:kvstore.json}")
    private String filePath;

    public KvStoreService() {
        try {
            Map<String, KvEntry> loadedData = FileUtil.loadFromFile(filePath, KvEntry.class);
            if (loadedData != null) {
                store.putAll(loadedData);
                logger.info("Loaded store from file: {}", store.size());
            } else {
                logger.warn("No data loaded; starting with an empty store.");
            }
        } catch (Exception e) {
            logger.error("Failed to load store from file.", e);
        }
    }

    public String create(String key, String value, Integer ttlSeconds) {
        if (key == null || value == null || key.length() > 32 || value.length() > 16 * 1024) {
            return "Invalid key or value!";
        }
        if (store.containsKey(key)) {
            return "Key already exists!";
        }
        LocalDateTime ttl = (ttlSeconds != null) ? LocalDateTime.now().plusSeconds(ttlSeconds) : null;
        store.put(key, new KvEntry(key, value, ttl));
        persistStore();
        return "Key-Value pair created!";
    }

    public KvEntry read(String key) {
        if (key == null) {
            logger.error("Read failed: Key is null.");
            return null;
        }
        KvEntry entry = store.get(key);
        if (entry == null || isExpired(entry)) {
            store.remove(key);
            persistStore();
            return null;
        }
        return entry;
    }

    public String delete(String key) {
        if (key == null) {
            return "Key cannot be null!";
        }
        KvEntry removed = store.remove(key);
        persistStore();
        return (removed != null) ? "Key deleted!" : "Key not found!";
    }

    public String batchCreate(Map<String, String> entries, Integer ttlSeconds) {
        if (entries == null || entries.size() > 100) {
            return "Invalid batch size!";
        }
        entries.forEach((key, value) -> {
            if (key.length() <= 32 && value.length() <= 16 * 1024 && !store.containsKey(key)) {
                LocalDateTime ttl = (ttlSeconds != null) ? LocalDateTime.now().plusSeconds(ttlSeconds) : null;
                store.put(key, new KvEntry(key, value, ttl));
            }
        });
        persistStore();
        return "Batch created successfully!";
    }

    private boolean isExpired(KvEntry entry) {
        return entry.getTtl() != null && entry.getTtl().isBefore(LocalDateTime.now());
    }

    private void persistStore() {
        try {
            FileUtil.saveToFile(filePath, store);
            logger.info("Store saved to file successfully.");
        } catch (Exception e) {
            logger.error("Error saving store to file.", e);
        }
    }
}
