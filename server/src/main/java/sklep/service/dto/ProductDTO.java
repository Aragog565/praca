package sklep.service.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import sklep.entity.FavoriteProduct;
import sklep.entity.Rate;
import sklep.entity.User;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductDTO {
    private Long id;

    private String name;

    private Double price;

    private Long number;

    private String description;

    private Timestamp createdAt;

    private String photo;

    private Integer promotion;

    private UserDTO user;

    private Set<ParameterDTO> parameters;

    private CategoryDTO category;

    private Double vat;

    private Double avg;

    private Set<Rate> rate;

    private Set<FavoriteProduct> favoriteProducts;

    private Boolean isFavorite;

    public ProductDTO() {
        rate = new HashSet<>();
        favoriteProducts = new HashSet<>();
    }

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

    public Double getPrice() {
        return price-(price * (promotion/100.));
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public Set<ParameterDTO> getParameters() {
        return parameters;
    }

    public void setParameters(Set<ParameterDTO> parameters) {
        this.parameters = parameters;
    }

    public Double getVat() {
        return vat;
    }

    public void setVat(Double vat) {
        this.vat = vat;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public Double getAvgRate() {
        if(avg==null){
            double sum = 0.;
            for (Rate i :rate){
                sum+=i.getValue();
            }
            avg=(sum/Math.max(rate.size(), 1));
        }
        return avg;
    }

    public void setRate(Set<Rate> rate) {
        if(rate == null){
            this.rate = new HashSet<>();
        }else {
            this.rate = rate;
        }
    }

    public Integer getTotalRates(){
        return rate.size();
    }


    public Integer getFavorites() {
        return favoriteProducts.size();
    }

    public void setFavoriteProducts(Set<FavoriteProduct> favoriteProducts) {
        this.favoriteProducts = favoriteProducts;
    }

    public Boolean getIsFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    public void setPromotion(Integer promotion) {
        this.promotion = promotion;
    }

    public Integer getPromotion() {
        return promotion;
    }
}
