package sklep.service.dto.Update;
import sklep.config.Constants;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class UpdateCommentDTO {
    @NotEmpty
    @Size(max= Constants.COMMENT_CONTENT_MAX_LENGTH)
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
