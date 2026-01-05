package com.example.store.fundamentals1;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final Map<String, User> users = new HashMap<>();
    @Override
    public void save(User user){
        System.out.println("Saving a new user " + user);
        this.users.put(user.getEmail(), user);
    }

    @Override
    public User findByEmail(String email){
        return this.users.getOrDefault(email, null);
    }
}
