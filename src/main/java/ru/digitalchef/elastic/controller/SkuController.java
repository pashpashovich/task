package ru.digitalchef.elastic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.digitalchef.elastic.entity.Sku;
import ru.digitalchef.elastic.repository.ProductRepository;
import ru.digitalchef.elastic.repository.SkuRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/skus")
public class SkuController {

    private final SkuRepository skuRepository;
    private final ProductRepository productRepository;

    @Autowired
    public SkuController(SkuRepository skuRepository, ProductRepository productRepository) {
        this.skuRepository = skuRepository;
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<Sku> getAllSkus() {
        return skuRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sku> getSkuById(@PathVariable Long id) {
        Optional<Sku> sku = skuRepository.findById(id);
        return sku.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PostMapping("/product/{productId}")
    public ResponseEntity<Sku> createSkuForProduct(@PathVariable Long productId, @RequestBody Sku sku) {
        return productRepository.findById(productId)
                .map(product -> {
                    sku.setProduct(product);
                    Sku savedSku = skuRepository.save(sku);
                    return ResponseEntity.status(HttpStatus.CREATED).body(savedSku);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sku> updateSku(@PathVariable Long id, @RequestBody Sku updatedSku) {
        return skuRepository.findById(id)
                .map(sku -> {
                    sku.setColor(updatedSku.getColor())
                            .setAvailable(updatedSku.getAvailable());
                    Sku savedSku = skuRepository.save(sku);
                    return ResponseEntity.ok(savedSku);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSku(@PathVariable Long id) {
        if (skuRepository.existsById(id)) {
            skuRepository.deleteById(id);
            return ResponseEntity.ok("Sku deleted");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sku not found");
        }
    }
}

