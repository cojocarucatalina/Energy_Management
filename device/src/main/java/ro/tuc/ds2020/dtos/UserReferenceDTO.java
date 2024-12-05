package ro.tuc.ds2020.dtos;


import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

public class UserReferenceDTO extends RepresentationModel<UserReferenceDTO> {
    private UUID id;
    private String email;

    public UserReferenceDTO() {
    }

    public UserReferenceDTO(UUID id, String email) {
        this.id = id;
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
