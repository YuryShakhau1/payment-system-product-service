package by.shakhau.ps.product.service;

import by.shakhau.ps.product.service.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    List<Product> findByIdIn(List<UUID> ids);
    Page<Product> findAll(String name, Boolean deleted, Pageable pageable);
    Product findById(UUID id);
    List<Product> create(List<Product> products);
    Product create(Product product);
    Product update(Product product);
    void updateDeleted(UUID id, Boolean deleted);
}
