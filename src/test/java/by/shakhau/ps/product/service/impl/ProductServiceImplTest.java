package by.shakhau.ps.product.service.impl;

import by.shakhau.ps.product.repository.ProductRepository;
import by.shakhau.ps.product.repository.entity.ProductEntity;
import by.shakhau.ps.product.service.exception.ResourceForbiddenException;
import by.shakhau.ps.product.service.exception.ResourceNotFoundException;
import by.shakhau.ps.product.service.mapper.ProductMapper;
import by.shakhau.ps.product.service.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductMapper mapper;

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductServiceImpl service;

    @Test
    void shouldReturnAllProductsWhenNameIsNull() {
        Pageable pageable = PageRequest.of(0, 20);
        var entity = new ProductEntity();
        var product = new Product();
        var entityPage = new PageImpl<>(List.of(entity));

        when(repository.findAll(pageable)).thenReturn(entityPage);
        when(mapper.toModel(entity)).thenReturn(product);

        Page<Product> result = service.findAll(null, pageable);

        assertThat(result.getContent()).containsExactly(product);
        verify(repository).findAll(pageable);
        verify(repository, never()).findByNameStartingWith(anyString(), any(Pageable.class));
        verify(mapper).toModel(entity);
    }

    @Test
    void shouldReturnProductsByNameWhenNameIsProvided() {
        Pageable pageable = PageRequest.of(0, 20);
        var entity = new ProductEntity();
        var product = new Product();
        var entityPage = new PageImpl<>(List.of(entity));

        when(repository.findByNameStartingWith("phone", pageable)).thenReturn(entityPage);
        when(mapper.toModel(entity)).thenReturn(product);

        Page<Product> result = service.findAll("phone", pageable);


        assertThat(result.getContent()).containsExactly(product);
        verify(repository).findByNameStartingWith("phone", pageable);
        verify(repository, never()).findAll(any(Pageable.class));
    }

    @Test
<<<<<<< HEAD
    void shouldReturnAllProductsWhenProductsExist() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        List<UUID> ids = List.of(id1, id2);

        var entity1 = new ProductEntity();
        var entity2 = new ProductEntity();
        entity1.setName("product1");
        entity2.setName("product2");
        var product1 = new Product();
        var product2 = new Product();

        when(repository.findAllById(ids)).thenReturn(List.of(entity1, entity2));
        when(mapper.toModel(entity1)).thenReturn(product1);
        when(mapper.toModel(entity2)).thenReturn(product2);

        List<Product> results = service.findByIdIn(ids);

        assertThat(results).hasSize(2);
        Product result1 = results.get(0);
        Product result2 = results.get(1);
        assertThat(result1).isEqualTo(product1);
        assertThat(result2).isEqualTo(product2);
        verify(repository).findAllById(ids);
        verify(mapper).toModel(entity1);
        verify(mapper).toModel(entity2);
    }

    @Test
=======
>>>>>>> c5a7ab9 (Test implemented)
    void shouldReturnProductWhenProductExists() {
        UUID id = UUID.randomUUID();

        var entity = new ProductEntity();
        var product = new Product();

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toModel(entity)).thenReturn(product);

        Product result = service.findById(id);

        assertThat(result).isEqualTo(product);
        verify(repository).findById(id);
        verify(mapper).toModel(entity);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenProductDoesNotExist() {
        UUID id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(id.toString());

        verify(repository).findById(id);

        verifyNoInteractions(mapper);
    }

    @Test
    void shouldCreateProductWhenProductIdIsNull() {
        var product = new Product();
        var entity = new ProductEntity();
        var savedEntity = new ProductEntity();
        var savedProduct = new Product();

        when(mapper.toEntity(false, product)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(savedEntity);
        when(mapper.toModel(savedEntity)).thenReturn(savedProduct);

        Product result = service.create(product);

        assertThat(result).isEqualTo(savedProduct);
        verify(mapper).toEntity(false, product);
        verify(repository).save(entity);
        verify(mapper).toModel(savedEntity);
    }

    @Test
    void shouldThrowResourceForbiddenExceptionWhenCreatingProductWithId() {
        var product = new Product();
        product.setId(UUID.randomUUID());

        assertThatThrownBy(() -> service.create(product))
                .isInstanceOf(ResourceForbiddenException.class)
                .hasMessage("Product ID must be null");

        verifyNoInteractions(repository);
        verifyNoInteractions(mapper);
    }

    @Test
    void shouldUpdateProductWhenProductExists() {
        UUID id = UUID.randomUUID();

        var product = new Product();
        product.setId(id);

        var entity = new ProductEntity();
        var updatedProduct = new Product();

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toModel(entity)).thenReturn(updatedProduct);

        Product result = service.update(product);

        assertThat(result).isEqualTo(updatedProduct);
        verify(repository).findById(id);
        verify(mapper).update(product, entity);
        verify(repository).save(entity);
        verify(mapper).toModel(entity);
    }

    @Test
    void shouldThrowResourceForbiddenExceptionWhenUpdatingProductWithoutId() {
        var product = new Product();

        assertThatThrownBy(() -> service.update(product))
                .isInstanceOf(ResourceForbiddenException.class)
                .hasMessage("Product ID must not be null");

        verifyNoInteractions(repository);
        verifyNoInteractions(mapper);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUpdatingNonExistingProduct() {
        UUID id = UUID.randomUUID();
        var product = new Product();
        product.setId(id);

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(product))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(id.toString());

        verify(repository).findById(id);
        verify(repository, never()).save(any());
    }

    @Test
    void shouldUpdateDeletedStatusWhenProductIdProvided() {
        UUID id = UUID.randomUUID();

        service.updateDeleted(id, true);

        verify(repository).updateDeleted(id, true);
    }
}
