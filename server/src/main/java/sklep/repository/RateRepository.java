package sklep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import sklep.entity.Comment;
import sklep.entity.Rate;

import java.util.List;
import java.util.Optional;

public interface RateRepository extends JpaRepository<Rate, Long> {
    List<Rate> findAllByUser_IdOrderByCreatedAtDesc(Long userID);

    Optional<Rate> findByIdAndProduct_IdOrderByCreatedAtDesc(Long id, Long productID);

    Rate findByProduct_IdAndUser_IdOrderByCreatedAtDesc(Long productID, Long userID);


}
