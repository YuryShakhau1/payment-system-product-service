package by.shakhau.ps.product.service.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(of = { "name" }, callSuper = false)
public class Product {

    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private Boolean deleted;
}
