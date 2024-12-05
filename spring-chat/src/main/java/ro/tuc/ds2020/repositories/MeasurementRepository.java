package ro.tuc.ds2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ro.tuc.ds2020.entities.Measurement;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MeasurementRepository extends JpaRepository<Measurement, UUID> {

    @Query("SELECT m FROM Measurement m WHERE m.device_id = :device_id")
    List<Measurement> findByDevice_id(@Param("device_id") UUID device_id);
    List<Measurement> findByTimestamp(Date timestamp);
    @Query("SELECT m FROM Measurement m WHERE FUNCTION('DATE', m.timestamp) = :startDate")
    List<Measurement> findAllByDate(@Param("startDate") Date startDate);
    @Query("SELECT m FROM Measurement m WHERE m.device_id = :deviceId AND FUNCTION('DATE', m.timestamp) = :startDate")
    List<Measurement> findByDeviceAndDate(@Param("deviceId") UUID deviceId, @Param("startDate") Date startDate);

    @Query("SELECT m FROM Measurement m WHERE m.device_id = :deviceId AND m.timestamp = :timestamp")
    Optional<Measurement> findByDeviceIdAndTimestamp(@Param("deviceId") UUID deviceId, @Param("timestamp") Date timestamp);

    @Transactional
    @Modifying
    @Query("DELETE FROM Measurement m WHERE m.device_id = :deviceId AND m.timestamp = :timestamp")
    void deleteMeasurementByDeviceIdAndTimestamp(@Param("deviceId") UUID deviceId, @Param("timestamp") Date timestamp);

}
