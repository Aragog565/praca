package sklep.repository;

import sklep.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByName(String email);

    User findByEmail(String email);

    boolean existsByName(String name);

    boolean existsByEmail(String email);
}
