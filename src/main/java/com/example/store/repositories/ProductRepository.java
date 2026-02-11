package com.example.store.repositories;

import com.example.store.dtos.ProductSummary;
import com.example.store.dtos.ProductSummaryDTOClass;
import com.example.store.entities.Category;
import com.example.store.entities.Product;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {
    // Derived query method to find products:

    List<Product> findByName(String name); // select * from products where name = ?
//    List<Product> findByCategory(Category category); // select * from products where category_id = ?
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

    // Find products whose prices are in a given range and sort by name
//    List<Product> findByPriceBetweenOrderByName(BigDecimal min, BigDecimal max);

    // Writing custom queries using @Query annotation (JPQL or native SQL) for more complex queries that cannot be derived from method names
    @Query(value = "SELECT * FROM products p where p.price between :min and :max order by p.name", nativeQuery = true) // nativeQuery = true means that this is a native SQL query (not JPQL) and we have to use the table and column names as they are in the database (not the entity field names)
    List<Product> findProducts(@Param("min") BigDecimal min,@Param("max") BigDecimal max);

    // same query as above but using JPQL (Java Persistence Query Language) which is an object-oriented query language that uses the entity field names and class names instead of the table and column names in the database
    // JPQL is more portable and database-agnostic than native SQL because it is based on the entity model rather than the database schema, but it may not support all the features of the underlying database and may have performance implications for complex queries
    @Query("SELECT p FROM Product p where p.price between :min and :max order by p.name")
    List<Product> findProductsUsingJPQL(@Param("min") BigDecimal min, @Param("max") BigDecimal max);
    // you can also extract the jpql with query annotation using derived query and jpa buddy plugin: put the cursor on the method name and press alt + enter and then select "Extract query to @Query annotation and configure" and then you can see the generated jpql query in the @Query annotation

    @Query("SELECT count(p) FROM Product p where p.price between :min and :max") // select count(*) from products where price between ? and ?
    long countProducts(@Param("min") BigDecimal min, @Param("max") BigDecimal max);

    @Modifying // to indicate that this query is a modifying query (update or delete) and not a select query
    @Query("UPDATE Product p set p.price = :newPrice where p.category.id = :categoryId") // update products set price = ? where category_id = ?
    void updatePriceByCategory(BigDecimal newPrice, Byte categoryId);

    // Using Projections to select only specific fields from the database (instead of selecting the whole entity) for better performance when we only need a subset of the entity's data:

//    List<ProductSummary> findByCategory(Category category);
    // ProductSummary is a projection interface that defines getters for the fields we want to select from the database (e.g. name and id) and we can use it in the repository method return type to get only those fields instead of the whole Product entity(because in the Product entity we have getters for all the fields but in the ProductSummary interface we write getters only for the fields we want to select from the database) and these methods (getters) will be used from the Product entity to just select the name and id fields from the database and return them as a list of ProductSummary objects (which are not entities but just projections) and this is more efficient than selecting the whole Product entity when we only need a subset of the data (e.g. name and id) because it reduces the amount of data transferred from the database and also reduces the memory usage in the application

    // we could also use projections with a class if we need more logic in the projection (e.g. we want to calculate a field based on other fields or we want to have a constructor in the projection class to set the fields)
    // note for the class dto we need getters and a constructor that takes the fields we want to select from the database as parameters (e.g. name and id) (@AllArgsConstructor and @Getter annotations from lombok can be used for this).
    //List<ProductSummaryDTOClass> findByCategory(Category category);

    // we could also use projection with @Query and interface
    // we have to select individually the fields in the query. else all the fields will be loaded
//    @Query("select p.id, p.name from Product p where p.category = :category")
//    List<ProductSummary> findByCategoryJPQL(@Param("category") Category category);

    // we could also use projection with @Query and class but we need to use the constructor expression in JPQL(with full name from the top level package) to specify how to map the selected fields to the constructor of the projection class (e.g. new com.example.store.dtos.ProductSummaryDTOClass(p.id, p.name)) and we have to select individually the fields in the query. else all the fields will be loaded
    @Query("select new com.example.store.dtos.ProductSummaryDTOClass(p.id, p.name) from Product p where p.category = :category")
    List<ProductSummaryDTOClass> findByCategoryJPQL(@Param("category") Category category);

}
