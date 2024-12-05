package ro.tuc.ds2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.UserReference;

import java.util.Optional;
import java.util.UUID;

public interface UserReferenceRepository extends JpaRepository<UserReference, UUID> {

    Optional<UserReference> findByEmail(String email);
    Optional<UserReference> findUserReferenceById(UUID id);
    Optional<UserReference> findById(UUID id);

    @Modifying
    @Transactional
    @Query("DELETE FROM UserReference ur WHERE ur.email = :userEmail")
    void deleteByUserEmail(@Param("userEmail") String userEmail);
}
