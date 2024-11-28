package com.example.SpringProject1.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.SpringProject1.Model.KvEntry;
import com.example.SpringProject1.Service.KvStoreService;

import java.util.Map;

@RestController
@RequestMapping("/api/kvstore")
public class KvStoreController {
    @Autowired
    private KvStoreService service;

    @PostMapping("/create")
    public String create(@RequestParam String key, @RequestParam String value, @RequestParam(required = false) Integer ttl) {
        return service.create(key, value, ttl);
    }

    @GetMapping("/read")
    public KvEntry read(@RequestParam String key) {
        return service.read(key);
    }

    @DeleteMapping("/delete")
    public String delete(@RequestParam String key) {
        return service.delete(key);
    }

    @PostMapping("/batchCreate")
    public String batchCreate(@RequestBody Map<String, String> entries, @RequestParam(required = false) Integer ttl) {
        return service.batchCreate(entries, ttl);
    }
}
