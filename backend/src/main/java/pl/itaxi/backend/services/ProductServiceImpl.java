package pl.itaxi.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.itaxi.backend.dto.ProductDto;
import pl.itaxi.backend.model.ProductEntity;
import pl.itaxi.backend.repositories.ProductRepository;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ProductEntity> getAll() {
        return productRepository.findAll();
    }

    @Override
    public Optional<ProductEntity> getById(long id) {
        return productRepository.findById(id);
    }

    @Override
    public ProductEntity create(ProductDto productDto) {

        ProductEntity product = productRepository
                .save(ProductEntity.builder()
                        .name(productDto.getName())
                        .price(productDto.getPrice())
                        .build());
        return productRepository.save(product);
    }

    @Override
    public ProductEntity update(long id, ProductDto product) {

        Optional<ProductEntity> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            ProductEntity existingProduct = productOptional.get();
            existingProduct.setName(product.getName());
            existingProduct.setPrice(product.getPrice());
            return productRepository.save(existingProduct);
        } else {
            throw new RuntimeException("Product not found with id: " + id);
        }
    }

    @Override
    public void delete(long id) {
        productRepository.deleteById(id);
    }
}