package sklep.service.dto.Create;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class CreateOrderDTO {

    @NotEmpty
    @NotNull
    @Size(min=16,max=16,message = "zła długość: wymagane 16 znaków ")
    private String bankAccount;

    @NotEmpty
    @NotNull
    private String payment;

    @NotNull
    private List<CreateOrderProductDTO> products;

    private Boolean cancelled = false;

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public List<CreateOrderProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<CreateOrderProductDTO> products) {
        this.products = products;
    }

    public Boolean getCancelled() {
        return cancelled;
    }

    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }
}
