package by.shakhau.ps.product.controller;

import by.shakhau.ps.product.controller.dto.mapper.ProductDtoMapper;
import by.shakhau.ps.product.controller.dto.request.CreateProductRequest;
import by.shakhau.ps.product.controller.dto.request.PatchProductRequest;
import by.shakhau.ps.product.controller.dto.request.ProductIdsRequest;
import by.shakhau.ps.product.controller.dto.request.ProductListRequest;
import by.shakhau.ps.product.controller.dto.response.ProductResponse;
import by.shakhau.ps.product.service.ProductService;
import by.shakhau.ps.product.service.model.Product;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductDtoMapper mapper;
    private final ProductService service;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ProductResponse>> findProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean deleted,
            @PageableDefault(
                    page = 0,
                    size = 20,
                    sort = "name",
                    direction = Sort.Direction.ASC)
            Pageable pageable) {
        return ResponseEntity.ok(service.findAll(name, deleted, pageable).map(mapper::toResponse));
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponse> findProduct(@PathVariable UUID id) {
        return ResponseEntity.ok(mapper.toResponse(service.findById(id)));
    }

    @PostMapping(value = "/filter", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductResponse>> findProducts(@RequestBody ProductIdsRequest request) {
        List<ProductResponse> products = service.findByIdIn(request.getIds()).stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(products);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid CreateProductRequest request) {
        Product product = service.create(mapper.toModel(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(product));
    }

    @PostMapping(value = "/list", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductResponse>> createProducts(@RequestBody @Valid ProductListRequest request) {
        List<Product> products = request.getProducts().stream()
                .map(mapper::toModel)
                .toList();
        List<ProductResponse> createProducts = service.create(products).stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.status(HttpStatus.CREATED).body(createProducts);
    }

    @PatchMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable UUID id,
            @RequestBody @Valid PatchProductRequest request) {
        Product product = service.update(mapper.toModel(id, request));
        return ResponseEntity.ok(mapper.toResponse(product));
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<Void> restoreProduct(@PathVariable UUID id) {
        service.updateDeleted(id, false);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        service.updateDeleted(id, true);
        return ResponseEntity.noContent().build();
    }
}
