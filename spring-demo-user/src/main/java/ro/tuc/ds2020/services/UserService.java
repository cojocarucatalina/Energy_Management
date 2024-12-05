package ro.tuc.ds2020.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.UserDTO;
import ro.tuc.ds2020.dtos.UserDetailsDTO;
import ro.tuc.ds2020.dtos.UserUpdateDTO;
import ro.tuc.ds2020.dtos.builders.UserBuilder;
import ro.tuc.ds2020.entities.User;
import ro.tuc.ds2020.repositories.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> findUsers() {
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .map(UserBuilder::toUserDTO)
                .collect(Collectors.toList());
    }

    public UserDetailsDTO findUserById(UUID id) {
        Optional<User> prosumerOptional = userRepository.findById(id);
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("User with id {} was not found in db", id);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
        }
        return UserBuilder.toUserDetailsDTO(prosumerOptional.get());
    }

    public UUID insert(UserDetailsDTO userDTO) {
        User user = UserBuilder.toEntity(userDTO);
        user = userRepository.save(user);
        LOGGER.debug("User with id {} was inserted in db", user.getId());
        return user.getId();
    }

    //update and delete

//    public UserDetailsDTO updateUser(UUID id, UserDetailsDTO userDTO) {
//        Optional<User> userOptional = userRepository.findById(id);
//        if (!userOptional.isPresent()) {
//            LOGGER.error("User with id {} was not found in db for update", id);
//            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
//        }
//        User userToUpdate = userOptional.get();
//
//        userToUpdate.setName(userDTO.getName());
//        userToUpdate.setEmail(userDTO.getEmail());
//        userToUpdate.setIsAdmin(userDTO.getAdmin());
//
//        User updatedUser = userRepository.save(userToUpdate);
//        LOGGER.debug("User with id {} was updated in db", updatedUser.getId());
//        return UserBuilder.toUserDetailsDTO(updatedUser);
//    }

    public UserDetailsDTO updateUser(UUID id, UserUpdateDTO userDTO) {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            LOGGER.error("User with id {} was not found in db for update", id);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
        }
        User userToUpdate = userOptional.get();

        if (userDTO.getName() != null) {
            userToUpdate.setName(userDTO.getName());
        }
        if (userDTO.getEmail() != null) {
            userToUpdate.setEmail(userDTO.getEmail());
        }
        if (userDTO.getPassword() != null) {
            userToUpdate.setPassword((userDTO.getPassword()));
        }

        User updatedUser = userRepository.save(userToUpdate);
        LOGGER.debug("User with id {} was updated in db", updatedUser.getId());
        return UserBuilder.toUserDetailsDTO(updatedUser);
    }


    public void deleteUser(UUID id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            LOGGER.error("User with id {} was not found in db for deletion", id);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
        }
        userRepository.delete(userOptional.get());
        LOGGER.debug("User with id {} was deleted from db", id);
    }

    public boolean authenticateUser(String username, String password) {
        Optional<User> userOptional = userRepository.findByEmail(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getPassword().equals(password);
        }
        return false;
    }

    public UserDetailsDTO findUserByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            LOGGER.error("User with email {} was not found in db", email);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with email: " + email);
        }
        return UserBuilder.toUserDetailsDTO(userOptional.get());
    }

    public String getEmailByUserId(UUID userId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            return user.get().getEmail();
        } else {
            throw new EntityNotFoundException("User with ID " + userId + " not found.");
        }
    }
}
