package com.example.springsecurity.services;

import com.example.springsecurity.models.Product;
import com.example.springsecurity.models.ProductCategory;
import com.example.springsecurity.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Метод позволяет получить список всех товаров
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    // Метод позволяет вернуть товар по его id
    public Product getProductById(int id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        return optionalProduct.orElse(null);
    }

    public List<Product> getProductsByNamePart(String namePart) {
        return productRepository.findByTitleContainingIgnoreCase(namePart);
    }

    public List<Product> getFilteredProductsByTitle(String title, float startPrice, float endPrice) {
        return productRepository.findByTitleAndPriceGreaterThanEqualAndPriceLessThanEqual(title, startPrice, endPrice);
    }

    public List<Product> getFilteredAndSortedProductsByTitle(String title, float startPrice, float endPrice, String mode) {
        if (mode.equals("desc")) {
            return productRepository.findByTitleOrderByPriceDesc(title, startPrice, endPrice);
        } else {
            return productRepository.findByTitleOrderByPriceAsc(title, startPrice, endPrice);
        }
    }

    public List<Product> getFilteredAndSortedProductsByTitle(String title, float startPrice, float endPrice, int categoryId, String mode) {
        if (mode.equals("desc")) {
            return productRepository.findByTitleAndCategoryOrderByPriceDesc(title, startPrice, endPrice, categoryId);
        } else {
            return productRepository.findByTitleAndCategoryOrderByPriceAsc(title, startPrice, endPrice, categoryId);
        }
    }

    // Метод позволяет сохранить товар
    @Transactional
    public void saveProduct(Product product, ProductCategory category){
        product.setCategory(category);
        productRepository.save(product);
    }

    // Метод обновляет данные о товаре
    @Transactional
    public void updateProduct(int id, Product product) {
        product.setId(id);
        productRepository.save(product);
    }

    // Метод позволяет удалить товар по id
    @Transactional
    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }
}
