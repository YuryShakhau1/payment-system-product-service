package by.shakhau.ps.product.controller.dto.mapper;

import by.shakhau.ps.product.controller.dto.request.CreateProductRequest;
import by.shakhau.ps.product.controller.dto.request.PatchProductRequest;
import by.shakhau.ps.product.controller.dto.response.ProductResponse;
import by.shakhau.ps.product.service.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.UUID;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductDtoMapper {

    ProductResponse toResponse(Product product);
    Product toModel(CreateProductRequest request);
    Product toModel(UUID id, PatchProductRequest request);
}
