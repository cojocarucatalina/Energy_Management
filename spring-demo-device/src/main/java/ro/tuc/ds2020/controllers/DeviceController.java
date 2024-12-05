package ro.tuc.ds2020.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.RabbitMQSender;
import ro.tuc.ds2020.dtos.*;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.UserReference;
import ro.tuc.ds2020.repositories.DeviceRepository;
import ro.tuc.ds2020.services.DeviceService;
import ro.tuc.ds2020.services.UserReferenceService;

import javax.validation.Valid;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin
@RequestMapping(value = "/device")
public class DeviceController {
//NU TRB DEFINITA LOGICA -- TRM LA SERRVICE
    //UN FEL DE BLL
    private final DeviceService deviceService;
    @Autowired
    private DeviceRepository deviceRepository;
    private UserReferenceService userReferenceService;
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);
    @Autowired
    private UserReferenceController userReferenceController;

    @Autowired
    public DeviceController(DeviceService deviceService, UserReferenceService userReferenceService,  UserReferenceController userReferenceController) {
        this.deviceService = deviceService;
        this.userReferenceService = userReferenceService;
        this.userReferenceController = userReferenceController;
    }

    private String decodeEmail(String encodedEmail) {
        try {
            return URLDecoder.decode(encodedEmail, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Failed to decode email: {}", encodedEmail, e);
            throw new RuntimeException("Invalid email encoding", e); // or handle it more gracefully
        }
    }
//get
    @GetMapping()
    public ResponseEntity<List<DeviceDTO>> getDescriptions() {
        List<DeviceDTO> dtos = deviceService.findDevices();
        for (DeviceDTO dto : dtos) {
            Link descriptionLink = linkTo(methodOn(DeviceController.class)
                    .getDescription(dto.getId())).withRel("descriptionDetails");
            dto.add(descriptionLink);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/details")
    public ResponseEntity<List<DeviceDetailsDTO>> getAllDeviceDetails() {
        List<DeviceDetailsDTO> deviceDetails = deviceService.findAllDeviceDetails();
        return new ResponseEntity<>(deviceDetails, HttpStatus.OK);
    }

    //post
    @PostMapping()
    public ResponseEntity<UUID> insertProsumer(@Valid @RequestBody DeviceDetailsDTO deviceDTO) {
        UUID deviceId = deviceService.insert(deviceDTO);
        UserReferenceDTO userReferenceDTO = new UserReferenceDTO();
        userReferenceDTO.setEmail(deviceDTO.getUserEmail());
        System.out.println("[DEVICE CONTROLLER LOG] DEVICE ADDED: " + deviceId);

        boolean userExists = userReferenceService.existsByEmail(deviceDTO.getUserEmail());
        if (!userExists) {
            UUID userId = userReferenceService.insert(userReferenceDTO);
        } else {
           // return new ResponseEntity<>(new UUID(5,5), HttpStatus.CONFLICT);
        }

        try {
            RabbitMQSender rabbitMQSender = new RabbitMQSender();
            rabbitMQSender.sendMessage("insert", deviceId, deviceDTO.getUserEmail(), deviceDTO.getMhec());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(deviceId, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DeviceDetailsDTO> getDescription(@PathVariable("id") UUID descriptionId) {
        DeviceDetailsDTO dto = deviceService.findDeviceById(descriptionId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/address")
    public ResponseEntity<String> getDeviceAddress(@PathVariable("id") UUID descriptionId) {
        String address = deviceService.findDeviceAddressById(descriptionId);
        return new ResponseEntity<>(address, HttpStatus.OK);
    }

    //TODO: UPDATE, DELETE per resource

    @PutMapping(value = "/{id}")
    public ResponseEntity<DeviceDetailsDTO> updateDevice(@PathVariable("id") UUID deviceId,
                                                     @Valid @RequestBody DeviceUpdateDTO deviceDTO) {
        LOGGER.info("Attempting to update device with id: {}", deviceId);
        DeviceDetailsDTO updatedDevice = deviceService.updateDevice(deviceId, deviceDTO);
        //userReferenceController.cleanupUnusedUserReferences();
        UserReferenceDTO userReferenceDTO = new UserReferenceDTO();
        userReferenceDTO.setEmail(deviceDTO.getUserEmail());

        try {
            RabbitMQSender rabbitMQSender = new RabbitMQSender();
            rabbitMQSender.sendMessage("update", deviceId, deviceDTO.getUserEmail(), deviceDTO.getMhec());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        boolean userExists = userReferenceService.existsByEmail(deviceDTO.getUserEmail());
        if (!userExists) {
            UUID userId = userReferenceService.insert(userReferenceDTO);
        } else {
            // return new ResponseEntity<>(new UUID(5,5), HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(updatedDevice, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable("id") UUID deviceId) {
        deviceService.deleteDevice(deviceId);

        try {
            RabbitMQSender rabbitMQSender = new RabbitMQSender();
            rabbitMQSender.sendMessage("delete", deviceId);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/device")
    public List<DeviceDTO> getDevices() {
        return deviceRepository.findAll().stream()
                .map(device -> {
                    DeviceDTO dto = new DeviceDTO();
                    dto.setId(device.getId());
                    dto.setDescription(device.getDescription());
                    dto.setAddress(device.getAddress());
                   // dto.setUserEmail(device.getUser_id() != null ? device.getUser_id().getEmail() : null);
                    return dto;
                })
                .collect(Collectors.toList());
    }


    ///CONFIG FILE
//    @PostMapping("/user-devices")
//    public ResponseEntity<List<DeviceDetailsDTO>> getDevicesByEmail(@Valid @RequestBody EmailRequestDTO emailRequestDTO) {
//        List<DeviceDetailsDTO> devices = deviceService.getDevicesByUserEmail(emailRequestDTO.getEmail());
//        return new ResponseEntity<>(devices, HttpStatus.OK);
//    }



    @PostMapping("/user-devices")
    public ResponseEntity<List<DeviceDetailsDTO>> getDevicesByEmail(@Valid @RequestBody EmailRequestDTO emailRequestDTO) {
        List<DeviceDetailsDTO> devices = deviceService.getDevicesByUserEmail(emailRequestDTO.getEmail());

        if (!devices.isEmpty()) {
            UUID firstDeviceId = devices.get(0).getId();
            writeFirstDeviceIdToFile(firstDeviceId);
        }

        return new ResponseEntity<>(devices, HttpStatus.OK);
    }

    private void writeFirstDeviceIdToFile(UUID deviceId) {
        String filePath = "C:\\Users\\cojoc\\Desktop\\an4 sem1\\sisteme distribuite - proiect\\simulator\\config.config";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(deviceId.toString()); // Convert UUID to String
        } catch (IOException e) {
            e.printStackTrace();
            // Log the error (use a logging framework in production code)
        }
    }




    ////// config file ^^^^^

    //delete
    @DeleteMapping("/deleteByUserEmail")
    public ResponseEntity<Void> deleteDevicesByUserEmail(@RequestParam String email) {
        boolean success = deviceService.deleteDevicesByUserEmail(email);

        if (success) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //update cascada
    @PutMapping("/updateEmail")
    public ResponseEntity<Void> updateEmail(@RequestBody Map<String, String> request) {
        String oldEmail = request.get("oldEmail");
        String newEmail = request.get("newEmail");

        boolean success = deviceService.updateEmail(oldEmail, newEmail);

        if (success) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{email}")
    public ResponseEntity<Void> deleteDeviceByEmail(@PathVariable("email") String email) {
        try {
            String decodedEmail = URLDecoder.decode(email, StandardCharsets.UTF_8.toString());

            deviceService.deleteByEmail(decodedEmail);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Failed to decode email: {}", email, e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOGGER.error("Failed to delete device for email: {}", email, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
