package ru.digitalchef.elastic.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.digitalchef.elastic.entity.Product;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductSearchService {

    private final ElasticsearchClient elasticsearchClient;

    @Autowired
    public ProductSearchService(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    public List<Product> search(String query) throws IOException {
        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index("products")
                .query(q -> q
                        .multiMatch(m -> m
                                .query(query)
                                .fields("name", "description", "skus.color")
                        )
                )
        );

        SearchResponse<Product> searchResponse = elasticsearchClient.search(searchRequest, Product.class);

        return searchResponse.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }
}
