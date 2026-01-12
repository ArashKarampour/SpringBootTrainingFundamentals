package com.example.store.services;

import com.example.store.entities.User;
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
}
