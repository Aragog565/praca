package sklep.service.dto.Create;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CreateAddressDTO {

    @NotEmpty
    @NotNull
    private String city;
    @NotEmpty
    @NotNull
    private String zipCode;
    @NotEmpty
    @NotNull
    private String street;
    @NotEmpty
    @NotNull
    private String houseNumber;

    private String apartmentNumber;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }
}
