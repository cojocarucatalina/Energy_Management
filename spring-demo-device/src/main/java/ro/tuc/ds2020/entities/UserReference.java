package ro.tuc.ds2020.entities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class UserReference implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-binary")
    private UUID id;

    @Column(name = "email", nullable = false)
    private String email;

//    @OneToMany(mappedBy = "userReference", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Device> devices = new ArrayList<>();

    public UserReference() {
    }

    public UserReference(UUID id, String email) {
        this.id = id;
        this.email = email;
    }

    public UserReference(String email) {
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public void setUserId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
