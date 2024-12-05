package ro.tuc.ds2020.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.DeviceReferenceDTO;
import ro.tuc.ds2020.dtos.DeviceReferenceDetailsDTO;
import ro.tuc.ds2020.dtos.builders.DeviceReferenceBuilder;
import ro.tuc.ds2020.entities.DeviceReference;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import ro.tuc.ds2020.repositories.DeviceReferenceRepository;

@Service
public class DeviceReferenceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceReferenceService.class);
    private final DeviceReferenceRepository deviceReferenceRepository;

    @Autowired
    public DeviceReferenceService(DeviceReferenceRepository deviceReferenceRepository) {
        this.deviceReferenceRepository = deviceReferenceRepository;
    }

    public Optional<String> getUserEmailByDeviceId(UUID deviceId) {
        return deviceReferenceRepository.getUserEmailByDeviceId(deviceId);
    }

    public List<DeviceReferenceDTO> findDeviceReferences() {
        List<DeviceReference> DeviceReferenceList = deviceReferenceRepository.findAll();
        return DeviceReferenceList.stream()
                .map(DeviceReferenceBuilder::toDeviceReferenceDTO)
                .collect(Collectors.toList());
    }

    public DeviceReferenceDetailsDTO findDeviceReferenceById(UUID id) {
        Optional<DeviceReference> prosumerOptional = deviceReferenceRepository.findById(id);
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("DeviceReference with id {} was not found in db", id);
            throw new ResourceNotFoundException(DeviceReference.class.getSimpleName() + " with id: " + id);
        }
        return DeviceReferenceBuilder.toDeviceReferenceDetailsDTO(prosumerOptional.get());
    }

    public List<DeviceReferenceDetailsDTO> findAllDeviceReferenceDetails() {
        List<DeviceReference> deviceList = deviceReferenceRepository.findAll();
        return deviceList.stream()
                .map(DeviceReferenceBuilder::toDeviceReferenceDetailsDTO)
                .collect(Collectors.toList());
    }

    public UUID insertDevice(DeviceReferenceDetailsDTO deviceReferenceDTO) {
        DeviceReference deviceReference = new DeviceReference();
        deviceReference.setDevice_id(deviceReferenceDTO.getDevice_id());
        deviceReference.setUser_email(deviceReferenceDTO.getUser_email());
        deviceReference.setMhec(deviceReferenceDTO.getMhec());

        deviceReference = deviceReferenceRepository.save(deviceReference);

        return deviceReference.getId();
    }

    public void deleteDeviceVoid(UUID deviceId) {

        try {
            deviceReferenceRepository.deleteByDeviceId(deviceId);
        } catch (EmptyResultDataAccessException e) {
            System.out.println(" [!] No device found with ID: " + deviceId + ". Nothing to delete.");
        }
    }

    public boolean deleteDeviceBool(UUID deviceId) {

        Optional<DeviceReference> deviceReferenceOptional = deviceReferenceRepository.findByDeviceId(deviceId);
        try {
            deviceReferenceRepository.deleteByDeviceId(deviceId);
            System.out.println(" [*] Deleted device with ID: " + deviceId);
        } catch (EmptyResultDataAccessException e) {
            System.out.println(" [!] No device found with ID: " + deviceId + ". Nothing to delete.");
        }
        return false;
    }

    public Optional<DeviceReferenceDetailsDTO> updateDevice(UUID deviceId, DeviceReferenceDetailsDTO deviceDTO) {
        Optional<DeviceReference> deviceReferenceOptional = deviceReferenceRepository.findByDeviceId(deviceId);

        if (deviceReferenceOptional.isEmpty()) {
            LOGGER.info("Device with ID {} not found for update", deviceId);
            return Optional.empty();
        }

        DeviceReference deviceReference = deviceReferenceOptional.get();
        deviceReference.setUser_email(deviceDTO.getUser_email());
        deviceReference.setMhec(deviceDTO.getMhec());

        deviceReferenceRepository.save(deviceReference);
        return Optional.of(new DeviceReferenceDetailsDTO(deviceReference));
    }

    public int getMhecByDeviceId(UUID deviceId) {
        return deviceReferenceRepository.findMhecByDeviceId(deviceId)
                .orElseThrow(() -> new RuntimeException("Device not found or MHEC not set for device ID: " + deviceId));
    }

}
