package sklep.repository;

import org.springframework.data.jpa.repository.Query;
import sklep.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>{
    List<Comment> findAllByUser_IdOrderByCreatedAtDesc(Long userID);

    Optional<Comment> findByIdAndProduct_IdOrderByCreatedAtDesc(Long id, Long productID);

    List<Comment> findAllByProduct_IdOrderByCreatedAtDesc(Long productID);
    @Query("select c from Comment c where c.content like %:content% and " +
            "(:userID is null or c.user.id = :userID) and" +
            "(:productID is null or c.product.id = :productID)")
    List<Comment> searchComments(String content, Long userID, Long productID);
}
