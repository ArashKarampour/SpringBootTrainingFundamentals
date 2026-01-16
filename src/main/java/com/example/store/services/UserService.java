package com.example.store.services;

import com.example.store.entities.Address;
import com.example.store.entities.User;
import com.example.store.repositories.AddressRepository;
import com.example.store.repositories.ProfileRepository;
import com.example.store.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor // to generate constructor with all arguments (for dependency injection)
@Service("UserService")
public class UserService {
    private final UserRepository userRepository;
    private final EntityManager entityManager; // to manage(check) entity states
    private final ProfileRepository profileRepository;
    private final AddressRepository addressRepository;

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
}
