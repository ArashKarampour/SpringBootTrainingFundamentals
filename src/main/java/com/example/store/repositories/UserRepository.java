package com.example.store.repositories;

import com.example.store.entities.User;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    // using EntityGraph to specify which related entities to fetch eagerly for this specific query (by default, related entities are fetched lazily(where the other side is many by default it's lazily loaded) to improve performance but we can override this behavior for specific queries using EntityGraph)
    @EntityGraph(attributePaths = {"tags", "addresses"}) //attributePaths = {"tags", "addresses.country"} to fetch the country of the address as well(nested related entity)
    Optional<User> findByEmail(String email); // optional because there might be no user with the given email (to avoid null pointer exception)
}
