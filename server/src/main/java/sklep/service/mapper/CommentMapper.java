package sklep.service.mapper;

import org.mapstruct.Mapper;
import sklep.entity.Comment;
import sklep.service.dto.CommentDTO;
import sklep.service.dto.Create.CreatedCommentDTO;

@Mapper(componentModel = "spring")
public interface CommentMapper extends EntityMapper<Comment, CommentDTO>{
    Comment toCommentFromCreatedCommentDTO(CreatedCommentDTO createdCommentDTO);

    Comment toEntity(CommentDTO commentDTO);

    CommentDTO toDto(Comment comment);
}
