package pl.itaxi.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.itaxi.backend.dto.ProductDto;
import pl.itaxi.backend.model.ProductEntity;
import pl.itaxi.backend.repositories.ProductRepository;
import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    public void cleanup() {
        productRepository.deleteAll();
    }

    @Test
    public void getAll_success() throws Exception {

        ProductEntity product = new ProductEntity();
        product.setName("Test Name");
        product.setPrice(BigDecimal.TEN);
        productRepository.save(product);
        mockMvc.perform(MockMvcRequestBuilders.get("/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    public void create_success() throws Exception {

        ProductEntity product = new ProductEntity();
        product.setName("Test Name");
        product.setPrice(BigDecimal.TEN);

        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                        .content(asJsonString(product))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    public void create_fail() throws Exception {
        ProductDto product = ProductDto.builder()
                .name(null)
                .price(BigDecimal.TEN)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                        .content(asJsonString(product))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Name is required"));
    }

    @Test
    public void getById_success() throws Exception {

        ProductEntity product = new ProductEntity();
        product.setName("Test Name");
        product.setPrice(BigDecimal.TEN);
        ProductEntity savedProduct = productRepository.save(product);

        mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", savedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Name"));
    }

    @Test
    public void getById_notFound() throws Exception {

        ProductRepository productRepositoryMock = mock(ProductRepository.class);
        when(productRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void update_success() throws Exception {

        ProductEntity product = new ProductEntity();
        product.setName("Test Name");
        product.setPrice(BigDecimal.TEN);
        ProductEntity savedProduct = productRepository.save(product);

        savedProduct.setName("Updated Test Name");

        mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", savedProduct.getId())
                        .content(asJsonString(savedProduct))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Test Name"));
    }

    @Test
    public void delete_success() throws Exception {

        ProductEntity product = new ProductEntity();
        product.setName("Test Name");
        product.setPrice(BigDecimal.TEN);
        ProductEntity savedProduct = productRepository.save(product);

        mockMvc.perform(MockMvcRequestBuilders.delete("/products/{id}", savedProduct.getId()))
                .andExpect(status().isNoContent());
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}