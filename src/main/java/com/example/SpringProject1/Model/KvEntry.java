package com.example.SpringProject1.Model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class KvEntry {
    private String key;
    private String value;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime ttl;

    // Constructors
    public KvEntry(String key, String value, LocalDateTime ttl) {
        this.key = key;
        this.value = value;
        this.ttl = ttl;
    }

    public KvEntry() {}

    // Getters and Setters
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public LocalDateTime getTtl() {
        return ttl;
    }

    public void setTtl(LocalDateTime ttl) {
        this.ttl = ttl;
    }
}
