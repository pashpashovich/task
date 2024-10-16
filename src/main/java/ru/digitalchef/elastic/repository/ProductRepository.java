package ru.digitalchef.elastic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.digitalchef.elastic.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByInSale(Boolean inSale);

}