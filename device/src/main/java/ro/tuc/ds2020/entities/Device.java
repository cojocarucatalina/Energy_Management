package ro.tuc.ds2020.entities;

import jakarta.persistence.Column;
import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.Id;
import org.springframework.web.bind.annotation.RequestMapping;

//import javax.persistence.*;
//import javax.persistence.Entity;
import java.io.Serializable;
import java.util.UUID;

@Entity
public class Device implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-binary")
    private UUID id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "mhec", nullable = false)
    private int MHEC;

    @Column(name = "userEmail")
    private String userEmail;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_reference_id", nullable = false)
//    private UserReference userReference;

    public Device() {
    }

    public Device(String description, String address, int MHEC, String userEmail) {
        this.description = description;
        this.address = address;
        this.MHEC = MHEC;
        this.userEmail = userEmail;
      //  this.user_id = user_id;
    }

    public Device(String description, String address, int MHEC) {
        this.description = description;
        this.address = address;
        this.MHEC = MHEC;
      //  this.userReference = userReference;
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

    public int getMHEC() {
        return MHEC;
    }

    public void setMHEC(int age) {
        this.MHEC = age;
    }

//    public UserReference getUser_id() {
//        return user_id;
//    }
//    public void setUser_id(UserReference user_id) {
//        this.user_id = user_id;
//    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

}
