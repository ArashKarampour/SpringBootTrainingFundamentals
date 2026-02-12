package com.example.store.repositories;

import com.example.store.dtos.UserSummary;
import com.example.store.entities.User;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    // using EntityGraph to specify which related entities to fetch eagerly for this specific query (by default, related entities are fetched lazily(where the other side is many by default it's lazily loaded) to improve performance but we can override this behavior for specific queries using EntityGraph)
    @EntityGraph(attributePaths = {"tags", "addresses"}) //attributePaths = {"tags", "addresses.country"} to fetch the country of the address as well(nested related entity)
    Optional<User> findByEmail(String email); // optional because there might be no user with the given email (to avoid null pointer exception)

    @EntityGraph(attributePaths = "addresses") // fetching addresses eagerly for this query to avoid n+1 problem when accessing addresses of users
    @Query("SELECT u FROM User u")
    List<User> findAllUsersWithAddresses();

    @Query("select u.id as id, u.email as email from User u where u.profile.loyaltyPoints > :loyaltyPoint order by u.email") // note we need to use the alias with as + the name in the user's fields of UserSummary interface in order for the query to get converted to the interface values we have defined, else the generated object has some name that will not be bind to UserSummary fields of the interface correctly and we get null as the result for the Profile entity to access its fields and the related user's email field. also we need to specify the return type of the query (UserSummary) which is a projection interface that defines the fields we want to fetch from the database (id and email in this case)
    List<UserSummary> findLoyalUsers(@Param("loyaltyPoint") Integer loyaltyPoint);
}
