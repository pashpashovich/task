package ru.digitalchef.elastic.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.digitalchef.elastic.entity.Product;
import ru.digitalchef.elastic.repository.ProductRepository;

import java.io.IOException;
import java.util.List;

@Service
public class DataLoadService {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @Autowired
    private ProductRepository productRepository;


    public void loadDataToElasticsearch() throws IOException {
        List<Product> products = productRepository.findAll();

        for (Product product : products) {
            IndexRequest<Product> request = IndexRequest.of(i -> i
                    .index("products")
                    .id(product.getId().toString())
                    .document(product)
            );
            IndexResponse response = elasticsearchClient.index(request);
            System.out.println("Indexed product: " + response.id());
        }
    }
}
