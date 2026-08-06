package by.shakhau.ps.product.controller.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductListRequest {

    @Size(min = 1, max = 50)
    private List<CreateProductRequest> products;
}
