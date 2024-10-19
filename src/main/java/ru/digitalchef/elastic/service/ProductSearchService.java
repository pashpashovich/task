package ru.digitalchef.elastic.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.digitalchef.elastic.config.FilterConfig;
import ru.digitalchef.elastic.entity.Product;
import ru.digitalchef.elastic.entity.Sku;

import java.io.IOException;
import java.util.List;

@Service
public class ProductSearchService {

    private final ElasticsearchClient elasticsearchClient;
    private final FilterConfig filterConfig;

    @Autowired
    public ProductSearchService(ElasticsearchClient elasticsearchClient, FilterConfig filterConfig) {
        this.elasticsearchClient = elasticsearchClient;
        this.filterConfig = filterConfig;
    }

    public List<Product> search(String query) throws IOException {
        SearchRequest searchRequest = createSearchRequest(query);
        SearchResponse<Product> searchResponse = elasticsearchClient.search(searchRequest, Product.class);

        List<Product> products = searchResponse.hits().hits().stream()
                .map(Hit::source)
                .toList();

        if (filterConfig.isEnabled()) {
            products.forEach(product -> {
                List<Sku> filteredSkus = product.getSkus().stream()
                        .filter(sku -> sku.getColor().equals(filterConfig.getColor())
                                && sku.getAvailable().equals(filterConfig.getAvailable()))
                        .toList();
                product.setSkus(filteredSkus);
            });
        }

        return products;
    }

    private SearchRequest createSearchRequest(String query) {
        if (filterConfig.isEnabled()) {
            return SearchRequest.of(s -> s
                    .index("products")
                    .query(q -> q
                            .bool(b -> b
                                    .must(m -> m
                                            .multiMatch(mm -> mm
                                                    .query(query)
                                                    .fields("name", "description")
                                            )
                                    )
                                    .filter(f -> f
                                            .nested(n -> n
                                                    .path("skus")
                                                    .query(nq -> nq
                                                            .bool(nb -> nb
                                                                    .must(m -> m
                                                                            .term(t -> t
                                                                                    .field("skus.color")
                                                                                    .value(filterConfig.getColor())
                                                                            )
                                                                    )
                                                                    .must(m -> m
                                                                            .term(t -> t
                                                                                    .field("skus.available")
                                                                                    .value(filterConfig.getAvailable())
                                                                            )
                                                                    )
                                                            )
                                                    )
                                            )
                                    )
                            )
                    )
            );
        } else {
            return SearchRequest.of(s -> s
                    .index("products")
                    .query(q -> q
                            .multiMatch(m -> m
                                    .query(query)
                                    .fields("name", "description")
                            )
                    )
            );
        }
    }
}
