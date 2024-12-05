package ro.tuc.ds2020.dtos;

public class UserIdDTO {

    private String email;

    public UserIdDTO() {
    }

    public UserIdDTO(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
