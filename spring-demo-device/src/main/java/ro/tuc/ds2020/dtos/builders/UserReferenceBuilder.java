package ro.tuc.ds2020.dtos.builders;

import ro.tuc.ds2020.dtos.UserReferenceDTO;
import ro.tuc.ds2020.entities.UserReference;

public class UserReferenceBuilder {

    private UserReferenceBuilder() {
    }

    public static UserReferenceDTO toUserReferenceDTO(UserReference userReference) {
        return new UserReferenceDTO(userReference.getId(), userReference.getEmail());
    }

    public static UserReferenceDTO toUserReferenceDetailsDTO(UserReference userReference) {
        return new UserReferenceDTO(userReference.getId(), userReference.getEmail());
    }

    public static UserReference toEntity(UserReferenceDTO userReferenceDTO) {
        return new UserReference(userReferenceDTO.getEmail());
    }
}
