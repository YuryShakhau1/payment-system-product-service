package by.shakhau.ps.product.controller.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateProductRequest {

    @NotBlank(message = "Product name must not be blank")
    @Size(max = 50, message = "Product name must not exceed 50 characters")
    private String name;

    @NotBlank(message = "Description must not be blank")
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @NotNull(message = "Product price must not be null")
    @DecimalMin(
            value = "0.01",
            message = "Product price must be greater than zero")
    @Digits(
            integer = 19,
            fraction = 2,
            message = "Price must have up to 19 integer digits and 2 decimal places")
    private BigDecimal price;
}
