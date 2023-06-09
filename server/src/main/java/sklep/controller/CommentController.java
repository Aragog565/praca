package sklep.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sklep.entity.User;
import sklep.service.CommentService;
import sklep.service.dto.CommentDTO;

import java.util.List;
import org.springframework.http.ResponseEntity;
import sklep.service.dto.Create.CreatedCommentDTO;
import sklep.service.dto.Update.UpdateCommentDTO;


@CrossOrigin
@RestController
@RequestMapping(path="/api/products")
public class CommentController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{productID}/comments")
    public ResponseEntity<CommentDTO> addComment(
            @PathVariable final Long productID,
            @RequestBody @Validated CreatedCommentDTO createdCommentDTO,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        log.debug("Request to save: comment {} in product {}", createdCommentDTO, productID);

        CommentDTO commentDTO = commentService.save(createdCommentDTO, authenticatedUser, productID);

        return ResponseEntity.ok(commentDTO);
    }

    @GetMapping("/{productID}/comments")
    public ResponseEntity<List<CommentDTO>> getPostComments(
            @PathVariable final Long productID
    ){
        log.debug("Request to get: product {} comments", productID);

        List<CommentDTO> commentDTOS = commentService.getProductComments(productID);

        return ResponseEntity.ok(commentDTOS);
    }

    @GetMapping("/comments")
    public ResponseEntity<List<CommentDTO>> searchComments(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(required = false) Long userID,
            @RequestParam(required = false) Long productID
    ){
        log.debug("Request to search: comments");

        List<CommentDTO> commentDTOS = commentService.searchComments(query, userID, productID);

        return ResponseEntity.ok(commentDTOS);
    }

    @PutMapping("/{productID}/comments/{id}")
    public ResponseEntity<Void> updateComment(
            @PathVariable final long productID,
            @PathVariable final Long id,
            @RequestBody @Validated UpdateCommentDTO updateCommentDTO,
            @AuthenticationPrincipal User authenticatedUser
    ){
        log.debug("Request to update: comment {} to {}", id, updateCommentDTO);

        commentService.updateComment(id, productID, updateCommentDTO, authenticatedUser);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{productID}/comments/{id}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable final long productID,
            @PathVariable final Long id,
            @AuthenticationPrincipal User authenticatedUser
    ){
        log.debug("Request to delete: comment {}", id);

        commentService.deleteComment(id, productID, authenticatedUser);

        return ResponseEntity.noContent().build();
    }

}
