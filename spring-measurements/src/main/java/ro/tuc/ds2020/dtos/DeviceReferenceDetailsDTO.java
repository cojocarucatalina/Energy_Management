package ro.tuc.ds2020.dtos;

import ro.tuc.ds2020.entities.DeviceReference;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class DeviceReferenceDetailsDTO {

    private UUID id;
    @NotNull
    private UUID device_id;
    @NotNull
    private String user_email;
    @NotNull
    private int mhec;

    public DeviceReferenceDetailsDTO() {
    }

    public DeviceReferenceDetailsDTO(DeviceReference deviceReference) {
        this.id = deviceReference.getId();
        this.device_id = deviceReference.getDevice_id();
        this.user_email = deviceReference.getUser_email();
        this.mhec = deviceReference.getMhec();
    }

    public DeviceReferenceDetailsDTO(UUID id, UUID device_id, String user_email, int mhec) {
        this.id = id;
        this.device_id = device_id;
        this.user_email = user_email;
        this.mhec = mhec;
    }

    public DeviceReferenceDetailsDTO( UUID device_id, String user_email, int mhec) {
        this.device_id = device_id;
        this.user_email = user_email;
        this.mhec = mhec;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDevice_id() {
        return device_id;
    }

    public void setDevice_id(UUID device_id) {
        this.device_id = device_id;
    }

    public int getMhec() {
        return mhec;
    }

    public void setMhec(int mhec) {
        this.mhec = mhec;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }
}
