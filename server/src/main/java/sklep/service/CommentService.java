package sklep.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sklep.entity.Comment;
import sklep.entity.Product;
import sklep.entity.User;
import sklep.repository.CommentRepository;
import sklep.repository.ProductRepository;
import sklep.repository.UserRepository;
import sklep.service.dto.CommentDTO;
import sklep.service.dto.Create.CreatedCommentDTO;
import sklep.service.dto.Update.UpdateCommentDTO;
import sklep.service.exception.EntityNotFoundException;
import sklep.service.exception.ForbiddenException;
import sklep.service.mapper.CommentMapper;
import java.sql.Timestamp;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CommentService {
    private final static Logger log = LoggerFactory.getLogger(CommentService.class);

    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final SecurityService securityService;

    public CommentService(CommentMapper commentMapper,
                          CommentRepository commentRepository,
                          UserRepository userRepository,
                          ProductRepository productRepository,
                          SecurityService securityService) {
        this.commentMapper = commentMapper;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.securityService = securityService;
    }
    public CommentDTO save(CreatedCommentDTO createdCommentDTO, User authenticatedUser, Long productID){
        log.debug("Saving: comment {} in product {}", createdCommentDTO, productID);

        Product product = productRepository.findById(productID)
                .orElseThrow(() -> new EntityNotFoundException("Product with requested id doesn't exists."));

        if(authenticatedUser==null){
            throw new ForbiddenException("zaloguj się");
        }
        Comment comment = commentMapper.toCommentFromCreatedCommentDTO(createdCommentDTO);
        comment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        comment.setLastModificationAt(new Timestamp(System.currentTimeMillis()));
        comment.setProduct(product);
        comment.setUser(authenticatedUser);

        return commentMapper.toDto(commentRepository.save(comment));
    }

    public List<CommentDTO> getProductComments(Long productID){
        log.debug("Fetching: comments of product {}", productID);

        handleIfProductExists(productID);
        List<Comment> comments = commentRepository.findAllByProduct_IdOrderByCreatedAtDesc(productID);

        return commentMapper.toDto(comments);
    }

    public List<CommentDTO> searchComments(String query,
                                           Long userID,
                                           Long productID
    ){
        log.debug("Fetching: searching comments");

        List<Comment> comments = commentRepository.searchComments(query, userID, productID);

        return commentMapper.toDto(comments);
    }

    public void updateComment(Long id, Long productID, UpdateCommentDTO updateCommentDTO, User authenticatedUser){
        log.debug("Updating: comment {} to {}", id, updateCommentDTO);

        Comment comment = getEditableComment(id, productID, authenticatedUser);
        comment.setContent(updateCommentDTO.getContent());
        comment.setLastModificationAt(new Timestamp(System.currentTimeMillis()));

        commentRepository.save(comment);
    }

    public void deleteComment(Long id, Long postID, User authenticatedUser){
        log.debug("Deleting: comment {}", id);

        Comment comment = getEditableComment(id, postID, authenticatedUser);

        commentRepository.delete(comment);
    }

    private Comment getEditableComment(Long id, Long productID, User authenticatedUser){
        handleIfProductExists(productID);
        Comment comment = commentRepository.findByIdAndProduct_IdOrderByCreatedAtDesc(id, productID)
                .orElseThrow(() -> new EntityNotFoundException("Comment with requested id doesn't exist under requested product."));
        securityService.checkPermission(authenticatedUser,comment.getUser().getId());

        return comment;
    }

    private void handleIfProductExists(Long productID){
        if(!productRepository.existsById(productID)){
            throw new EntityNotFoundException("Product with requested id doesn't exist.");
        }
    }
}
