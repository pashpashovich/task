package ru.digitalchef.elastic.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.digitalchef.elastic.entity.Product;
import ru.digitalchef.elastic.repository.ProductRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ElasticsearchSyncService {

    private final ElasticsearchClient elasticsearchClient;
    private final ProductRepository productRepository;

    @Autowired
    public ElasticsearchSyncService(ElasticsearchClient elasticsearchClient, ProductRepository productRepository) {
        this.elasticsearchClient = elasticsearchClient;
        this.productRepository = productRepository;
    }

    public List<Product> getProducts() {
        return productRepository.findByInSale(true);
    }

    public void syncData() throws IOException {
        List<Product> products = productRepository.findByInSale(true);

        for (Product product : products) {
            IndexRequest<Product> request = IndexRequest.of(i -> i
                    .index("products")
                    .id(product.getId().toString())
                    .document(product)
            );

            // Execute the indexing request
            IndexResponse response = elasticsearchClient.index(request);
            System.out.println("Indexed document with ID: " + response.id());
        }
    }
}

