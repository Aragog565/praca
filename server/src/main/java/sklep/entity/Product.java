package sklep.entity;

import com.sun.istack.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import sklep.service.ProductService;
import sklep.utils.FileUploadUtil;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;

    @NotNull
    @Column(name = "vat", nullable = false)
    private Double vat;

    @NotNull
    @Column(name = "number", nullable = false)
    private Long number;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @NotNull
    @Column(name = "photo", nullable = false)
    private String photo;

    @Column(name = "promotion", nullable = false)
    private Integer promotion = 0;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Comment> comment;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Rate> rate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Parameters> parameters;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<OrderProduct> orderProduct;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<FavoriteProduct> favoriteProducts;

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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPhoto() {
        return FileUploadUtil.readFile(ProductService.uploadDir, photo);
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhotoName(){
        return this.photo;
    }

    public List<Parameters> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameters> parameters) {
        this.parameters = parameters;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Comment> getComment() {
        return comment;
    }

    public void setComment(Set<Comment> comment) {
        this.comment = comment;
    }

    public Set<OrderProduct> getOrderProduct() {
        return orderProduct;
    }

    public void setOrderProduct(Set<OrderProduct> orderProduct) {
        this.orderProduct = orderProduct;
    }

    public Double getVat() {
        return vat;
    }

    public void setVat(Double vat) {
        this.vat = vat;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<Rate> getRate() {
        return rate;
    }

    public void setRate(Set<Rate> rate) {
        this.rate = rate;
    }

    public Set<FavoriteProduct> getFavoriteProducts() {
        return favoriteProducts;
    }

    public void setFavoriteProducts(Set<FavoriteProduct> favoriteProducts) {
        this.favoriteProducts = favoriteProducts;
    }

    public Integer getPromotion() {
        return promotion;
    }

    public void setPromotion(Integer promotion) {
        this.promotion = promotion;
    }
}
