package com.example.SpringProject1.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.SpringProject1.FileUtill.FileUtil;
import com.example.SpringProject1.Model.KvEntry;

/**
 * Service class that manages a key-value store with support for creating,
 * reading, deleting, and batch processing key-value pairs. It handles
 * persistence of data to a file and implements time-to-live (TTL) for entries.
 */
@Service
public class KvStoreService {

	// Logger instance for logging information, warnings, and errors
	private static final Logger logger = LoggerFactory.getLogger(KvStoreService.class);

	// In-memory store for key-value pairs
	private final Map<String, KvEntry> store = new ConcurrentHashMap<>();

	// File path for persisting the store data (can be configured through
	// application properties)
	@Value("${kvstore.file.path:kvstore.json}")
	private String filePath;

	// File utility for loading and saving data to/from file
	private FileUtil fileUtil;

	/**
	 * Constructor for KvStoreService. The constructor loads the data from a file if
	 * it exists, or starts with an empty store.
	 *
	 * @param fileUtil FileUtil instance used to load and save data to a file.
	 */
	@Autowired
	public KvStoreService(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}

	/**
	 * Default constructor that tries to load the key-value store from the
	 * configured file. If the file does not exist or there is an error, an empty
	 * store is initialized.
	 */
	public KvStoreService() {
		try {
			Map<String, KvEntry> loadedData = fileUtil.loadFromFile(filePath, KvEntry.class);
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

	/**
	 * Creates a new key-value pair in the store.
	 * 
	 * @param key        Key of the new entry.
	 * @param value      Value of the new entry.
	 * @param ttlSeconds Time-to-live (TTL) in seconds. If null, the entry never
	 *                   expires.
	 * @return A message indicating whether the creation was successful or failed.
	 */
	public String create(String key, String value, Integer ttlSeconds) {
		// Validate the key and value
		if (key == null || key.trim().isEmpty() || value == null || value.trim().isEmpty()) {
			return "Invalid key or value!";
		}

		// Check for length constraints
		if (key.length() > 32 || value.length() > 16 * 1024) {
			return "Key or value exceeds maximum length!";
		}

		// Ensure the key does not already exist
		if (store.containsKey(key)) {
			return "Key already exists!";
		}

		// Set TTL if provided, else leave it as null (no expiry)
		LocalDateTime ttl = (ttlSeconds != null) ? LocalDateTime.now().plusSeconds(ttlSeconds) : null;
		store.put(key, new KvEntry(key, value, ttl));

		// Persist the updated store to a file
		persistStore();

		return "Key-Value pair created!";
	}

	/**
	 * Reads a value from the store by its key.
	 * 
	 * @param key The key to search for in the store.
	 * @return The corresponding KvEntry if found and not expired; null if not found
	 *         or expired.
	 */
	public KvEntry read(String key) {
		if (key == null || key.trim().isEmpty()) {
			logger.error("Read failed: Key is null or empty.");
			return null;
		}

		KvEntry entry = store.get(key);

		// Check if the entry has expired or does not exist
		if (entry == null || isExpired(entry)) {
			store.remove(key); // Remove expired or non-existent entries
			persistStore();
			return null;
		}

		return entry;
	}

	/**
	 * Deletes a key-value pair from the store.
	 * 
	 * @param key The key of the entry to delete.
	 * @return A message indicating whether the deletion was successful or not.
	 */
	public String delete(String key) {
		if (key == null || key.trim().isEmpty()) {
			return "Key cannot be null or empty!";
		}

		KvEntry removed = store.remove(key);
		persistStore();

		return (removed != null) ? "Key deleted!" : "Key not found!";
	}

	/**
	 * Creates multiple key-value pairs in batch.
	 * 
	 * @param entries    A map of key-value pairs to be added to the store.
	 * @param ttlSeconds Time-to-live (TTL) in seconds for the batch of entries. If
	 *                   null, no expiry.
	 * @return A message indicating the result of the batch creation process.
	 */
	public String batchCreate(Map<String, String> entries, Integer ttlSeconds) {
		// Validate the batch size (ensure it does not exceed 100 entries)
		if (entries == null || entries.size() > 100) {
			return "Invalid batch size!";
		}

		// Iterate through the entries and add valid ones to the store
		entries.forEach((key, value) -> {
			if (key.length() <= 32 && value.length() <= 16 * 1024 && !store.containsKey(key)) {
				LocalDateTime ttl = (ttlSeconds != null) ? LocalDateTime.now().plusSeconds(ttlSeconds) : null;
				store.put(key, new KvEntry(key, value, ttl));
			}
		});

		persistStore();

		return "Batch created successfully!";
	}

	/**
	 * Checks if the given entry has expired based on its TTL.
	 * 
	 * @param entry The KvEntry to check.
	 * @return true if the entry has expired; false otherwise.
	 */
	private boolean isExpired(KvEntry entry) {
		return entry.getTtl() != null && entry.getTtl().isBefore(LocalDateTime.now());
	}

	/**
	 * Persists the current state of the store to a file.
	 */
	private void persistStore() {
		try {
			fileUtil.saveToFile(filePath, store); // Save the current store data to a file
			logger.info("Store saved to file successfully.");
		} catch (Exception e) {
			logger.error("Error saving store to file.", e); // Log any error that occurs during file saving
		}
	}
}
