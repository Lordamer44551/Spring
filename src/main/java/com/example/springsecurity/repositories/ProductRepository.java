package com.example.springsecurity.repositories;

import com.example.springsecurity.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    // Не чувствительный к регистру поиск всех товаров по части названия
    List<Product> findByTitleContainingIgnoreCase(String name);

    // Поиск по названию и фильтрация по диапазону цен
    @Query(value = "select * from product where ((lower(title) LIKE %?1%) or (lower(title) LIKE '?1%') or (lower(title) LIKE '%?1')) and (price >= ?2 and price <= ?3)", nativeQuery = true)
    List<Product> findByTitleAndPriceGreaterThanEqualAndPriceLessThanEqual(String title, float startPrice, float endPrice);

    // Поиск по названию и фильтрация по диапазону цен, а также сортировка по возрастанию цены
    @Query(value = "select * from product where ((lower(title) LIKE %?1%) or (lower(title) LIKE '?1%') or (lower(title) LIKE '%?1')) and (price >= ?2 and price <= ?3) order by price", nativeQuery = true)
    List<Product> findByTitleOrderByPriceAsc(String title, float startPrice, float endPrice);

    // Поиск по названию и фильтрация по диапазону цен, а также сортировка по убыванию цены
    @Query(value = "select * from product where ((lower(title) LIKE %?1%) or (lower(title) LIKE '?1%') or (lower(title) LIKE '%?1')) and (price >= ?2 and price <= ?3) order by price desc", nativeQuery = true)
    List<Product> findByTitleOrderByPriceDesc(String title, float startPrice, float endPrice);

    // Поиск по названию и фильтрация по диапазону цен и категории, а также сортировка по возрастанию цены
    @Query(value = "select * from product where category_id = ?4 and ((lower(title) LIKE %?1%) or (lower(title) LIKE '?1%') or (lower(title) LIKE '%?1')) and (price >= ?2 and price <= ?3) order by price asc", nativeQuery = true)
    List<Product> findByTitleAndCategoryOrderByPriceAsc(String title, float startPrice, float endPrice, int categoryId);

    // Поиск по названию и фильтрация по диапазону цен и категории, а также сортировка по убыванию цены
    @Query(value = "select * from product where category_id = ?4 and ((lower(title) LIKE %?1%) or (lower(title) LIKE '?1%') or (lower(title) LIKE '%?1')) and (price >= ?2 and price <= ?3) order by price desc", nativeQuery = true)
    List<Product> findByTitleAndCategoryOrderByPriceDesc(String title, float startPrice, float endPrice, int categoryId);
}
