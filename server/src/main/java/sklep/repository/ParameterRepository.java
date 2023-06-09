package sklep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sklep.entity.Parameters;


public interface ParameterRepository extends JpaRepository<Parameters, Long> {

    Parameters findByName(String parametersName);
    Parameters findByValue(String parametersValue);

    boolean existsByName(String parametersName);
    boolean existsByValue(String parametersValue);


}
