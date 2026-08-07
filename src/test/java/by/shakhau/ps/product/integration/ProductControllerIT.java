package by.shakhau.ps.product.integration;

import by.shakhau.ps.product.controller.dto.request.ProductIdsRequest;
import by.shakhau.ps.product.controller.dto.request.ProductRequest;
import by.shakhau.ps.product.repository.ProductRepository;
import by.shakhau.ps.product.repository.entity.ProductEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository repository;

    @Test
    void shouldCreateProductWhenRequestIsValid() throws Exception {
        var request = new ProductRequest();
        request.setName("iPhone");
        request.setDescription("Phone");
        request.setPrice(new BigDecimal("999.99"));

        mockMvc.perform(post("/products")
                        .header(AUTHORIZATION, AUTHORIZATION_HEADER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("iPhone"))
                .andExpect(jsonPath("$.price").value(999.99));

        List<ProductEntity> products = repository.findAll();

        assertThat(products).hasSize(1);
        assertThat(products.getFirst().getName()).isEqualTo("iPhone");
    }

    @Test
    void shouldReturnProductsByManyIdsWhenProductsExist() throws Exception {
        var iphone = new ProductEntity();
        iphone.setName("iPhone");
        iphone.setDescription("Phone");
        iphone.setPrice(BigDecimal.valueOf(1000));
        iphone.setDeleted(false);

        var samsung = new ProductEntity();
        samsung.setName("Samsung");
        samsung.setDescription("Phone");
        samsung.setPrice(BigDecimal.valueOf(700));
        samsung.setDeleted(false);

        repository.save(iphone);
        repository.save(samsung);

        mockMvc.perform(post("/products/filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new ProductIdsRequest(List.of(iphone.getId(), samsung.getId())))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("iPhone"))
                .andExpect(jsonPath("$[1].name").value("Samsung"));
    }

    @Test
    void shouldReturnProductsWhenProductsExist() throws Exception {
        var product = new ProductEntity();
        product.setName("Samsung");
        product.setDescription("Phone");
        product.setPrice(new BigDecimal("500"));
        product.setDeleted(false);

        repository.save(product);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].name").value("Samsung"));
    }

    @Test
    void shouldReturnProductsByNameWhenNameProvided() throws Exception {
        var iphone = new ProductEntity();
        iphone.setName("iPhone");
        iphone.setDescription("Phone");
        iphone.setPrice(BigDecimal.valueOf(1000));
        iphone.setDeleted(false);

        var samsung = new ProductEntity();
        samsung.setName("Samsung");
        samsung.setDescription("Phone");
        samsung.setPrice(BigDecimal.valueOf(700));
        samsung.setDeleted(false);

        repository.saveAll(List.of(iphone, samsung));

        mockMvc.perform(get("/products")
                        .param("name", "iPh"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value("iPhone"));
    }

    @Test
    void shouldReturnProductWhenProductExists() throws Exception {
        var product = new ProductEntity();
        product.setName("MacBook");
        product.setDescription("Laptop");
        product.setPrice(BigDecimal.valueOf(2000));
        product.setDeleted(false);

        ProductEntity saved = repository.save(product);

        mockMvc.perform(get("/products/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name")
                        .value("MacBook"));
    }

    @Test
    void shouldReturnNotFoundWhenProductDoesNotExist() throws Exception {
        UUID id = UUID.randomUUID();
        mockMvc.perform(get("/products/{id}", id)).andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateProductWhenProductExists() throws Exception {
        var product = new ProductEntity();
        product.setName("Old name");
        product.setDescription("Old");
        product.setPrice(BigDecimal.valueOf(100));
        product.setDeleted(false);

        ProductEntity saved = repository.save(product);

        var request = new ProductRequest();
        request.setName("New name");
        request.setDescription("New");
        request.setPrice(BigDecimal.valueOf(200));

        mockMvc.perform(patch("/products/{id}", saved.getId())
                        .header(AUTHORIZATION, AUTHORIZATION_HEADER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name")
                        .value("New name"));

        ProductEntity updated = repository.findById(saved.getId()).orElseThrow();

        assertThat(updated.getName()).isEqualTo("New name");
    }

    @Test
    void shouldDeleteProductWhenProductExists() throws Exception {
        var product = new ProductEntity();
        product.setName("iPhone");
        product.setDescription("Phone to delete");
        product.setPrice(BigDecimal.valueOf(100));
        product.setDeleted(false);

        ProductEntity saved = repository.save(product);

        mockMvc.perform(delete("/products/{id}", saved.getId())
                        .header(AUTHORIZATION, AUTHORIZATION_HEADER))
                .andExpect(status().isNoContent());

        ProductEntity deleted = repository.findById(saved.getId()).orElseThrow();

        assertThat(deleted.getDeleted()).isTrue();
    }

    @Test
    void shouldRestoreProductWhenProductWasDeleted() throws Exception {
        var product = new ProductEntity();
        product.setName("iPhone");
        product.setDescription("Phone to restore");
        product.setPrice(BigDecimal.valueOf(100));
        product.setDeleted(true);

        ProductEntity saved = repository.save(product);

        mockMvc.perform(patch("/products/{id}/restore", saved.getId())
                        .header(AUTHORIZATION, AUTHORIZATION_HEADER))
                .andExpect(status().isNoContent());

        ProductEntity restored = repository.findById(saved.getId()).orElseThrow();

        assertThat(restored.getDeleted()).isFalse();
    }

    @Test
    void shouldReturnBadRequestWhenProductRequestIsInvalid() throws Exception {
        var request = new ProductRequest();

        request.setName("");
        request.setPrice(BigDecimal.ZERO);

        mockMvc.perform(post("/products")
                        .header(AUTHORIZATION, AUTHORIZATION_HEADER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}