package ru.digitalchef.elastic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.digitalchef.elastic.service.DataLoadService;

import java.io.IOException;

@RestController
@RequestMapping("/api/load")
public class DataLoadController {

    @Autowired
    private DataLoadService dataLoadService;

    @GetMapping()
    public ResponseEntity<String> loadData() {
        try {
            dataLoadService.loadDataToElasticsearch();
            return ResponseEntity.ok("Data loaded to Elasticsearch");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error loading data");
        }
    }
}

