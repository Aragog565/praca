package sklep.service.dto;

import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;

public class OrderDTO {

    private Long id;

    private Timestamp createdAt;

    private Timestamp dateOfPayment;

    private Timestamp shippingDate;

    private Timestamp completionDate;

    private Timestamp lastModificationAt;

    @Size(min=16,max=16,message = "zła długość: wymagane 16 znaków ")
    private String bankAccount;

    private String payment;

    private List<OrderProductDTO> orderProduct;

    private Boolean cancelled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getDateOfPayment() {
        return dateOfPayment;
    }

    public void setDateOfPayment(Timestamp dateOfPayment) {
        this.dateOfPayment = dateOfPayment;
    }

    public Timestamp getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(Timestamp shippingDate) {
        this.shippingDate = shippingDate;
    }

    public Timestamp getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Timestamp completionDate) {
        this.completionDate = completionDate;
    }

    public Timestamp getLastModificationAt() {
        return lastModificationAt;
    }

    public void setLastModificationAt(Timestamp lastModificationAt) {
        this.lastModificationAt = lastModificationAt;
    }

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

    public List<OrderProductDTO> getOrderProduct() {
        return orderProduct;
    }

    public void setOrderProduct(List<OrderProductDTO> orderProducts) {
        this.orderProduct = orderProducts;
    }

    public Boolean getCancelled() {
        return cancelled;
    }

    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }
}
