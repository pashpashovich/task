package ru.digitalchef.elastic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.digitalchef.elastic.service.ProductIndexService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api/load")
public class DataLoadController {

    private final ProductIndexService dataLoadService;

    @Autowired
    public DataLoadController(ProductIndexService dataLoadService) {
        this.dataLoadService = dataLoadService;
    }

    @GetMapping("/active")
    public ResponseEntity<String> loadActiveProducts() {
        try {
            dataLoadService.recreateProductIndex();
            dataLoadService.loadActiveProductsToElasticsearch();
            return ResponseEntity.ok("Active products loaded to Elasticsearch");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error loading active products");
        }
    }

    @GetMapping("/afterStartDate")
    public ResponseEntity<String> loadProductsAfterStartDate(@RequestParam String startDate) {
        try {
            dataLoadService.recreateProductIndex();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = formatter.parse(startDate);
            dataLoadService.loadProductsAfterStartDateToElasticsearch(parsedDate);
            return ResponseEntity.ok("Products after " + startDate + " loaded to Elasticsearch");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error loading products after startDate");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid date format. Please use 'yyyy-MM-dd'");
        }
    }
}

