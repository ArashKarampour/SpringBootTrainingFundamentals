package com.example.store.repositories;

import com.example.store.entities.Category;
import com.example.store.entities.Product;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {
    // Derived query method to find products:

    List<Product> findByName(String name); // select * from products where name = ?
    List<Product> findByCategory(Category category); // select * from products where category_id = ?
    List<Product> findByCategoryAndName(Category category, String name); // select * from products where category_id = ? and name = ?
    List<Product> findByNameLike(String pattern); // select * from products where name like ?
    List<Product> findByNameNotLike(String pattern); // select * from products where name not like ?
    List<Product> findByNameContaining(String namePart); // select * from products where name like %?%
    List<Product> findByNameStartingWith(String prefix); // select * from products where name like ?%
    List<Product> findByNameEndingWith(String suffix); // select * from products where name like %?
    List<Product> findByNameEndingWithIgnoreCase(String suffix); // select * from products where lower(name) like lower(%?)
    List<Product> findByNameLikeOrNameLike(String pattern1, String pattern2); // select * from products where name like ? or name like ?

    // Numbers
    List<Product> findByPrice(BigDecimal price); // select * from products where price = ?
    List<Product> findByPriceGreaterThanEqual(BigDecimal price); // select * from products where price >= ?
    List<Product> findByPriceLessThanEqual(BigDecimal price); // select * from products where price <= ?
    List<Product> findByPriceBetween(BigDecimal min, BigDecimal max); // select * from products where price between ? and ?

    // Null
    List<Product> findByPriceNull(); // select * from products where description is null
    List<Product> findByPriceNotNull(); // select * from products where description is not null

    // Multiple conditions
    List<Product> findByPriceNullAndNameNotNull(); // select * from products where description is null and name is not null

    // Sort (OrderBy)
    List<Product> findByNameOrderByIdAsc(String name); // select * from products where name = ? order by id asc

    // Limit(Top/First)
    List<Product> findTop5ByNameLikeOrderByPriceDesc(String pattern); // select * from products where name like ? order by price desc limit 5

}
