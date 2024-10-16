package ru.digitalchef.elastic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.digitalchef.elastic.entity.Product;
import ru.digitalchef.elastic.service.ElasticsearchSyncService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/elastic-sync")
public class ElasticsearchSyncController {

    private final ElasticsearchSyncService syncService;

    @Autowired
    public ElasticsearchSyncController(ElasticsearchSyncService syncService) {
        this.syncService = syncService;
    }

    @GetMapping()
    public ResponseEntity<List<Product>> findAll() {
        List<Product> products = syncService.getProducts();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(products);
    }

    @PostMapping("/sync")
    public ResponseEntity<String> syncData() {
        try {
            syncService.syncData();
            return ResponseEntity.ok("Data synced to Elasticsearch");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error syncing data");
        }
    }
}

