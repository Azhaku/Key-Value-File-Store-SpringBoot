package com.example.SpringProject1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.SpringProject1.Controller.KvStoreController;
import com.example.SpringProject1.Model.KvEntry;
import com.example.SpringProject1.Service.KvStoreService;

@ExtendWith(MockitoExtension.class)
public class AppTest {

    @InjectMocks
    private KvStoreController controller;

    @Mock
    private KvStoreService service;

    private KvEntry kvEntry;

    @BeforeEach
    public void setUp() {
        kvEntry = new KvEntry("username", "john_doe", LocalDateTime.now());
    }

    @Test
    public void testCreate_ValidKeyValue() {
        when(service.create("username", "john_doe", 3600)).thenReturn("Key-Value pair created!");

        ResponseEntity<String> response = controller.create("username", "john_doe", 3600);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Key-Value pair created!", response.getBody());
    }

    @Test
    public void testCreate_InvalidKey() {
        ResponseEntity<String> response = controller.create("", "john_doe", 3600);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid key or value!", response.getBody());
    }

    @Test
    public void testRead_ValidKey() {
        when(service.read("username")).thenReturn(kvEntry);

        ResponseEntity<KvEntry> response = controller.read("username");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(kvEntry, response.getBody());
    }

    @Test
    public void testRead_KeyNotFound() {
        when(service.read("non_existing_key")).thenReturn(null);

        ResponseEntity<KvEntry> response = controller.read("non_existing_key");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testDelete_ValidKey() {
        when(service.delete("username")).thenReturn("Key deleted!");

        ResponseEntity<String> response = controller.delete("username");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Key deleted!", response.getBody());
    }

    @Test
    public void testDelete_KeyNotFound() {
        when(service.delete("non_existing_key")).thenReturn("Key not found");

        ResponseEntity<String> response = controller.delete("non_existing_key");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Key not found", response.getBody());
    }

    @Test
    public void testBatchCreate_ValidBatch() {
        Map<String, String> entries = Map.of("username", "john_doe", "email", "john.doe@example.com");
        when(service.batchCreate(entries, 3600)).thenReturn("Batch created successfully!");

        ResponseEntity<String> response = controller.batchCreate(entries, 3600);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Batch created successfully!", response.getBody());
    }

    @Test
    public void testBatchCreate_InvalidBatch() {
        Map<String, String> entries = Map.of();  // Empty batch

        ResponseEntity<String> response = controller.batchCreate(entries, 3600);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid batch size or empty entries!", response.getBody());
    }
}
