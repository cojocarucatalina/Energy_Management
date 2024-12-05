package ro.tuc.ds2020.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ro.tuc.ds2020.AppConfig;
import ro.tuc.ds2020.dtos.UserReferenceDTO;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.UserReference;
import ro.tuc.ds2020.repositories.DeviceRepository;
import ro.tuc.ds2020.repositories.UserReferenceRepository;
import ro.tuc.ds2020.services.DeviceService;
import ro.tuc.ds2020.services.UserReferenceService;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin
@RequestMapping(value = "/reference")
public class UserReferenceController {

    private final UserReferenceService userReferenceService;
    @Autowired
    private UserReferenceRepository userReferenceRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserReferenceService.class);
    private DeviceRepository deviceRepository;
    private AppConfig restTemplate;

    @Autowired
    public UserReferenceController(UserReferenceService userReferenceService, UserReferenceRepository userReferenceRepository) {
        this.userReferenceService = userReferenceService;
        this.userReferenceRepository = userReferenceRepository;
    }

    //create
    @PostMapping()
    public ResponseEntity<UUID> insertUserReference(@Valid @RequestBody UserReferenceDTO userReferenceDTO) {
        //userReferenceService.insertUserReference(userReferenceDTO);
        UUID userId = userReferenceService.insert(userReferenceDTO);
        return new ResponseEntity<>(userId,HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteUserReference(@PathVariable("id") UUID userId) {
        userReferenceService.deleteUserReference(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping()
    public ResponseEntity<List<UserReferenceDTO>> getUserReferences() {
        List<UserReferenceDTO> dtos = userReferenceService.findUserReferences();
        for (UserReferenceDTO dto : dtos) {
            Link userLink = linkTo(methodOn(UserReferenceController.class)
                    .getUserReference(dto.getId())).withRel("userDetails");
            dto.add(userLink);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserReferenceDTO> getUserReference(@PathVariable("id") UUID userId) {
        UserReferenceDTO dto = userReferenceService.findUserReferenceById(userId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    //update in cascada

    @PutMapping("/updateEmail")
    public ResponseEntity<Void> updateEmail(@RequestBody Map<String, String> request) {
        String oldEmail = request.get("oldEmail");
        String newEmail = request.get("newEmail");

        boolean success = userReferenceService.updateEmail(oldEmail, newEmail);

        if (success) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //delete in cascada

//    @DeleteMapping("/deleteByEmail")
//    public ResponseEntity<Void> deleteUserByEmail(@RequestParam String email) {
//        boolean success = userReferenceService.deleteUserByEmail(email);
//
//        if (success) {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }

    @DeleteMapping("/deleteByEmail")
    public ResponseEntity<Void> deleteUserByEmail(@RequestParam String email) {
        boolean success = userReferenceService.deleteUserByEmail(email);

        if (success) {
            boolean devicesDeleted = deleteDevicesByUserEmail(email);

            if (devicesDeleted) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                LOGGER.warn("Devices associated with email {} could not be deleted.", email);
                return new ResponseEntity<>(HttpStatus.PARTIAL_CONTENT);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private boolean deleteDevicesByUserEmail(String email) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8.toString());
            String url = "http://localhost:8081/device/deleteByUserEmail/" + encodedEmail;

            restTemplate.delete(url);
            return true;
        } catch (Exception e) {
            LOGGER.error("Failed to delete devices in device service for email: {}", email, e);
            return false;
        }
    }

    @DeleteMapping("/delete/{email}")
    public ResponseEntity<Void> deleteUserReferenceByEmail(@PathVariable("email") String email) {
        try {
            String decodedEmail = URLDecoder.decode(email, StandardCharsets.UTF_8.toString());
            userReferenceService.deleteByEmail(decodedEmail);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Failed to decode email: {}", email, e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //cleanup
    @DeleteMapping("/cleanup")
    public ResponseEntity<Void> cleanupUnusedUserReferences() {
        List<UserReferenceDTO> userReferences = userReferenceService.findUserReferences();
        for (UserReferenceDTO userReference : userReferences) {
            String userEmail = userReference.getEmail();
            Optional<Device> devices = deviceRepository.findAllByUserEmail(userEmail);


            if (!devices.isPresent()) {
                userReferenceService.deleteUserReference(userReference.getId());
            }
        }
        return ResponseEntity.noContent().build();
    }


}
