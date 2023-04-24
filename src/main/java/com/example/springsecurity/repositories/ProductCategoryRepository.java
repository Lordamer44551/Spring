package com.example.springsecurity.repositories;

import com.example.springsecurity.models.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
    com.example.springsecurity.models.ProductCategory findByName(String name);
}
