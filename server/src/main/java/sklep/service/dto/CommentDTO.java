package sklep.service.dto;
import sklep.entity.User;

import java.sql.Timestamp;
import java.util.List;

public class CommentDTO {
    private Long id;

    private String content;

    private Timestamp createdAt;

    private Timestamp lastModificationAt;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getLastModificationAt() {
        return lastModificationAt;
    }

    public void setLastModificationAt(Timestamp lastModificationAt) {
        this.lastModificationAt = lastModificationAt;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
