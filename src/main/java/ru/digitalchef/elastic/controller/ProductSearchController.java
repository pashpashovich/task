package ru.digitalchef.elastic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.digitalchef.elastic.entity.Product;
import ru.digitalchef.elastic.service.ProductSearchService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/search")
public class ProductSearchController {

    private final ProductSearchService searchService;

    @Autowired
    public ProductSearchController(ProductSearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/products")
    public List<Product> searchProducts(@RequestParam("query") String query) {
        try {
            return searchService.search(query);
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }
}

