package ro.tuc.ds2020.dtos;

import ro.tuc.ds2020.dtos.validators.annotation.AgeLimit;
import ro.tuc.ds2020.entities.UserReference;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Objects;
import java.util.UUID;

public class DeviceDetailsDTO {

    private UUID id;
    @NotNull
    private String description;
    @NotNull
    private String address;
    private int mhec;
    @NotNull
    private String userEmail;
    @Null
    private UserReference user_id;

    public DeviceDetailsDTO() {
    }

    public DeviceDetailsDTO(UUID id, String description, String address, int mhec,String userEmail) {
        this.id = id;
        this.description = description;
        this.address = address;
        this.mhec = mhec;
        this.userEmail = userEmail;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getMhec() {
        return mhec;
    }

    public void setMhec(int mhec) {
        this.mhec = mhec;
    }

    public UserReference getUser_id() {
        return user_id;
    }

    public void setUser_id(UserReference user_id) {
        this.user_id = user_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceDetailsDTO that = (DeviceDetailsDTO) o;
        return mhec == that.mhec &&
                Objects.equals(description, that.description) &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, address, mhec);
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
