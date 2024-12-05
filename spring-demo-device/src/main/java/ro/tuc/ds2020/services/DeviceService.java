package ro.tuc.ds2020.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.controllers.UserReferenceController;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.DeviceDetailsDTO;
import ro.tuc.ds2020.dtos.DeviceUpdateDTO;
import ro.tuc.ds2020.dtos.UserReferenceDTO;
import ro.tuc.ds2020.dtos.builders.DeviceBuilder;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.UserReference;
import ro.tuc.ds2020.repositories.DeviceRepository;
import ro.tuc.ds2020.repositories.UserReferenceRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeviceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);
    private final DeviceRepository deviceRepository;
    private final UserReferenceController userReferenceController;
    private final UserReferenceRepository userReferenceRepository;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository , UserReferenceController userReferenceController, UserReferenceRepository userReferenceRepository) {
        this.deviceRepository = deviceRepository;
        this.userReferenceController = userReferenceController;
        this.userReferenceRepository = userReferenceRepository;
    }

    public List<DeviceDTO> findDevices() {
        List<Device> deviceList = deviceRepository.findAll();
        return deviceList.stream()
                .map(DeviceBuilder::toPersonDTO)
                .collect(Collectors.toList());
    }

    public DeviceDetailsDTO findDeviceById(UUID id) {
        Optional<Device> prosumerOptional = deviceRepository.findById(id);
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("Description with id {} was not found in db", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }
        return DeviceBuilder.toDeviceDetailsDTO(prosumerOptional.get());
    }
//aici se salveaza in BD
    public UUID insert(DeviceDetailsDTO deviceDTO) {
        Device device = DeviceBuilder.toEntity(deviceDTO);
        device = deviceRepository.save(device);
        LOGGER.debug("Description with id {} was inserted in db", device.getId());
        return device.getId();
    }

    // update and delete

//    public DeviceDetailsDTO updateDevice(UUID id, DeviceDetailsDTO deviceDTO) {
//        Optional<Device> deviceOptional = deviceRepository.findById(id);
//        if (!deviceOptional.isPresent()) {
//            LOGGER.error("Device with id {} was not found in db for update", id);
//            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
//        }
//        Device deviceToUpdate = deviceOptional.get();
//
//        deviceToUpdate.setDescription(deviceDTO.getDescription());
//        deviceToUpdate.setAddress(deviceDTO.getAddress());
//        deviceToUpdate.setMHEC(deviceDTO.getMhec());
//       // deviceToUpdate.setUser_id(deviceDTO.getUser_id());
//
//        Device updatedDevice = deviceRepository.save(deviceToUpdate);
//        LOGGER.debug("Device with id {} was updated in db", updatedDevice.getId());
//        return DeviceBuilder.toPersonDetailsDTO(updatedDevice);
//    }

    public DeviceDetailsDTO updateDevice(UUID id, DeviceUpdateDTO userDTO) {
        Optional<Device> userOptional = deviceRepository.findById(id);
        if (!userOptional.isPresent()) {
            LOGGER.error("User with id {} was not found in db for update", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }
        Device userToUpdate = userOptional.get();

        // Check each field and update only if it's not null
        if (userDTO.getDescription() != null) {
            userToUpdate.setDescription(userDTO.getDescription());
        }
        if (userDTO.getAddress() != null) {
            userToUpdate.setAddress(userDTO.getAddress());
        }
        if (userDTO.getUserEmail() != null) {
            userToUpdate.setUserEmail((userDTO.getUserEmail()));
        }
        if (userDTO.getMhec() != 0) {
            userToUpdate.setMHEC((userDTO.getMhec()));
        }

        Device updatedUser =  deviceRepository.save(userToUpdate);
        LOGGER.debug("User with id {} was updated in db", updatedUser.getId());
        return DeviceBuilder.toDeviceDetailsDTO(updatedUser);
    }

    public void deleteDevice(UUID id) {
        Optional<Device> deviceOptional = deviceRepository.findById(id);
        if (!deviceOptional.isPresent()) {
            LOGGER.error("Device with id {} was not found in db for deletion", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }
        deviceRepository.delete(deviceOptional.get());
        LOGGER.debug("Device with id {} was deleted from db", id);
    }

    public List<DeviceDetailsDTO> findAllDeviceDetails() {
        List<Device> deviceList = deviceRepository.findAll();
        return deviceList.stream()
                .map(DeviceBuilder::toDeviceDetailsDTO)
                .collect(Collectors.toList());
    }

    public String findDeviceAddressById(UUID id) {
        Optional<Device> deviceOptional = deviceRepository.findById(id);
        if (!deviceOptional.isPresent()) {
            LOGGER.error("Device with id {} was not found in db", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }
        return deviceOptional.get().getAddress();
    }

   //
//
//    public UUID insert(DeviceDetailsDTO descriptionDTO) {
//        Device newDevice = new Device();
//        newDevice.setDescription(descriptionDTO.getDescription());
//        newDevice.setAddress(descriptionDTO.getAddress());
//        newDevice.setMHEC(descriptionDTO.getMhec());
//
//        // Check if the userId is present in the DTO
//        if (descriptionDTO.getUser_id() != null) {
//            UserReference userReference = userReferenceRepository.findById(descriptionDTO.getId())
//                    .orElseGet(() -> createNewUserReference(descriptionDTO.getId()));
//            newDevice.setUser_id(userReference);
//        }
//
//        Device savedDevice = deviceRepository.save(newDevice);
//        return savedDevice.getId();
//    }
//
//    private UserReference createNewUserReference(UUID userId) {
//        UserReference newUserReference = new UserReference();
//        newUserReference.setUserId(userId);
//        return userReferenceRepository.save(newUserReference);
//    }

//    public List<DeviceDetailsDTO> getDevicesByUserEmail(String userEmail) {
//        List<Device> devices = deviceRepository.findByUserEmail(userEmail);
//        return devices.stream()
//                .map(this::convertToDTO) // Ensure you have the convertToDTO method
//                .collect(Collectors.toList());
//    }

    public List<DeviceDetailsDTO> getDevicesByUserEmail(String email) {
        List<Device> devices = deviceRepository.findByUserEmail(email);
        return devices.stream()
                .map(DeviceBuilder::toDeviceDetailsDTO)
                .collect(Collectors.toList());
    }

    private DeviceDetailsDTO convertToDTO(Device device) {
        DeviceDetailsDTO dto = new DeviceDetailsDTO();
        dto.setDescription(device.getDescription());
        dto.setAddress(device.getAddress());
        dto.setMhec(device.getMHEC());
        dto.setUserEmail(device.getUserEmail());
        return dto;
    }


    //delete in cascada
    public boolean deleteDevicesByUserEmail(String email) {
        List<Device> devices = deviceRepository.findByUserEmail(email);

        if (!devices.isEmpty()) {
            deviceRepository.deleteAll(devices);
            return true;
        }

        return false;
    }

    //update cascada


    public boolean updateEmail(String oldEmail, String newEmail) {
        List<Device> devices = deviceRepository.findByUserEmail(oldEmail);

        if (!devices.isEmpty()) {
            for (Device device : devices) {
                device.setUserEmail(newEmail);
                deviceRepository.save(device);
            }
            return true;
        }
        return false;
    }
    //delete cascada

    public void deleteByEmail(String email) {
        deviceRepository.deleteByUserEmail(email);
    }

}
