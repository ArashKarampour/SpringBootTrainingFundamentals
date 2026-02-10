package com.example.store.services;

import com.example.store.entities.Address;
import com.example.store.entities.Category;
import com.example.store.entities.Product;
import com.example.store.entities.User;
import com.example.store.repositories.*;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
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
}
