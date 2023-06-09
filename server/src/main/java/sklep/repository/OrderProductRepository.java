package sklep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sklep.entity.OrderProduct;

import java.util.List;


public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

}
