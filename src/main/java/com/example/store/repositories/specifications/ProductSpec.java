package com.example.store.repositories.specifications;

import com.example.store.entities.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpec {
    public static Specification<Product> hasName(String name){
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" +  name + "%"); // select * from products where name like ?. this is a specification that can be used to filter products by name using the like operator by a lambda expression that takes the root, query, and criteriaBuilder as parameters and returns a Predicate that represents the condition for filtering products by name using the like operator. The root.get("name") is used to access the name field of the Product entity and the criteriaBuilder.like() method is used to create a Predicate that checks if the name field matches the given name pattern.
    }

    public static Specification<Product> hasPriceGreaterThanOrEqualTo(BigDecimal price){
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("price"), price); // select * from products where price >= ?. this is a specification that can be used to filter products by price using the greater than or equal to operator by a lambda expression that takes the root, query, and criteriaBuilder as parameters and returns a Predicate that represents the condition for filtering products by price using the greater than or equal to operator. The root.get("price") is used to access the price field of the Product entity and the criteriaBuilder.greaterThanOrEqualTo() method is used to create a Predicate that checks if the price field is greater than or equal to the given price.
    }

    public static Specification<Product> hasPriceLessThanOrEqualTo(BigDecimal price){
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("price"), price); // select * from products where price <= ?. this is a specification that can be used to filter products by price using the less than or equal to operator by a lambda expression that takes the root, query, and criteriaBuilder as parameters and returns a Predicate that represents the condition for filtering products by price using the less than or equal to operator. The root.get("price") is used to access the price field of the Product entity and the criteriaBuilder.lessThanOrEqualTo() method is used to create a Predicate that checks if the price field is less than or equal to the given price.
    }
}
