package ro.tuc.ds2020.controllers;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ro.tuc.ds2020.dtos.*;
import ro.tuc.ds2020.repositories.UserRepository;
import ro.tuc.ds2020.services.UserService;
import org.slf4j.Logger;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository, RestTemplate restTemplate) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
    }

    @GetMapping()
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<UserDTO> dtos = userService.findUsers();
        for (UserDTO dto : dtos) {
            Link userLink = linkTo(methodOn(UserController.class)
                    .getUser(dto.getId())).withRel("userDetails");
            dto.add(userLink);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    //create
    @PostMapping()
    public ResponseEntity<UUID> insertProsumer(@Valid @RequestBody UserDetailsDTO userDTO) {
        UUID userID = userService.insert(userDTO);
        return new ResponseEntity<>(userID, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDetailsDTO> getUser(@PathVariable("id") UUID userId) {
        UserDetailsDTO dto = userService.findUserById(userId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    //TODO: UPDATE, DELETE per resource

    @PutMapping(value = "/{id}")
    public ResponseEntity<UserDetailsDTO> updateUser(@PathVariable("id") UUID userId,
                                                     @Valid @RequestBody UserUpdateDTO userDTO) {
        LOGGER.info("Attempting to update user with id: {}", userId);

        UserDetailsDTO existingUser = userService.findUserById(userId);
        String oldEmail = existingUser.getEmail();

        UserDetailsDTO updatedUser = userService.updateUser(userId, userDTO);

        if (!oldEmail.equals(updatedUser.getEmail())) {
            updateUserInUserReference(oldEmail, updatedUser.getEmail());
        }

        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    private void updateUserInUserReference(String oldEmail, String newEmail) {
        String url = "http://localhost:8081/reference/updateEmail";
        String url2 = "http://localhost:8081/device/updateEmail";

        Map<String, String> request = new HashMap<>();
        try {
            //String encodedOldEmail = URLEncoder.encode(oldEmail, StandardCharsets.UTF_8.toString());
            //String encodedNewEmail = URLEncoder.encode(newEmail, StandardCharsets.UTF_8.toString());

            request.put("oldEmail", oldEmail);
            request.put("newEmail", newEmail);

            //restTemplate.put(url, request);
            restTemplate.put(url2, request);
            LOGGER.info("Successfully updated email in userReference microservice");
        } catch (Exception e) {
            LOGGER.error("Failed to update email in userReference microservice", e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        boolean isAuthenticated = userService.authenticateUser(loginDTO.getEmail(), loginDTO.getPassword());
        if (isAuthenticated) {
            LOGGER.info("User {} logged in successfully.", loginDTO.getEmail());
            UserDetailsDTO dto = userService.findUserByEmail(loginDTO.getEmail());
            return new ResponseEntity<>("Login successful "+ dto.getAdmin(), HttpStatus.OK);
        } else {
            LOGGER.warn("Failed login attempt for user {}.", loginDTO.getEmail());
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/email-by-id")
    public ResponseEntity<String> getEmailByUserId(@Valid @RequestBody EmailDTO request) {
        UserDetailsDTO dto = userService.findUserById(request.getUserId());
        if (dto != null) {
            return new ResponseEntity<>(dto.getEmail(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/id-by-email")
    public ResponseEntity<UUID> getUserIdByEmail(@Valid @RequestBody UserIdDTO request) {
        UserDetailsDTO dto = userService.findUserByEmail(request.getEmail());
        if (dto != null) {
            return new ResponseEntity<>(dto.getId(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") UUID userId) {
        String userEmail = userService.getEmailByUserId(userId);
        //deleteUserReferenceByEmail(userEmail);
        deleteDeviceByEmail(userEmail);
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private void deleteUserReferenceByEmail(String userEmail) {
        try {
            String encodedEmail = URLEncoder.encode(userEmail, StandardCharsets.UTF_8.toString());
            String url = "http://localhost:8081/reference/delete/" + encodedEmail;
            restTemplate.delete(url);
            LOGGER.info("Successfully deleted user reference for email: {}", userEmail);
        } catch (HttpClientErrorException e) {
            LOGGER.error("Failed to delete user reference for email: {}. Status: {}, Response: {}",
                    userEmail, e.getStatusCode(), e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            LOGGER.error("Unexpected error while deleting user reference for email: {}", userEmail, e);
        }
    }

    private void deleteDeviceByEmail(String userEmail) {
        try {
            String encodedEmail = URLEncoder.encode(userEmail, StandardCharsets.UTF_8.toString());
            String url = "http://localhost:8081/device/delete/" + encodedEmail;
            restTemplate.delete(url);
            LOGGER.info("Successfully deleted device for email: {}", userEmail);
        } catch (HttpClientErrorException e) {
            LOGGER.error("Failed to delete device for email: {}. Status: {}, Response: {}",
                    userEmail, e.getStatusCode(), e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            LOGGER.error("Unexpected error while deleting device for email: {}", userEmail, e);
        }
    }

}

