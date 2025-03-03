package com.meli.challenge.products.mapper;

import com.meli.challenge.products.model.dto.DTOCreateProduct;
import com.meli.challenge.products.model.dto.DTOProduct;
import com.meli.challenge.products.model.entity.Category;
import com.meli.challenge.products.model.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "id", ignore = true)
    Product toEntity(DTOCreateProduct dtoProduct);

    @Mapping(target = "categories", source = "categories", qualifiedByName = "mapCategoriesToNames")
    DTOProduct toDTO(Product product);

    @Mapping(target = "categories", ignore = true)
    void updateProductFromDTO(DTOProduct dtoProduct, @MappingTarget Product product);

    @Named("mapCategoriesToNames")
    default List<String> mapCategoriesToNames(Set<Category> categories) {
        return categories == null ? List.of() : categories.stream()
                .map(Category::getName)
                .toList();
    }

}

