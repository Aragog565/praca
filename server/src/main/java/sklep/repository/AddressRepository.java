package sklep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sklep.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
