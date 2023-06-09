package sklep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sklep.entity.FavoriteProduct;
import sklep.entity.Order;
import sklep.entity.Product;

import java.util.List;


public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order>  findByUser_IdOrderByCreatedAtDesc(Long userID);

    @Query("SELECT o FROM Order o WHERE o.id IN :ids")
    List<Order> findAllByIds(Long[] ids);
}
