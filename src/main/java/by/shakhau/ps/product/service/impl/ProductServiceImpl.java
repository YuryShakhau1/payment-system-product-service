package by.shakhau.ps.product.service.impl;

import by.shakhau.ps.product.repository.ProductRepository;
import by.shakhau.ps.product.repository.entity.ProductEntity;
import by.shakhau.ps.product.repository.specification.ProductSpecifications;
import by.shakhau.ps.product.service.ProductService;
import by.shakhau.ps.product.service.exception.ResourceForbiddenException;
import by.shakhau.ps.product.service.exception.ResourceNotFoundException;
import by.shakhau.ps.product.service.mapper.ProductMapper;
import by.shakhau.ps.product.service.model.Product;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper mapper;
    private final ProductRepository repository;

    @Override
    public List<Product> findByIdIn(List<UUID> ids) {
        return repository.findAllById(ids).stream()
                .map(mapper::toModel)
                .toList();
    }

    @Override
    public Page<Product> findAll(String name, Boolean deleted, Pageable pageable) {
        Specification<ProductEntity> specification = Specification.allOf(
                ProductSpecifications.hasName(name),
                ProductSpecifications.deleted(deleted));

        return repository.findAll(specification, pageable).map(mapper::toModel);
    }

    @Override
    public Product findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toModel)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with id = %s nof found".formatted(id)));
    }

    @Override
    public List<Product> create(List<Product> products) {
        List<ProductEntity> savedProducts = repository.saveAll(
                products.stream()
                        .map(p -> mapper.toEntity(false, p))
                        .toList());
        return savedProducts.stream().map(mapper::toModel).toList();
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
