package ro.tuc.ds2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ro.tuc.ds2020.dtos.DeviceDetailsDTO;
import ro.tuc.ds2020.entities.Device;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {

   // Optional<Device> findFirstByUserEmail(String userEmail);
    List<Device> findByUserEmail(String userEmail);

    @Query("SELECT d FROM Device d WHERE d.userEmail = :userEmail")
    Optional<Device> findAllByUserEmail(@Param("userEmail") String userEmail);

    @Modifying
    @Transactional
    @Query("DELETE FROM Device d WHERE d.userEmail = :userEmail")
    void deleteByUserEmail(@Param("userEmail") String userEmail);
    /**
     * Example: JPA generate Query by Field
     */
   // List<Device> findByDescription(String description);
    //list <ce returneaza> findByAgeAfter(int age)

    /**
     * Example: Write Custom Query
     */
//    @Query(value = "SELECT p " +
//            "FROM Device p " +
//            "WHERE p.description = :description " +
//            "AND p.MHEC >= 60  ")
//    Optional<Device> findSeniorsByDescription(@Param("description") String description);

}
