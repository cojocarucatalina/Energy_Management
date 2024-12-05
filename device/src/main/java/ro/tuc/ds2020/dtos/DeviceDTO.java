package ro.tuc.ds2020.dtos;

import org.springframework.hateoas.RepresentationModel;
import ro.tuc.ds2020.entities.UserReference;

import java.util.Objects;
import java.util.UUID;

public class DeviceDTO extends RepresentationModel<DeviceDTO> {
    private UUID id;
    private String description;
    private int mhec;
    private String address;
    private String userEmail;
   // private UserReference user_id;

    public DeviceDTO() {
    }

//    public DeviceDTO(UUID id, String description,int mhec, String address,  String userEmail, UserReference user_id) {
//        this.id = id;
//        this.description = description;
//        this.mhec = mhec;
//        this.address = address;
//        this.userEmail = userEmail;
//        this.user_id = user_id;
//    }

    public DeviceDTO(UUID id, String description,int mhec, String address,  String userEmail) {
        this.id = id;
        this.description = description;
        this.mhec = mhec;
        this.address = address;
        this.userEmail = userEmail;
       // this.user_id = user_id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMhec() {
        return mhec;
    }

    public void setMhec(int mhec) {
        this.mhec = mhec;
    }

//    public UserReference getUser_id() {
//        return user_id;
//    }
//
//    public void setUser_id(UserReference user_id) {
//        this.user_id = user_id;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceDTO deviceDTO = (DeviceDTO) o;
        return mhec == deviceDTO.mhec &&
                Objects.equals(description, deviceDTO.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, mhec);
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
    }
}
