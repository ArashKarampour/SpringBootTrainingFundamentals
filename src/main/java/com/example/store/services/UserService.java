package com.example.store.services;

import com.example.store.entities.User;
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
}
