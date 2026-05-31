package com.example.tp.service;

import com.example.tp.model.Product;
import com.example.tp.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public Product createProduct(Product product) {
        return repository.save(product);
    }

    public Optional<Product> getProductById(Long id) {
        return repository.findById(id);
    }

    public Product updateProduct(Long id, Product updated) {
        Product existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Produit introuvable avec l'id : " + id));
        existing.setName(updated.getName());
        existing.setPrice(updated.getPrice());
        existing.setDescription(updated.getDescription());
        return repository.save(existing);
    }

    public void deleteProduct(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Produit introuvable avec l'id : " + id);
        }
        repository.deleteById(id);
    }
}
