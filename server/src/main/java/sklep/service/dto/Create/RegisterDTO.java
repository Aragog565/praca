package sklep.service.dto.Create;

import org.springframework.web.multipart.MultipartFile;
import sklep.service.annotation.ValidEmail;
import sklep.service.annotation.ValidName;
import sklep.service.annotation.ValidPassword;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class RegisterDTO implements Serializable {

    @NotEmpty
    @ValidName
    private String name;

    @NotEmpty
    @ValidEmail
    private String email;

    @NotEmpty
    @ValidPassword
    private String password;

    private Boolean termsAndConditions = false;

    private Boolean specialOffers = false;

    @NotEmpty
    @Size(min=1,max=36,message = "zła długosć: min 1, max 36")
    private String surname;

    @Size(min=9,max=11,message = "zła długosć: min 9, max 11")
    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
