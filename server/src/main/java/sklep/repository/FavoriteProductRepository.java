package sklep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sklep.entity.FavoriteProduct;

import java.util.List;
import java.util.Optional;

public interface FavoriteProductRepository extends JpaRepository<FavoriteProduct, Long> {
    List<FavoriteProduct> findAllByUser_IdOrderByCreatedAtDesc(Long userID);

    Optional<FavoriteProduct> findByIdAndProduct_IdOrderByCreatedAtDesc(Long id, Long productID);

    FavoriteProduct findByProduct_IdAndUser_IdOrderByCreatedAtDesc(Long productID, Long userID);

    @Query("SELECT fp FROM FavoriteProduct fp WHERE fp.id IN :ids")
    List<FavoriteProduct> findAllByIds(Long[] ids);

    @Query("SELECT fp FROM FavoriteProduct fp JOIN fp.product p WHERE p.name LIKE %:name%")
    List<FavoriteProduct> findAll(String name);

    @Query("SELECT fp FROM FavoriteProduct fp JOIN fp.product p JOIN p.category c WHERE c.id = :categoryId AND p.name LIKE %:name%")
    List<FavoriteProduct> findAll(Long categoryId, String name);

}
