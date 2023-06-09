package sklep.service.dto.Update;

import org.springframework.web.multipart.MultipartFile;
import sklep.service.dto.Create.CreateParameterDTO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class UpdateProductDTO {
    @NotEmpty
    @NotNull
    private String name;

    @NotNull
    private Double price;

    @NotNull
    private Long number;

    @NotEmpty
    @NotNull
    private String description;

    private Integer promotion;

    private MultipartFile photoFile;

    private List<CreateParameterDTO> parameters;

    private Double vat;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public MultipartFile getPhotoFile() {
        return this.photoFile;
    }

    public void setPhotoFile(MultipartFile photoFile) {
        this.photoFile = photoFile;
    }

    public List<CreateParameterDTO> getParameters() {
        return parameters;
    }

    public void setParameters(List<CreateParameterDTO> parameters) {
        this.parameters = parameters;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getVat() {
        return vat;
    }

    public void setVat(Double vat) {
        this.vat = vat;
    }

    public Integer getPromotion() {
        return promotion;
    }

    public void setPromotion(Integer promotion) {
        this.promotion = promotion;
    }
}
