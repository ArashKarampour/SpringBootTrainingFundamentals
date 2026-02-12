package com.example.store.repositories;

import com.example.store.dtos.UserSummary;
import com.example.store.entities.Profile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProfileRepository extends CrudRepository<Profile, Long> {
    // 9.7 Exercise
    @EntityGraph(attributePaths = "user") // to fetch the user data along with the profile data in a single query (to avoid N+1 problem). another way for the user is to set the fetch type to eager(for OneToOne is the default mode) in the Profile entity but it is not recommended because it will always load the user data even when we don't need it.
    List<Profile> findProfileByLoyaltyPointsGreaterThanOrderByUserEmail(Integer loyaltyPoint); // this method will fetch all profiles with loyalty points greater than the specified value and order them by the user's email (note that we are ordering by a field in the related entity (user) which is possible because of the @EntityGraph annotation that fetches the user data along with the profile data)

    // 9.7 Exercise with @EntityGraph and @Query annotations
    @Query("select p.id as id, p.user.email as email from Profile p where p.loyaltyPoints > :loyaltyPoint order by p.user.email") // note we need to use the alias with as + the name in the user's fields of UserSummary interface in order for the query to get converted to the interface values we have defined, else the generated object has some name that will not be bind to UserSummary fields of the interface correctly and we get null as the result for the Profile entity to access its fields and the related user's email field. also we need to specify the return type of the query (UserSummary) which is a projection interface that defines the fields we want to fetch from the database (id and email in this case)
    @EntityGraph(attributePaths = "user")
    List<UserSummary> findProfilesByLoyality(@Param("loyaltyPoint") Integer loyaltyPoint);
}
