package ro.tuc.ds2020.controllers;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
        import org.springframework.web.client.RestTemplate;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.*;
        import ro.tuc.ds2020.repositories.DeviceReferenceRepository;
import ro.tuc.ds2020.services.DeviceReferenceService;
import org.slf4j.Logger;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@CrossOrigin
@RequestMapping(value = "/deviceReference")
public class DeviceReferenceController {

    @Autowired
    private final DeviceReferenceService deviceReferenceService;
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceReferenceService.class);
    private final DeviceReferenceRepository deviceReferenceRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public DeviceReferenceController(DeviceReferenceService deviceReferenceService, DeviceReferenceRepository deviceReferenceRepository, RestTemplate restTemplate) {
        this.deviceReferenceService = deviceReferenceService;
        this.deviceReferenceRepository = deviceReferenceRepository;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/details")
    public ResponseEntity<List<DeviceReferenceDetailsDTO>> getAllDeviceReferenceDetails() {
        List<DeviceReferenceDetailsDTO> deviceReferenceDetails = deviceReferenceService.findAllDeviceReferenceDetails();
        return new ResponseEntity<>(deviceReferenceDetails, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<UUID> insertDeviceReference(@Valid @RequestBody DeviceReferenceDetailsDTO deviceReferenceDTO) {
        LOGGER.info("Received DeviceReferenceDetailsDTO with deviceReference_id: {}", deviceReferenceDTO.getDevice_id());

        UUID deviceReferenceID = deviceReferenceService.insertDevice(deviceReferenceDTO);
        return new ResponseEntity<>(deviceReferenceID, HttpStatus.CREATED);
    }

    public void deleteDevice(UUID deviceId) {
        try {
            deviceReferenceRepository.deleteById(deviceId);
            System.out.println(" [*] Deleted device with ID: " + deviceId);
        } catch (EmptyResultDataAccessException e) {
            System.out.println(" [!] No device found with ID: " + deviceId + ". Nothing to delete.");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteDeviceById(@PathVariable UUID id) {
        boolean success = deviceReferenceService.deleteDeviceBool(id);

        if (success) {
            LOGGER.info("Device with ID {} deleted successfully.", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            LOGGER.warn("Device with ID {} not found. Deletion not performed.", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{deviceId}/mhec")
    public ResponseEntity<Integer> getMhecByDeviceId(@PathVariable UUID deviceId) {
        try {
            int mhec = deviceReferenceService.getMhecByDeviceId(deviceId);
            return new ResponseEntity<>(mhec, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}

