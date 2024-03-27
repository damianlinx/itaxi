package pl.itaxi.backend.mapper;

import pl.itaxi.backend.dto.ProductDto;
import pl.itaxi.backend.model.ProductEntity;

import java.util.List;
import java.util.stream.Collectors;

public class ProductDtoMapper {

    private ProductDtoMapper() {}

    public static List<ProductDto> toList(List<ProductEntity> products) {
        if (products == null) {
            return null;
        }
        return products.stream()
                .map(ProductDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    public static ProductDto toDto(ProductEntity product) {
        if (product == null) {
            return null;
        }
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }
}
