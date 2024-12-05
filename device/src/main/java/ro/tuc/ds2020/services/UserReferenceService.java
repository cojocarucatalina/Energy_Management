package ro.tuc.ds2020.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.DeviceUpdateDTO;
import ro.tuc.ds2020.dtos.UserReferenceDTO;
import ro.tuc.ds2020.dtos.builders.UserReferenceBuilder;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.UserReference;
import ro.tuc.ds2020.repositories.UserReferenceRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserReferenceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserReferenceService.class);
    private final UserReferenceRepository userReferenceRepository;

    @Autowired
    public UserReferenceService(UserReferenceRepository userReferenceRepository) {
        this.userReferenceRepository = userReferenceRepository;
    }

    public void insertUserReference(UserReferenceDTO userReferenceDTO) {
        UserReference userReference = new UserReference();
        userReference.setUserId(userReferenceDTO.getId());
        userReference.setEmail(userReferenceDTO.getEmail());
        userReferenceRepository.save(userReference);
    }

    public List<UserReferenceDTO> getAllUserReferences() {
        return userReferenceRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<UserReferenceDTO> getUserReferenceById(UUID id) {
        return userReferenceRepository.findById(id)
                .map(this::convertToDTO);
    }

    private UserReferenceDTO convertToDTO(UserReference userReference) {
        UserReferenceDTO dto = new UserReferenceDTO();
        dto.setId(userReference.getId());
        dto.setEmail(userReference.getEmail());
        return dto;
    }

    private UserReference convertToEntity(UserReferenceDTO userReferenceDTO) {
        UserReference userReference = new UserReference();
        userReference.setUserId(userReferenceDTO.getId());
        userReference.setEmail(userReferenceDTO.getEmail());
        return userReference;
    }

    public UUID insert(UserReferenceDTO userDTO) {
        UserReference user = UserReferenceBuilder.toEntity(userDTO);
        user = userReferenceRepository.save(user);
        LOGGER.debug("User with id {} was inserted in db", user.getId());
        return user.getId();
    }

    public void deleteUserReference(UUID id) {
        Optional<UserReference> userOptional = userReferenceRepository.findById(id);
        if (!userOptional.isPresent()) {
            LOGGER.error("User with id {} was not found in db for deletion", id);
            throw new ResourceNotFoundException(UserReference.class.getSimpleName() + " with id: " + id);
        }
        userReferenceRepository.delete(userOptional.get());
        LOGGER.debug("User with id {} was deleted from db", id);
    }

    public List<UserReferenceDTO> findUsers() {
        List<UserReference> userList = userReferenceRepository.findAll();
        return userList.stream()
                .map(UserReferenceBuilder::toUserReferenceDTO)
                .collect(Collectors.toList());
    }

    public UserReferenceDTO findUserReferenceById(UUID id) {
        Optional<UserReference> prosumerOptional = userReferenceRepository.findById(id);
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("User with id {} was not found in db", id);
            throw new ResourceNotFoundException(UserReference.class.getSimpleName() + " with id: " + id);
        }
        return UserReferenceBuilder.toUserReferenceDetailsDTO(prosumerOptional.get());
    }

    public List<UserReferenceDTO> findUserReferences() {
        List<UserReference> userList = userReferenceRepository.findAll();
        return userList.stream()
                .map(UserReferenceBuilder::toUserReferenceDTO)
                .collect(Collectors.toList());
    }

    public boolean existsByEmail(String email) {
        return userReferenceRepository.findByEmail(email).isPresent();
    }

    //update
    public boolean updateEmail(String oldEmail, String newEmail) {
        Optional<UserReference> optionalUserReference = userReferenceRepository.findByEmail(oldEmail);

        if (optionalUserReference.isPresent()) {
            UserReference userReference = optionalUserReference.get();
            userReference.setEmail(newEmail);
            userReferenceRepository.save(userReference);
            return true;
        }
        return false;
    }


    public boolean deleteUserByEmail(String email) {
        Optional<UserReference> userReference = userReferenceRepository.findByEmail(email);

        if (userReference.isPresent()) {
            userReferenceRepository.delete(userReference.get());
            return true;
        }

        return false;
    }

    public void deleteByEmail(String email) {
        userReferenceRepository.deleteByUserEmail(email);
    }

}
