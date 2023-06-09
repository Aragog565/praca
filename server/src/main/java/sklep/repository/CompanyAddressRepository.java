package sklep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sklep.entity.CompanyAddress;


public interface CompanyAddressRepository extends JpaRepository<CompanyAddress, Long> {

    CompanyAddress findByUserId(Long id);

}
