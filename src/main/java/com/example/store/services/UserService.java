package com.example.store.services;

import com.example.store.entities.Address;
import com.example.store.entities.Category;
import com.example.store.entities.Product;
import com.example.store.entities.User;
import com.example.store.repositories.*;
import com.example.store.repositories.specifications.ProductSpec;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@AllArgsConstructor // to generate constructor with all arguments (for dependency injection)
@Service("UserService")
public class UserService {
    private final UserRepository userRepository;
    private final EntityManager entityManager; // to manage(check) entity states
    private final ProfileRepository profileRepository;
    private final AddressRepository addressRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

//    public UserService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }

    @Transactional
    public void showEntityStates(){
        var user = new User("arash karampour", "email", "securepassword");

        if(entityManager.contains(user))
            System.out.println("User is in managed state (Persistent state)");
        else
            System.out.println("User is in Transient/Detached state");

        userRepository.save(user);

        if(entityManager.contains(user))
            System.out.println("User is in managed state (Persistent state)");
        else
            System.out.println("User is in Transient/Detached state");
    }

    @Transactional // to keep the session(persistent context/session) open while accessing related entities (to avoid LazyInitializationException)
    public void showRelatedEntities(){
        var profile = profileRepository.findById(2L).orElseThrow();
        System.out.println(profile.getUser().getName()); // accessing the user of the profile (will work because we are in a transaction with the Transactional annotation)
    }

    public void fetchAddresses(){
        var address = addressRepository.findById(1L).orElseThrow();
    }

    public void persistRelated(){
        var user = new User("name", "email", "password");
        var address = new Address( "city", "street", "zip");
        user.addAddress(address);

        userRepository.save(user); // because of CascadeType.PERSIST in User->Address relationship, address will be saved automatically
//        addressRepository.save(address); // this line is not necessary because of CascadeType.PERSIST in User class
    }

    @Transactional
    public void deleteRelated(){
        // remove user(parent) along with profile and addresses(children)
        userRepository.deleteById(2L); // because of CascadeType.Remove in User->Profile relationship, profile related to the user will be deleted automatically // also for User->Address relationship because of CascadeType.Remove the addresses related to the user will be deleted automatically (we are deleting the user here so other related entities should be deleted too)
        // remove address(child) from user(parent)
        var user = userRepository.findById(5L).orElseThrow();
        var address = user.getAddresses().getFirst(); // we need transactional annotation here to keep the session open while accessing addresses (because of lazy loading)
        user.removeAddress(address); // remove the address from the user's collection and set the user in the address to null -> address will not be deleted from the database if we do not set orphanRemoval to true in User->Address relationship
        userRepository.save(user);
    }

    @Transactional
    public void manageProducts(){
        // Creating a new category and add a product to it's products list and save them alltogether(we need to set CascadeType.Persist in the category entity for the product)
//        var category = new Category("category2");
//        var product = new Product();
//        product.setName("product1");
//        product.setPrice(BigDecimal.valueOf(25.0));
//        product.setCategory(category);
//        category.getProducts().add(product);

//        categoryRepository.save(category);

        // Retrieving existing category and updating it's list of products by adding a new product to it
//        var category = categoryRepository.findById(Byte.valueOf("3")).orElseThrow();
//        var product = new Product();
//        product.setName("product2");
//        product.setPrice(BigDecimal.valueOf(26.0));
//        product.setCategory(category);
//        category.getProducts().add(product); // we are updating (adding new product) the products list of the category so in Category we shoud set CascadeType.Merge for this to work else we get an error
//
//        categoryRepository.save(category);

        // adding products to a user's wishlist (for ManyToMany we don't need to set cascadetypes! see in User class wishlistProducts )
//        var user = userRepository.findById(1L).orElseThrow();
//        var products = productRepository.findAll();
//        products.forEach(user::addWishlistProduct); // see method reference use cases: https://chatgpt.com/share/6988abbc-1888-8002-9cdf-a5aa5fee7414

        // deleting a product should delete it from wishlist as well (so we had to add cascading in the database migration to solve the error)
        productRepository.deleteById(2L);

    }

