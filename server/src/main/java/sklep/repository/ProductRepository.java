package sklep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sklep.entity.Category;
import sklep.entity.Product;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByName(String name);
    @Query("SELECT p FROM Product p JOIN p.category c WHERE c.id = :categoryId AND p.name LIKE %:name%")
    List<Product> findAll(Long categoryId, String name);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:name%")
    List<Product> findAll(String name);

    @Query("SELECT p FROM Product p WHERE p.id IN :ids")
    List<Product> findAllByIds(Long[] ids);

    boolean existsByName(String name);

}
