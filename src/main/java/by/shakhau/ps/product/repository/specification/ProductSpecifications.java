package by.shakhau.ps.product.repository.specification;

import by.shakhau.ps.product.repository.entity.ProductEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductSpecifications {

    public static Specification<ProductEntity> hasName(String name) {
        return (root, query, cb) ->
                Optional.ofNullable(name)
                        .map(n -> cb.like(root.get("name"), name + "%"))
                        .orElse(null);
    }

    public static Specification<ProductEntity> deleted(Boolean deleted) {
        return (root, query, cb) ->
                Optional.ofNullable(deleted)
                        .map(d -> cb.equal(root.get("deleted"), d))
                        .orElse(null);
    }
}
