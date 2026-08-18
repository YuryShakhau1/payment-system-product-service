package by.shakhau.ps.product.controller.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PatchProductRequest {

    private String name;
    private String description;
    private BigDecimal price;

    private Boolean deleted;
}
