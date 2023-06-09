package sklep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sklep.entity.Category;
import sklep.entity.Product;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);

    boolean existsByName(String name);
}
