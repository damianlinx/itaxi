package pl.itaxi.backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.itaxi.backend.dto.ProductDto;
import pl.itaxi.backend.mapper.ProductDtoMapper;
import pl.itaxi.backend.model.ProductEntity;
import pl.itaxi.backend.services.ProductService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAll() {
        return new ResponseEntity<>(ProductDtoMapper.toList(productService.getAll()),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getById(@PathVariable("id") long id) {
        Optional<ProductEntity> product = productService.getById(id);
        return product.map(value -> new ResponseEntity<>(ProductDtoMapper.toDto(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<ProductDto> create(@Valid @RequestBody ProductDto productDto) {
        return new ResponseEntity<>(ProductDtoMapper.toDto(
                productService.create(productDto)),
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable("id") long id, @Valid  @RequestBody ProductDto product) {
        return new ResponseEntity<>(ProductDtoMapper.toDto(productService.update(id, product)),
                HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") long id) {
        productService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
