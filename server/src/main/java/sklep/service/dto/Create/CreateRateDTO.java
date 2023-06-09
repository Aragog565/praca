package sklep.service.dto.Create;

import org.hibernate.validator.constraints.Length;
import sklep.config.Constants;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CreateRateDTO {
    @NotNull
    @Max(value=Constants.MAX_RATE_VALUE)
    @Min(value=Constants.MIN_RATE_VALUE)
    private Integer value;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
