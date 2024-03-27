package pl.itaxi.backend.services;

import pl.itaxi.backend.dto.ProductDto;
import pl.itaxi.backend.model.ProductEntity;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<ProductEntity> getAll();
    Optional<ProductEntity> getById(long id);
    ProductEntity create(ProductDto product);
    ProductEntity update(long id, ProductDto product);
    void delete(long id);
}