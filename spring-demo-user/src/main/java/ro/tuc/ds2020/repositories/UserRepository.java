package ro.tuc.ds2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.tuc.ds2020.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Example: JPA generate Query by Field
     */
    List<User> findByName(String name);

    /**
     * Example: Write Custom Query
     */
//    @Query(value = "SELECT p " +
//            "FROM User p " +
//            "WHERE p.name = :name " +
//            "AND p.age >= 60  ")
//    Optional<User> findSeniorsByName(@Param("name") String name);

    /**
     * Example: Custom query to find users older than a specific age with the given name
     */
//    @Query("SELECT u FROM User u WHERE u.name = :name AND u.isAdmin >= :age")
//    List<User> findUsersByNameAndMinimumAge(@Param("name") String name, @Param("age") int age);

    /**
     * Find users by email
     */
    Optional<User> findByEmail(String email);

    /**
     * Custom Query Example: Find all users whose age falls within a specified range
     */
//    @Query("SELECT u FROM User u WHERE u.isAdmin BETWEEN :minAge AND :maxAge")
//    List<User> findUsersWithinAgeRange(@Param("minAge") int minAge, @Param("maxAge") int maxAge);
}
