package com.example.SpringProject1.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.SpringProject1.Model.KvEntry;
import com.example.SpringProject1.Service.KvStoreService;

/**
 * REST Controller for handling CRUD operations on a Key-Value store.
 * 
 * This controller provides endpoints for creating, reading, deleting, and batch creating key-value pairs.
 * Each operation allows the user to interact with the key-value store, which supports optional time-to-live (TTL) for data expiration.
 * The methods utilize the KvStoreService to perform business logic, and the responses are returned with appropriate HTTP status codes.
 * 
 * Structure of the class:
 * - The class uses Spring's `@RestController` annotation to define a RESTful web service.
 * - Each method handles one specific CRUD operation and is mapped to the corresponding HTTP verb.
 * - The class uses dependency injection to obtain a reference to the `KvStoreService`, which performs the actual operations on the key-value store.
 */
@RestController
@RequestMapping("/api/kvstore")
public class KvStoreController {

    @Autowired
    private KvStoreService service;  // Injecting the KvStoreService to handle the business logic

    /**
     * Creates a new Key-Value pair in the store.
     * 
     * This method allows the creation of a new key-value entry in the store, with an optional time-to-live (TTL) parameter.
     * If the key and value are valid, the service's `create` method is called, and a success or error message is returned.
     * 
     * @param key the key for the new entry
     * @param value the value for the new entry
     * @param ttl optional time-to-live for the entry in seconds (can be null)
     * @return ResponseEntity with a success or failure message and appropriate HTTP status
     */
    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestParam String key, @RequestParam String value, 
                                         @RequestParam(required = false) Integer ttl) {
        // Validate the input parameters for null or empty values
        if (key == null || key.trim().isEmpty() || value == null || value.trim().isEmpty()) {
            return new ResponseEntity<>("Invalid key or value!", HttpStatus.BAD_REQUEST);  // Bad request if input is invalid
        }

        // Call the service method to create the key-value pair
        String result = service.create(key, value, ttl);
        
        // Return success or error response based on the result
        if ("Key-Value pair created!".equals(result)) {
            return new ResponseEntity<>(result, HttpStatus.CREATED);  // Return HTTP 201 for successful creation
        } else {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);  // Return HTTP 400 for failure
        }
    }

    /**
     * Reads a Key-Value pair from the store by its key.
     * 
     * This method retrieves a key-value pair from the store using the provided key.
     * If the key exists, the corresponding value is returned; otherwise, a 404 status is returned.
     * 
     * @param key the key of the entry to retrieve
     * @return ResponseEntity with the KvEntry object if found, or a 404 Not Found response if not found
     */
    @GetMapping("/read")
    public ResponseEntity<KvEntry> read(@RequestParam String key) {
        // Validate that the key is not null or empty
        if (key == null || key.trim().isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);  // Bad request if the key is invalid
        }

        // Call the service method to read the key-value pair
        KvEntry entry = service.read(key);
        
        // Return the entry if found or a not found response
        if (entry != null) {
            return new ResponseEntity<>(entry, HttpStatus.OK);  // Return HTTP 200 with the found entry
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);  // Return HTTP 404 if not found
        }
    }

    /**
     * Deletes a Key-Value pair from the store by its key.
     * 
     * This method deletes an existing key-value pair from the store. If the key does not exist, an error message is returned.
     * 
     * @param key the key of the entry to delete
     * @return ResponseEntity with a success or failure message and appropriate HTTP status
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam String key) {
        // Validate the key parameter
        if (key == null || key.trim().isEmpty()) {
            return new ResponseEntity<>("Key cannot be null or empty!", HttpStatus.BAD_REQUEST);  // Bad request for empty key
        }

        // Call the service method to delete the key-value pair
        String result = service.delete(key);
        
        // Return success or failure response based on the result
        if ("Key deleted!".equals(result)) {
            return new ResponseEntity<>(result, HttpStatus.OK);  // HTTP 200 if deletion is successful
        } else {
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);  // HTTP 404 if the key is not found
        }
    }

    /**
     * Batch creates multiple Key-Value pairs in the store.
     * 
     * This method allows the creation of multiple key-value pairs at once. The entries are passed as a map, with keys being the keys
     * and values being the corresponding values. The TTL is optional for each entry.
     * 
     * @param entries a map containing key-value pairs to be created
     * @param ttl optional time-to-live for the entries in seconds
     * @return ResponseEntity with a success or failure message and appropriate HTTP status
     */
    @PostMapping("/batchCreate")
    public ResponseEntity<String> batchCreate(@RequestBody Map<String, String> entries, @RequestParam(required = false) Integer ttl) {
        // Validate the batch size and check if the entries are empty
        if (entries == null || entries.isEmpty() || entries.size() > 100) {
            return new ResponseEntity<>("Invalid batch size or empty entries!", HttpStatus.BAD_REQUEST);  // Batch size must be valid
        }

        // Call the service method to create the batch of key-value pairs
        String result = service.batchCreate(entries, ttl);
        
        // Return success or failure response based on the result
        if ("Batch created successfully!".equals(result)) {
            return new ResponseEntity<>(result, HttpStatus.CREATED);  // HTTP 201 if batch creation is successful
        } else {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);  // HTTP 400 if there is an error
        }
    }
}
