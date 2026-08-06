package by.shakhau.ps.product.service;

import by.shakhau.ps.product.service.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    Page<Product> findAll(String name, Pageable pageable);
    List<Product> findByIdIn(List<UUID> ids);
    Product findById(UUID id);
    Product create(Product product);
    Product update(Product product);
    void updateDeleted(UUID id, Boolean deleted);
}
