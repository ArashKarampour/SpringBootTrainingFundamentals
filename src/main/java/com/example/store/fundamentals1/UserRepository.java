package com.example.store.fundamentals1;

public interface UserRepository {
    void save(User user);
    User findByEmail(String email);
}
