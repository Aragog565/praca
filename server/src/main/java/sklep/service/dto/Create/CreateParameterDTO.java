package sklep.service.dto.Create;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CreateParameterDTO {

    @NotEmpty
    @NotNull
    private String name;

    @NotEmpty
    @NotNull
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
