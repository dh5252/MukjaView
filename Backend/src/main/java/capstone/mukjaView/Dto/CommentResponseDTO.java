package capstone.mukjaView.Dto;

import capstone.mukjaView.Domain.Comment;
import capstone.mukjaView.Domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponseDTO {
    private Long commentId;
    private String oauthIdentifier;
    private String nickname;
    private String text;
    private String imgUrl;
    private String emotion;
    private String userMukbti;

    public CommentResponseDTO(Comment comment) {
        this.commentId = comment.getCommentId();
        this.oauthIdentifier = comment.getUser().getUsername();
        this.nickname = comment.getUser().getNickName();
        this.text = comment.getComment();
    }
}
