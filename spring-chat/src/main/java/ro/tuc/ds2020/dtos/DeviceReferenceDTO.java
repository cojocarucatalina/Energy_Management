package ro.tuc.ds2020.dtos;

import org.springframework.hateoas.RepresentationModel;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class DeviceReferenceDTO extends RepresentationModel<DeviceReferenceDTO> {

    private UUID id;
    private UUID device_id;
    private String user_email;
    private int mehc;

    public DeviceReferenceDTO() {
    }

    public DeviceReferenceDTO(UUID id, UUID device_id, int mehc, String user_email) {
        this.id = id;
        this.device_id = device_id;
        this.mehc = mehc;
        this.user_email = user_email;
    }

    public DeviceReferenceDTO(UUID device_id, String user_email, int mehc) {
        this.device_id = device_id;
        this.mehc = mehc;
        this.user_email = user_email;
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

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public int getMehc() {
        return mehc;
    }

    public void setMehc(int mehc) {
        this.mehc = mehc;
    }
}