    @Transactional // Transactional is required for modifying queries (update or delete) to keep the session open while executing the query and to manage the transaction properly (commit or rollback)
    public void updateProductPriceByCategory(){
        productRepository.updatePriceByCategory(BigDecimal.valueOf(30.0), (byte) 3);
    }

//    @Transactional
    public void fetchProducts(){
//        var category = categoryRepository.findById(Byte.valueOf("3")).orElseThrow();
        var products = productRepository.findByCategoryJPQL(new Category((byte)3)); // we are using the Category with id constructor to just have the id before saving the category to the database or not to retrieve the category from the database to reduce queries shown by hibernate in the console.
        products.forEach(System.out::println);
    }

    @Transactional
    public void fetchUser(String email){
        var user = userRepository.findByEmail(email).orElseThrow();
        System.out.println(user);
    }

    public void fetchAllUsers(){
        var users = userRepository.findAllUsersWithAddresses();
        users.forEach(u -> {
            System.out.println(u);
            System.out.println("Addresses:");
            u.getAddresses().forEach(System.out::println); // this will cause n+1 problem because for each user we are fetching its addresses in a separate query(n queries for n users's addresses) (because of lazy loading) but we can solve this problem by using EntityGraph in the repository method to fetch addresses eagerly for that specific query(fetch all addresses and users in one query) (see UserRepository findAllUsersWithAddresse method)
        });
    }

    @Transactional // we need transactional annotation here to keep the session open while accessing the products to manage the transaction properly while executing the stored procedure
    public void fetchProductsByProcedure(){
        var products = productRepository.findProductsByProcedure(BigDecimal.valueOf(20.0), BigDecimal.valueOf(30.0));
        products.forEach(System.out::println);
    }

    @Transactional
    public void fetchProfiles(){
        var profiles = profileRepository.findProfilesByLoyality(2);
        profiles.forEach(p -> {System.out.println(p.getEmail());});
    }

    @Transactional
    public void fetchLoyalUsers(){
        var users = userRepository.findLoyalUsers(2);
        users.forEach(u -> {System.out.println(u.getEmail());});
    }

    public void fetchProductsBySpecification(String name, BigDecimal minPrice, BigDecimal maxPrice){
//        Specification<Product> spec = Specification.where(null); // this doesn't work in this version of spring boot and spring data jpa because of some changes in the way specifications are handled in the newer versions, so we can use the below version instead which is a specification that does not filter anything (it is like a where 1=1 in SQL) and we can use it as a starting point to build our specification by adding more conditions to it using the and() method of the Specification interface (e.g. spec = spec.and(ProductSpec.hasName(name)) to add a condition for filtering products by name using the hasName specification from the ProductSpec class) and then we can pass this specification to the repository method that accepts a specification (e.g. productRepository.findAll(spec)) to get the filtered products based on the specified conditions in the specification.
        Specification<Product> spec = Specification.unrestricted(); // this is equivalent to the above version.
//        Specification<Product> spec = (root, query, cb) -> cb.conjunction(); // the above version with null didn't work in this version of spring boot and spring data jpa because of some changes in the way specifications are handled in the newer versions, so we can use this version instead which is a specification that does not filter anything (it is like a where 1=1 in SQL) and we can use it as a starting point to build our specification by adding more conditions to it using the and() method of the Specification interface (e.g. spec = spec.and(ProductSpec.hasName(name)) to add a condition for filtering products by name using the hasName specification from the ProductSpec class) and then we can pass this specification to the repository method that accepts a specification (e.g. productRepository.findAll(spec)) to get the filtered products based on the specified conditions in the specification.

        if (name != null) {
            spec = spec.and(ProductSpec.hasName(name));
        }
        if (minPrice != null) {
            spec = spec.and(ProductSpec.hasPriceGreaterThanOrEqualTo(minPrice));
        }
        if (maxPrice != null) {
            spec = spec.and(ProductSpec.hasPriceLessThanOrEqualTo(maxPrice));
        }

        var products = productRepository.findAll(spec);
        products.forEach(System.out::println);

    }
}
