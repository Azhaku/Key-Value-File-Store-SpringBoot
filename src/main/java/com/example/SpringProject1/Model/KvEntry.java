package com.example.SpringProject1.Model;

import java.time.LocalDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;
public final class KvEntry {
    private String key;
    private String value;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime ttl;

    // Default constructor
    public KvEntry() {}

    // Parameterized constructor
    public KvEntry(String key, String value, LocalDateTime ttl) {
        this.setKey(key);
        this.setValue(value);
        this.setTtl(ttl);
    }

    // Getters and Setters
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
        this.key = key.trim(); // Trim spaces
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Value cannot be null or empty");
        }
        this.value = value.trim(); // Trim spaces
    }

    public LocalDateTime getTtl() {
        return ttl;
    }

    public void setTtl(LocalDateTime ttl) {
        if (ttl != null && ttl.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("TTL cannot be in the past");
        }
        this.ttl = ttl;
    }

    // Utility Methods
    @Override
    public String toString() {
        return String.format("KvEntry{key='%s', value='%s', ttl=%s}", key, value, ttl);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KvEntry kvEntry = (KvEntry) o;
        return Objects.equals(key, kvEntry.key) &&
                Objects.equals(value, kvEntry.value) &&
                Objects.equals(ttl, kvEntry.ttl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value, ttl);
    }
}
