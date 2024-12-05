package ro.tuc.ds2020.entities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "device_reference")
public class DeviceReference implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-binary")
    private UUID id;

    @Column(name = "device_id", nullable = false)
    private UUID device_id;

    @Column(name = "user_email", nullable = false)
    private String user_email;

    @Column(name = "mhec", nullable = false)
    private int mhec;

    public DeviceReference() {
    }

    public DeviceReference(UUID id, UUID device_id, String user_email, int mhec) {
        this.id = id;
        this.device_id = device_id;
        this.user_email = user_email;
        this.mhec = mhec;
    }

    public DeviceReference(UUID deviceId, String userEmail, int mhec) {
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

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public int getMhec() {
        return mhec;
    }

    public void setMhec(int mhec) {
        this.mhec = mhec;
    }
}