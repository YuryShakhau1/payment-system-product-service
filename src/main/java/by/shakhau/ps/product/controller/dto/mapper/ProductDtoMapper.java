package by.shakhau.ps.product.controller.dto.mapper;

import by.shakhau.ps.product.controller.dto.request.ProductRequest;
import by.shakhau.ps.product.controller.dto.response.ProductResponse;
import by.shakhau.ps.product.service.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.UUID;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductDtoMapper {

    ProductResponse toResponse(Product product);
    Product toModel(ProductRequest request);
    Product toModel(UUID id, ProductRequest request);
}
