package ro.tuc.ds2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ro.tuc.ds2020.entities.DeviceReference;
import ro.tuc.ds2020.entities.Measurement;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeviceReferenceRepository extends JpaRepository<DeviceReference, UUID> {

    @Query("SELECT d FROM DeviceReference d WHERE d.device_id = :deviceId")
    Optional<DeviceReference> findByDeviceId(UUID deviceId);

    @Modifying
    @Transactional
    @Query("DELETE FROM DeviceReference d WHERE d.device_id = :deviceId")
    void deleteByDeviceId(UUID deviceId);

    @Query("SELECT d.mhec FROM DeviceReference d WHERE d.device_id = :deviceId")
    Optional<Integer> findMhecByDeviceId(@Param("deviceId") UUID deviceId);

    @Query("SELECT u.user_email FROM DeviceReference u JOIN DeviceReference d ON u.id = d.user_email WHERE d.device_id = :deviceId")
    Optional<String> getUserEmailByDeviceId(@Param("deviceId") UUID deviceId);

}
