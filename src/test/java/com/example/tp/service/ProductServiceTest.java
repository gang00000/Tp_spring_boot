package com.example.tp.service;

import com.example.tp.model.Product;
import com.example.tp.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests du ProductService")
class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product(1L, "Laptop", 999.99, "Un ordinateur portable");
    }

    @Test
    @DisplayName("getAllProducts() retourne la liste complète")
    void getAllProducts_shouldReturnAllProducts() {
        when(repository.findAll()).thenReturn(Arrays.asList(product, new Product(2L, "Souris", 29.99, "Souris optique")));

        List<Product> result = productService.getAllProducts();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Laptop");
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("createProduct() sauvegarde et retourne le produit")
    void createProduct_shouldSaveAndReturnProduct() {
        when(repository.save(any(Product.class))).thenReturn(product);

        Product result = productService.createProduct(product);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Laptop");
        assertThat(result.getPrice()).isEqualTo(999.99);
        verify(repository, times(1)).save(product);
    }

    @Test
    @DisplayName("getProductById() retourne le produit s'il existe")
    void getProductById_whenExists_shouldReturnProduct() {
        when(repository.findById(1L)).thenReturn(Optional.of(product));

        Optional<Product> result = productService.getProductById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("getProductById() retourne vide si inexistant")
    void getProductById_whenNotExists_shouldReturnEmpty() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Optional<Product> result = productService.getProductById(99L);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("updateProduct() modifie et retourne le produit mis à jour")
    void updateProduct_shouldUpdateAndReturn() {
        Product updated = new Product(null, "Laptop Pro", 1299.99, "Nouveau modèle");
        when(repository.findById(1L)).thenReturn(Optional.of(product));
        when(repository.save(any(Product.class))).thenReturn(product);

        Product result = productService.updateProduct(1L, updated);

        verify(repository, times(1)).save(any(Product.class));
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("updateProduct() lève EntityNotFoundException si produit inexistant")
    void updateProduct_whenNotExists_shouldThrowException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.updateProduct(99L, product))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessageContaining("99");
    }

    @Test
    @DisplayName("deleteProduct() supprime le produit existant")
    void deleteProduct_whenExists_shouldDelete() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        productService.deleteProduct(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("deleteProduct() lève EntityNotFoundException si produit inexistant")
    void deleteProduct_whenNotExists_shouldThrowException() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> productService.deleteProduct(99L))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessageContaining("99");
        verify(repository, never()).deleteById(any());
    }
}
