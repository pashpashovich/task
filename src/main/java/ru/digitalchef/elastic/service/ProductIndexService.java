package ru.digitalchef.elastic.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.digitalchef.elastic.entity.Product;
import ru.digitalchef.elastic.repository.ProductRepository;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ProductIndexService {

    private final ElasticsearchClient elasticsearchClient;

    private final ProductRepository productRepository;

    private static final String INDEX_NAME = "products";

    @Autowired
    public ProductIndexService(ElasticsearchClient elasticsearchClient, ProductRepository productRepository) {
        this.elasticsearchClient = elasticsearchClient;
        this.productRepository = productRepository;
    }

    public void loadActiveProductsToElasticsearch() throws IOException {
        List<Product> activeProducts = productRepository.findByInSaleTrue();
        for (Product product : activeProducts) {
            IndexRequest<Product> request = IndexRequest.of(i -> i
                    .index(INDEX_NAME)
                    .id(product.getId().toString())
                    .document(product)
            );
            elasticsearchClient.index(request);
        }
    }

    public void loadProductsAfterStartDateToElasticsearch(Date startDate) throws IOException {
        List<Product> products = productRepository.findByDateOfCreationAfter(startDate);
        for (Product product : products) {
            IndexRequest<Product> request = IndexRequest.of(i -> i
                    .index(INDEX_NAME)
                    .id(product.getId().toString())
                    .document(product)
            );
            elasticsearchClient.index(request);
        }
    }

    public void createProductIndex() throws IOException {
        Map<String, Property> skusMapping = Map.of(
                "id", Property.of(p -> p.long_(l -> l)),
                "color", Property.of(p -> p.keyword(k -> k)),
                "available", Property.of(p -> p.boolean_(b -> b))
        );

        Map<String, Property> propertiesMapping = Map.of(
                "id", Property.of(p -> p.long_(l -> l)),
                "name", Property.of(p -> p.text(t -> t)),
                "description", Property.of(p -> p.text(t -> t)),
                "inSale", Property.of(p -> p.boolean_(b -> b)),
                "quantity", Property.of(p -> p.long_(l -> l)),
                "dateOfCreation", Property.of(p -> p.date(d -> d)),
                "skus", Property.of(p -> p.nested(n -> n
                        .properties(skusMapping)
                ))
        );

        CreateIndexRequest createIndexRequest = CreateIndexRequest.of(i -> i
                .index(INDEX_NAME)
                .mappings(TypeMapping.of(m -> m
                        .properties(propertiesMapping)
                ))
        );

        elasticsearchClient.indices().create(createIndexRequest);
    }

    public void recreateProductIndex() throws IOException {
        boolean indexExists = elasticsearchClient.indices().exists(e -> e.index(INDEX_NAME)).value();
        if (indexExists) {
            elasticsearchClient.indices().delete(d -> d.index(INDEX_NAME));
        }
        createProductIndex();
    }
}