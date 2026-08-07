package by.shakhau.ps.product.service.impl;

import by.shakhau.ps.product.repository.ProductRepository;
import by.shakhau.ps.product.repository.entity.ProductEntity;
import by.shakhau.ps.product.service.ProductService;
import by.shakhau.ps.product.service.exception.ResourceForbiddenException;
import by.shakhau.ps.product.service.exception.ResourceNotFoundException;
import by.shakhau.ps.product.service.mapper.ProductMapper;
import by.shakhau.ps.product.service.model.Product;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper mapper;
    private final ProductRepository repository;

    @Override
    public Page<Product> findAll(String name, Pageable pageable) {
        Page<ProductEntity> products;
        if (name == null) {
            products = repository.findAll(pageable);
        } else {
            products = repository.findByNameStartingWith(name, pageable);
        }

        return products.map(mapper::toModel);
    }

    @Override
    public List<Product> findByIdIn(List<UUID> ids) {
        return repository.findAllById(ids).stream()
                .map(mapper::toModel)
                .toList();
    }

    @Override
    public Product findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toModel)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with id = %s nof found".formatted(id)));
    }

    @Transactional
    @Override
    public Product create(Product product) {
        if (product.getId() != null) {
            throw new ResourceForbiddenException("Product ID must be null");
        }

        return mapper.toModel(repository.save(mapper.toEntity(false, product)));
    }

    @Transactional
    @Override
    public Product update(Product product) {
        if (product.getId() == null) {
            throw new ResourceForbiddenException("Product ID must not be null");
        }

        ProductEntity foundProduct = repository.findById(product.getId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Product with id = %s not found".formatted(product.getId())));

        mapper.update(product, foundProduct);

        return mapper.toModel(repository.save(foundProduct));
    }

    @Transactional
    @Override
    public void updateDeleted(UUID id, Boolean deleted) {
        repository.updateDeleted(id, deleted);
    }
}
