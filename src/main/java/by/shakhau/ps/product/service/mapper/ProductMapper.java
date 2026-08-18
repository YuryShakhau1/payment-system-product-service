package by.shakhau.ps.product.service.mapper;

import by.shakhau.ps.product.repository.entity.ProductEntity;
import by.shakhau.ps.product.service.model.Product;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    @Mapping(target = "deleted", source = "deleted")
    ProductEntity toEntity(Boolean deleted, Product product);
    Product toModel(ProductEntity product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void update(Product source, @MappingTarget ProductEntity target);
}
