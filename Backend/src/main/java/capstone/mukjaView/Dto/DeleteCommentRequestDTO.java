package capstone.mukjaView.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteCommentRequestDTO {
    private String oauthIdentifier;
    private Long commentId;
}
