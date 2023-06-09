package sklep.service.dto.Update;

import org.springframework.web.multipart.MultipartFile;
import sklep.service.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class UpdateUserDTO {

    @ValidUpdateName
    private String name;

    @ValidUpdatePassword
    private String password;

    @Size(min=1,max=36,message = "Zła długość: min 1, max 36")
    private String surname;

    private Boolean termsAndConditions;

    private Boolean specialOffers;

    @ValidUpdateEmail
    private String email;

    @Size(min=9,max=11,message = "Zła długość: min 9, max 11")
    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getTermsAndConditions() {
        return termsAndConditions;
    }

    public void setTermsAndConditions(Boolean termsAndConditions) {
        this.termsAndConditions = termsAndConditions;
    }

    public Boolean getSpecialOffers() {
        return specialOffers;
    }

    public void setSpecialOffers(Boolean specialOffers) {
        this.specialOffers = specialOffers;
    }

}
