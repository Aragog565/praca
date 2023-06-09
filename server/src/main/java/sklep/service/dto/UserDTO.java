package sklep.service.dto;

import sklep.entity.Role;

import java.util.Set;

public class UserDTO {
    private Long id;

    private String name;

    private String email;

    private Role role;

    private String surname;

    private String phone;

    private String photo;

    private AddressDTO address;

    private Boolean termsAndConditions;

    private Boolean specialOffers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
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
