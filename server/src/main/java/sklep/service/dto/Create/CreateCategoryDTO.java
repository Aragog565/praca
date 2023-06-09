package sklep.service.dto.Create;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CreateCategoryDTO {

    @NotEmpty
    @NotNull
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
