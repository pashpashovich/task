package ru.digitalchef.elastic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.digitalchef.elastic.entity.Sku;

public interface SkuRepository extends JpaRepository<Sku, Long> {
}
